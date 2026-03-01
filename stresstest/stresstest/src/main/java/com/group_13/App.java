package com.group_13;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONArray;



public class App 
{

    private static void insertTest(ApiClient client, TestData data, String table, int numOfInserts)
    {
        try {
            for (int i = 0; i < numOfInserts; i++) {
                client.insert(data.generateRandomData(), table);
            }
        } catch (Exception e) {
            System.out.println("Insert failed!!!");
        }
    }

    private static void updateTest(ApiClient client, ArrayList<Long> ids, TestData data, String table, int numOfUpdates)
    {
        Random rng = new Random();
        try {
            for (int i = 0; i < numOfUpdates; i++) {
                client.update(data.generateSingleVariable(), ids.get(rng.nextInt(ids.size())), table);
            }
        } catch (Exception e) {
            System.out.println("Update failed!!!");
        }
    }

    private static void queryByNameTest(ApiClient client, TestData data, int numOfQueries)
    {
        try {
            for (int i = 0; i < numOfQueries; i++) {
                TreeMap<String, String> qmap = new TreeMap<>();
                qmap.put("fname", data.generateRandomValue("fname"));
                qmap.put("lname", data.generateRandomValue("lname"));

                client.query("patients", qmap).length();
            }
        } catch (Exception e) {
            System.out.println("Query failed");
        }
    }

    private static void queryByIdTest(ApiClient client, ArrayList<Long> ids, String table, int numOfQueries)
    {
        Random rng = new Random();

        try {
            for (int i = 0; i < numOfQueries; i++) {
                TreeMap<String, String> qmap = new TreeMap<>();
                qmap.put("id", Long.toString(ids.get(rng.nextInt(ids.size()))));

                client.query(table, qmap).length();
            }
        } catch (Exception e) {
            System.out.println("Query failed");
        }
    }

    private static void multiThreadedInsert(ApiClient client, TestData data, String table, int numOfInserts, int numOfThreads) throws Exception
    {
        long inserts = 0;
        long t0 = Instant.now().toEpochMilli();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            inserts += numOfInserts/numOfThreads;
            Thread t = new Thread(() -> insertTest(client, data, table, numOfInserts/numOfThreads));
            t.start();

            threads.add(t);
        }

        for (int i = 0; i < numOfThreads; i++) {
            threads.get(i).join();
        }

        long t1 = Instant.now().toEpochMilli();

        float time = (float)(t1 - t0) / 1000;
        float speed = (float)inserts / time;

