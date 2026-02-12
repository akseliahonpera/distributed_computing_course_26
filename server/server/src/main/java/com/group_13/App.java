package com.group_13;

import java.sql.SQLException;

import javax.xml.crypto.Data;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, SQLException
    {
        System.out.println( "Hello World! ||||||||||||||||||||||||||||||||||||||||||||||||||||||||" );
        int i = 0;
        String dbPath = "jdbc:mysql://localhost:3306/";
        String dbName = "DS26";
        String dbuser = "root";
        String dbPw = "Gambiinakiuas522";


        try{   
            while(i<1){
                System.out.println("Testing database class initiation."); 
                DataBase testDb = DataBase.getDatabase();

                System.out.println("Testing opening database."); 
                testDb.open(dbName, dbPath, dbuser, dbPw);
                Thread.sleep(2000);
                i++;
            } 
        }catch(Exception e){
        e.printStackTrace();
        System.out.println("database test failed");
        }
        DataBase db = DataBase.getDatabase();
        test_patient patient1 = new test_patient("1", "pekka", "eräpekka", "01020300000","04531","pallikuja","+0123456789","+0123456789","oulu");
        JSONObject patietn1Json = new JSONObject(patient1);
        
        DataBaseQueryHelper.insert(db, "patients", patietn1Json);

        try {

            int id = 1;
            test_patient patient2 = new test_patient("1", "antti", "eräpekka", "01020300000","04531","pallikuja","+0123456789","+0123456789","oulu");

            JSONObject patientJson = new JSONObject(patient2);
            
            DataBaseQueryHelper.update(db, "patients", patientJson, id);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}