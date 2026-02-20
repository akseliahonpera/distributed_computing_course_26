package com.group_13;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReplicaSync
{
    static long syncIntervalMs = 3000;
    static Thread syncThread = null;
    static volatile boolean syncThreadRunning = false;

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


    static JSONArray queryChanges(HospitalNode node, long sinceMs) throws Exception {
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

    static JSONObject queryRow(HospitalNode node, String table, long rowId) throws Exception {
        String fullUrl = "http://" + node.getAddress() + "/api/" + table + "?id=" + Long.toString(rowId);
        
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

        JSONArray array = new JSONArray(response.body());
        if (array.length() == 0) {
            return new JSONObject();
        } else {
            return array.getJSONObject(0);
        }
    }

    static JSONObject createDummyRowForTable(DataBase db, String tableName)
    {
        JSONObject o = new JSONObject();
        DataBaseTable def = db.getDefinition(tableName);
        for (String col : def.getVarNames()) {
            if (def.getVarType(col).toUpperCase().contains("VARCHAR")) {
                o.put(col, "DUMMY");
            }
        }
        return o;
    }

    static void syncReplica(HospitalNode node)
    {
        System.out.print("Attempting to sync replica " + node.getName() + " " + node.getId() + "... ");
        try {
            long lastSync = node.getLastSyncTime();

            //Lähetään query jolla selviää mitä muutoksia on tapahtunut viime synkkauksen jälkeen
            JSONArray changes = queryChanges(node, lastSync);

            if (changes.length() == 0) {
                System.out.println("No changes");
                return; //Query either failed or there is no changes, no need to update last sync time or do anything
            }
            int numOfInserts = 0;
            int numOfDeletes = 0;
            int numOfUpdates = 0;

            long latestChangeTime = 0;

            //haetaan replica database
            DataBase replicaDB = DataBaseManager.getDataBase(node);

            //Käydään muutokset läpi
            for (int i = 0; i < changes.length(); i++) {
                JSONObject o = changes.getJSONObject(i);


                String table = o.getString("tablename");
                String type = o.getString("type");
                long rowId = o.getLong("rowid");
                long time = o.getLong("timestamp");

                if (time > latestChangeTime) {
                    latestChangeTime = time;
                }

                if (type.equalsIgnoreCase("DELETE")) { //poisto, poistetaan rivi replicasta
                    DataBaseQueryHelper.delete(replicaDB, table, rowId);
                    numOfDeletes += 1;
                } else if (type.equalsIgnoreCase("INSERT")) { //Insertti, lisätään uusi rivi
                    //Haetaan rivi omistajalta
                    JSONObject row = queryRow(node, table, rowId);

                    //On mahdollista että on lisätty rivi, joka on omistajalla jo poistettu
                    //Rivi pitää kuitenkin lisätä ettei SQL auto_increment mee sekasin
                    if (row.length() == 0) {
                        //Luodaa dummy rivi
                        //Tämä kuitenkin poistetaan tässä synkkauksessa joten datalla ei väliä
                        row = createDummyRowForTable(replicaDB, table);
                    }
                    //Lisätään rivi replicaan
                    DataBaseQueryHelper.insert(replicaDB, table, row);
                    numOfInserts += 1;
                } else if (type.equalsIgnoreCase("UPDATE")) {
                    JSONObject row = queryRow(node, table, rowId);
                    //On mahdollista että päivitys on tehty riviin, joka on jo poistettu
                    //Updatella ei oo väliä (koska meillä ei ole vierasavaimia)
                    if (row.length() > 0) {
                        DataBaseQueryHelper.update(replicaDB, table, row, rowId);
                    }
                    numOfUpdates += 1;
                }
            }

            System.out.println("Replica synced, INSERTS: " + Integer.toString(numOfInserts) + 
                                             "  UPDATES: " + Integer.toString(numOfUpdates) +
                                             "  DELETES: " + Integer.toString(numOfDeletes));
            
            //Tallennetaa viimeisimmän muokkauksen aikaleima
            //Seuraava synkkaus hakee tätä uudemmat muokkaukset
            node.setLastSyncTime(latestChangeTime);
            //Tallennetaan sairaalaverkon conffitiedosto, koska siihen on tallennettu viimeisin synkkausaika
            HospitalNetwork.getInstance().save();
        } catch (Exception e) {
            System.out.println("Error when syncing replica: " + e.getMessage());
        }
    }
}