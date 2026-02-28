package com.group_13;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReplicaSync
{
    static long syncIntervalMs = 3000;
    private static Thread syncThread = null;
    private static volatile boolean syncThreadRunning = false;

    private static final ExecutorService fetchThreadPool = Executors.newFixedThreadPool(64);


    static class SyncOperation
    {
        public SyncOperation(String type, String table, long rowId, Future<JSONObject> rowValues, long time, long changeId) {
            this.type = type;
            this.table = table;
            this.rowId = rowId;
            this.rowValues = rowValues;
            this.time = time;
            this.changeId = changeId;
        }
        public String type;
        public String table;
        public long rowId;
        public long time;
        public long changeId;
        public Future<JSONObject> rowValues;
    }


    @SuppressWarnings("BusyWait")
    static private void syncThreadEntry()
    {
        while (syncThreadRunning) {
            try {
                Thread.sleep(syncIntervalMs);
                for (HospitalNode n : HospitalNetwork.getInstance().getAllNodes()) {
                    if (n.isReplica()) {
                        syncReplica(n);
                    }
                }
            } catch (InterruptedException e) { }
        }
    }

    static public void startSyncThread()
    {
        if (syncThread == null) {
            System.out.println("Starting sync thread");
            syncThreadRunning = true;
            syncThread = new Thread(ReplicaSync::syncThreadEntry);
            syncThread.start();
        }
    }

    static public void shutdownSyncThread()
    {
        if (syncThreadRunning) {
            syncThreadRunning = false;
            try {
                System.out.println("Closing sync thread");
                syncThread.interrupt();  //Wakes thread from sleep
                syncThread.join();
            } catch (InterruptedException e) {
            }
        }
    }


    static JSONArray queryChangesLog(HospitalNode node, long sinceMs) throws Exception {
        //This api call gives "changelog" about database writes (and deletes) since given timestamp
        String fullUrl = "https://" + node.getAddress() + "/api/sync?since=" + Long.toString(sinceMs);
        
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(fullUrl))
                                         .GET()
                                         .build();

        HttpResponse<String> response = Server.client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() == ServerUtility.HttpStatus.OK.code()) {
            return new JSONArray(response.body());
        } else {
            return new JSONArray();
        }
    }

    static JSONObject queryChanges(HospitalNode node, long logId) throws Exception {
        //This api call gives column name - value pairs for spesific "changelog" row
        String fullUrl = "https://" + node.getAddress() + "/api/sync?logid=" + Long.toString(logId);
        
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(fullUrl))
                                         .GET()
                                         .build();

        HttpResponse<String> response = Server.client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return new JSONObject(response.body());
    }

    static long getLastSyncId(DataBase replicaDB) throws Exception
    {
        long lastChangeLogId = 0;
        try {
            lastChangeLogId = DataBaseQueryHelper.queryWithRowId(replicaDB, "lastsync", 1).getJSONObject(0).getLong("lastsyncid");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Creating row for lastsyncid");

            JSONObject syncRow = new JSONObject();
            syncRow.put("lastsyncid", 0);
            DataBaseQueryHelper.insert(replicaDB, "lastsync", syncRow);
        }
        return lastChangeLogId;
    }

    static ArrayList<SyncOperation> fetchChanges(HospitalNode node, DataBase replicaDB) throws Exception
    {
        long lastChangeLogId = getLastSyncId(replicaDB);

        ArrayList<SyncOperation> ops = new ArrayList<>();

        //Query changelog which tells what operations has been made to database
        JSONArray changes = queryChangesLog(node, lastChangeLogId);

        if (changes.length() == 0) {
            return null; //Query either failed or there is no changes, no need to update last sync time or do anything
        }

        //Iterating changes
        for (int i = 0; i < changes.length(); i++) {
            JSONObject o = changes.getJSONObject(i);

            String table = o.getString("tablename"); //this tells which table was modified
            String type = o.getString("type");       //this tells what operation (INSERT, UPDATE, DELETE) was made
            long logId = o.getLong("id");            //this is id for change, it is used to query modified column values
            long rowId = o.getLong("rowid");         //which row in table was modified
            long time = o.getLong("timestamp");      //timestamp when operation was made

            Future<JSONObject> rowChanges = null;
            if (type.equalsIgnoreCase("INSERT") ||
                type.equalsIgnoreCase("UPDATE")) {

                //Change was INSERT or UPDATE
                //Lets query columns values which was inserted/updated
                //Uses futures to increase performance
                rowChanges = fetchThreadPool.submit(() -> queryChanges(node, logId));
            }

            //Store database operation
            ops.add(new SyncOperation(type, table, rowId, rowChanges, time, logId));
        }

        return ops;
    }


    static long replayChanges(HospitalNode node, ArrayList<SyncOperation> ops, DataBase replicaDB)
    {
        int numOfInserts = 0;
        int numOfDeletes = 0;
        int numOfUpdates = 0;

        long latestChangeId = 0;

        //replay database operations
        for (SyncOperation o : ops) {
            try {
                //replay operation
                if (o.type.equalsIgnoreCase("DELETE")) {
                    DataBaseQueryHelper.delete(replicaDB, o.table, o.rowId);
                    numOfDeletes += 1;
                } else if (o.type.equalsIgnoreCase("INSERT")) {
                    DataBaseQueryHelper.insert(replicaDB, o.table, o.rowValues.get());
                    numOfInserts += 1;
                } else if (o.type.equalsIgnoreCase("UPDATE")) {
                    DataBaseQueryHelper.update(replicaDB, o.table, o.rowValues.get(), o.rowId);
                    numOfUpdates += 1;
                }

                //This is important to be after change is committed to database
                //If we fail to make change to replica, we don't want advance past that change id
                //Since rowvalues uses future, it is possible that connection fails mid sync
                if (o.changeId > latestChangeId) {
                    latestChangeId = o.changeId;
                }
            } catch (Exception e) {
                System.out.println("Error when syncing.");
                break;
            }
        }

        System.out.println("Replica " + node.getName() + " synced, INSERTS: " + Integer.toString(numOfInserts) + 
                                                                "  UPDATES: " + Integer.toString(numOfUpdates) +
                                                                "  DELETES: " + Integer.toString(numOfDeletes));

        return latestChangeId;
    }


    static void syncReplica(HospitalNode node)
    {
        try {
            DataBase replicaDB = DataBaseManager.getDataBase(node);

            //We want fetch all changes before replaying changes to replica database
            //This avoids desyncronization when connection is lost during sync
            ArrayList<SyncOperation> ops = fetchChanges(node, replicaDB);
            if (ops != null && !ops.isEmpty()) { //Only replay if there is changes
                long latestChangeId = replayChanges(node, ops, replicaDB);

                JSONObject updatedRow = new JSONObject();
                updatedRow.put("lastsyncid", latestChangeId);
                DataBaseQueryHelper.update(replicaDB, "lastsync", updatedRow, 1);
            }
        } catch (Exception e) {
            System.out.println("Error when syncing replica: " + e.getMessage());
        }
    }
}