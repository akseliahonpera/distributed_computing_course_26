package com.group_13;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReplicaSync
{
    static long syncIntervalMs = 3000;
    static Thread syncThread = null;
    static volatile boolean syncThreadRunning = false;


    static class SyncOperation
    {
        public SyncOperation(String type, String table, long rowId, JSONObject rowValues, long time) {
            this.type = type;
            this.table = table;
            this.rowId = rowId;
            this.rowValues = rowValues;
            this.time = time;
        }
        public String type;
        public String table;
        public long rowId;
        public long time;
        public JSONObject rowValues;
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
        String fullUrl = "http://" + node.getAddress() + "/api/sync?since=" + Long.toString(sinceMs);
        
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Authorization", "Bearer " + node.getAuthToken().getTokenStr())
                .GET()
                .build();

        HttpResponse<String> response = client.send(
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
        String fullUrl = "http://" + node.getAddress() + "/api/sync?logid=" + Long.toString(logId);
        
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Authorization", "Bearer " + node.getAuthToken().getTokenStr())
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return new JSONObject(response.body());
    }


    static ArrayList<SyncOperation> fetchChanges(HospitalNode node) throws Exception
    {
        ArrayList<SyncOperation> ops = new ArrayList<>();

        long lastSync = node.getLastSyncTime();

        //Query changelog which tells what operations has been made to database
        JSONArray changes = queryChangesLog(node, lastSync);

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

            JSONObject rowChanges = null;
            if (type.equalsIgnoreCase("INSERT") ||
                type.equalsIgnoreCase("UPDATE")) {

                //Change was INSERT or UPDATE
                //Lets query columns values which was inserted/updated
                rowChanges = queryChanges(node, logId);
            }

            //Store database operation
            ops.add(new SyncOperation(type, table, rowId, rowChanges, time));
        }

        return ops;
    }


    static long replayChanges(HospitalNode node, ArrayList<SyncOperation> ops) throws Exception
    {
        int numOfInserts = 0;
        int numOfDeletes = 0;
        int numOfUpdates = 0;

        long latestChangeTime = 0;

        //Get replicate database for node
        DataBase replicaDB = DataBaseManager.getDataBase(node);

        //replay database operations
        for (SyncOperation o : ops) {
            if (o.time > latestChangeTime) {
                latestChangeTime = o.time;
            }

            //replay operation
            if (o.type.equalsIgnoreCase("DELETE")) {
                DataBaseQueryHelper.delete(replicaDB, o.table, o.rowId);
                numOfDeletes += 1;
            } else if (o.type.equalsIgnoreCase("INSERT")) {
                DataBaseQueryHelper.insert(replicaDB, o.table, o.rowValues);
                numOfInserts += 1;
            } else if (o.type.equalsIgnoreCase("UPDATE")) {
                DataBaseQueryHelper.update(replicaDB, o.table, o.rowValues, o.rowId);
                numOfUpdates += 1;
            }
        }

        System.out.println("Replica " + node.getName() + " synced, INSERTS: " + Integer.toString(numOfInserts) + 
                                                                "  UPDATES: " + Integer.toString(numOfUpdates) +
                                                                "  DELETES: " + Integer.toString(numOfDeletes));

        return latestChangeTime;
    }


    static void syncReplica(HospitalNode node)
    {
        try {
            //We want fetch all changes before replaying changes to replica database
            //This avoids desyncronization when connection is lost during sync
            ArrayList<SyncOperation> ops = fetchChanges(node);
            if (ops != null && !ops.isEmpty()) { //Only replay if there is changes
                long latestChangeTime = replayChanges(node, ops);

                //Lets store last timestamp, which is used at next sync
                node.setLastSyncTime(latestChangeTime);
                //Store hospitalnetwork datastructure to disk, because it contains lastsync timestamp
                HospitalNetwork.getInstance().save();
            }
        } catch (Exception e) {
            System.out.println("Error when syncing replica: " + e.getMessage());
        }
    }
}