        System.out.println("Insert test completed!");
        System.out.println("Inserts: " + Long.toString(inserts) + "  Time: " + Float.toString(time) + "sec");
        System.out.println("Speed: " + Float.toString(speed) + " inserts / second");
    }

    private static void multiThreadedUpdate(ApiClient client, TestData data, ArrayList<Long> ids, String table, int numOfUpdates, int numOfThreads) throws Exception
    {
        long updates = 0;
        long t0 = Instant.now().toEpochMilli();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            updates += numOfUpdates/numOfThreads;
            Thread t = new Thread(() -> updateTest(client, ids, data, table, numOfUpdates/numOfThreads));
            t.start();

            threads.add(t);
        }

        for (int i = 0; i < numOfThreads; i++) {
            threads.get(i).join();
        }

        long t1 = Instant.now().toEpochMilli();

        float time = (float)(t1 - t0) / 1000;
        float speed = (float)updates / time;

        System.out.println("Update test completed!");
        System.out.println("Updates: " + Long.toString(updates) + "  Time: " + Float.toString(time) + "sec");
        System.out.println("Speed: " + Float.toString(speed) + " updates / second");
    }

    private static void multiThreadedQueryById(ApiClient client, ArrayList<Long> ids, String table, int numOfQueries, int numOfThreads) throws Exception
    {
        long queries = 0;
        long t0 = Instant.now().toEpochMilli();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            queries += numOfQueries/numOfThreads;
            Thread t = new Thread(() -> queryByIdTest(client, ids, table, numOfQueries/numOfThreads));
            t.start();

            threads.add(t);
        }

        for (int i = 0; i < numOfThreads; i++) {
            threads.get(i).join();
        }

        long t1 = Instant.now().toEpochMilli();

        float time = (float)(t1 - t0) / 1000;
        float speed = (float)queries / time;

        System.out.println("Query by ID test completed!");
        System.out.println("Queries: " + Long.toString(queries) + "  Time: " + Float.toString(time) + "sec");
        System.out.println("Speed: " + Float.toString(speed) + " queries / second");
    }

    private static void multiThreadedQueryByName(ApiClient client, TestData data, int numOfQueries, int numOfThreads) throws Exception
    {
        long queries = 0;
        long t0 = Instant.now().toEpochMilli();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            queries += numOfQueries/numOfThreads;
            Thread t = new Thread(() -> queryByNameTest(client, data, numOfQueries/numOfThreads));
            t.start();

            threads.add(t);
        }

        for (int i = 0; i < numOfThreads; i++) {
            threads.get(i).join();
        }

        long t1 = Instant.now().toEpochMilli();

        float time = (float)(t1 - t0) / 1000;
        float speed = (float)queries / time;

        System.out.println("Query by random name test completed!");
        System.out.println("Queries: " + Long.toString(queries) + "  Time: " + Float.toString(time) + "sec");
        System.out.println("Speed: " + Float.toString(speed) + " queries / second");
    }


    private static ArrayList<Long> getIds(ApiClient client, String table, long prefixFilter) throws Exception
    {
        ArrayList<Long> ids = new ArrayList<>();
        
        JSONArray array = client.query(table, null);
        for (int i = 0; i < array.length(); i++) {
            long id = array.getJSONObject(i).getLong("id");

            long prefix = (id >> 48) & 0xFFFF;
            if (prefix == prefixFilter) {
                ids.add(id);
            }
        }
        System.out.println("Found " + Integer.toString(ids.size()) + " IDs from table " + table);
        return ids;
    }



    private static void testQueryByIdLatency(ApiClient client, TestData data, ArrayList<Long> ids, String table) throws Exception
    {
        long t0 = Instant.now().toEpochMilli();

        queryByIdTest(client, ids, table, 100);

        long t1 = Instant.now().toEpochMilli();

        float avgLatency = (float)(t1 - t0) / 100;
        
        System.out.println("Average query by ID latency: " + Float.toString(avgLatency) + "ms\n");
    }

    private static void testQueryByNameLatency(ApiClient client, TestData data) throws Exception
    {
        long t0 = Instant.now().toEpochMilli();

        queryByNameTest(client, data, 100);

        long t1 = Instant.now().toEpochMilli();

        float avgLatency = (float)(t1 - t0) / 100;
        
        System.out.println("Average query by name latency: " + Float.toString(avgLatency) + "ms\n");
    }

    private static void testInsertLatency(ApiClient client, TestData data, String table) throws Exception
    {
        long t0 = Instant.now().toEpochMilli();

        insertTest(client, data, table, 100);

        long t1 = Instant.now().toEpochMilli();

        float avgLatency = (float)(t1 - t0) / 100;
        
        System.out.println("Average insert latency: " + Float.toString(avgLatency) + "ms\n");
    }

    private static void testUpdateLatency(ApiClient client, TestData data, ArrayList<Long> ids, String table) throws Exception
    {
        long t0 = Instant.now().toEpochMilli();

        updateTest(client, ids, data, table, 100);

        long t1 = Instant.now().toEpochMilli();

        float avgLatency = (float)(t1 - t0) / 100;
        
        System.out.println("Average update latency: " + Float.toString(avgLatency) + "ms\n");
    }



    private static void runThroughputTests(ApiClient client, TestData data) throws Exception
    {
        multiThreadedInsert(client, data, "patients", 10000, 250);

        ArrayList<Long> ids = getIds(client, "patients", 0);

        multiThreadedUpdate(client, data, ids, "patients", 10000, 250);
        multiThreadedQueryById(client, ids, "patients", 10000, 250);
        multiThreadedQueryByName(client, data, 1000, 90);
    }

    private static void runLatencyTests(ApiClient client, ApiClient otherClient, TestData data) throws Exception
    {
        insertTest(client, data, "patients", 100);      //These makes sure that database is not empty
        insertTest(otherClient, data, "patients", 100);
        
        ArrayList<Long> ids = getIds(client, "patients", 0);
        ArrayList<Long> otherIds = getIds(otherClient, "patients", 1);

        testQueryByIdLatency(client, data, ids, "patients");
        testQueryByNameLatency(client, data);
        testInsertLatency(client, data, "patients");
        testUpdateLatency(client, data, ids, "patients");

        System.out.println("Testing update latency when IDs are owned by other node (updates are forwarded)");
        testUpdateLatency(client, data, otherIds, "patients");
    }

    public static void main( String[] args ) throws Exception
    {
        try {

            TestData patientData = new TestData();

            patientData.addStringVariable("fname", "fnames.txt");
            patientData.addStringVariable("lname", "lnames.txt");
            patientData.addStringVariable("address", "streetnames.txt");
            patientData.addStringVariable("homehospital", "hospitals.txt");
            patientData.addIntegerVariable("phone", 1000000, 9999999);
            patientData.addIntegerVariable("emergencycontact", 1000000, 9999999);

            ApiClient client = new ApiClient("DS26oulu:8001", "root", "root");
            ApiClient otherClient = new ApiClient("DS26tampere:8002", "root", "root");

            //runThroughputTests(client, patientData);
            runLatencyTests(client, otherClient, patientData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
