package com.group_13;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase{

    // Class attributes instantiation
    boolean dbExists = false;
    Connection connectionObject;
    private String dbName;
    private static DataBase dbInstance;
    SecureRandom secureRandom;


    // Method for getting db instance.
    public static synchronized DataBase getDatabase(){
        if(dbInstance == null){
            dbInstance = new DataBase();
        }
        return dbInstance;
    }

    // Method for opening the database
    public void open(String dbName, String dbPath, String dbUser, String dbPw){//
        //for testing
       // String mysqlPath = "jdbc:mysql://localhost:3306/";
       // String dbUser = "root";
       // String dbpw = "";
        //for testing

        if(dbName.isBlank() || dbName == null 
        ||dbPath.isBlank()|| dbPath == null){
            throw new InvalidParameterException();
        }
        this.dbName = dbName;
        if(tietokanta löytyi){
            dbExists = true;
            System.out.println("Found previous database.");
            String JDBC_CONNECTION_ADDRESS = dbPath+dbName;
            this.connectionObject = DriverManager.getConnection(JDBC_CONNECTION_ADDRESS, dbUser, dbPw);
        }else{
            initDatabase(dbName, dbPath, dbUser, dbPw);
        }

    }

    


    // Method for  overall initializing of the database comprising db creation and table creation
    private boolean initDatabase(String dbName, String dbPath, String user,String pwd){
        try{
            String JDBC_CONNECTION_ADDRESS = dbPath+dbName;
            this.connectionObject = DriverManager.getConnection(JDBC_CONNECTION_ADDRESS, user, pwd);

            if(createDatabase()){
                System.out.println("DB created");
            }
            System.out.println("Could not create tables");
            return false;

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("DB creation failed.");
            return false;
        }
    }

    // Function for creating the database
    private boolean createDatabase() throws SQLException{
        String createDB_string = "CREATE DATABASE HealthCenter";
        System.out.println("Trying to create new Database");
        if(connectionObject!=null){
            Statement sqlstatement = connectionObject.createStatement();
            sqlstatement.executeUpdate(createDB_string);
            sqlstatement.close();
            return true;
        }
        return false;
    }



String createTablePatients = "CREATE TABLE patients("+
    "Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
    "socialSecNum INT,"+
    "fName VARCHAR(150) NOT NULL,"+ // use temporary if needed
    "lName VARCHAR(150),"+
    "dateofbirth DATE,"+
    "address VARCHAR(150),"+
    "phone VARCHAR(150),"+
    "emergency_contact VARCHAR(150),"+
    "homehospital VARCHAR(150)"+
")";
        



String createTableHealthRecords = "CREATE TABLE HealthRecords("+
"recID INT PRIMARY KEY NOT_NULL AUTO_INCREMENT,"+
"FOREIGN KEY patientID INT NOT NULL,"+
"datetime TIMESTAMP,"+
"operation VARCHAR(400),"+
"responsible VARCHAR(100),"+
"followUp VARCHAR(150),"+
")";


}