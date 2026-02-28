package com.group_13;

import java.time.Instant;
import java.util.ArrayList;



public class App 
{

    private static void insertTest(ApiClient client, TestData data, String table, int numOfInserts)
    {
        try {
            for (int i = 0; i < numOfInserts; i++) {
                client.insert(data.generateRandomData(), table);
            }
        } catch (Exception e) {

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

        System.out.println("Inserts: " + Long.toString(inserts) + "  Time: " + Float.toString(time) + "sec");
        System.out.println("Speed: " + Float.toString(speed) + " inserts / second");
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

            TestData recordData = new TestData();
            recordData.addStringVariable("operation", "operations.txt");
            recordData.addStringVariable("responsible", "responsibles.txt");

            
            ApiClient client = new ApiClient("DS26oulu:8001", "root", "root");
            
            multiThreadedInsert(client, patientData, "patients", 1000, 250);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
