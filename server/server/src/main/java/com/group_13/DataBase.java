package com.group_13;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;

public class DataBase{

    // Class attributes instantiation
    Connection connectionObject;
    private static DataBase dbInstance;


    // Method for getting db instance.
    public static synchronized DataBase getDatabase(){
        if(dbInstance == null){
            System.out.println("Attempting to create new db object.");
            dbInstance = new DataBase();
        }
        System.out.println("returning existing db object");
        return dbInstance;
    }

    // Method for opening the database
    public void open(String dbName, String dbPath, String dbUser, String dbPw) throws SQLException{
        if(dbName.isBlank() || dbName == null 
        ||dbPath.isBlank()|| dbPath == null){
            throw new InvalidParameterException();
        }
        if(databaseExists(dbName, dbPath, dbUser, dbPw)){
            System.out.println("Found previous database.");
            
        }else{
            initDatabase(dbName, dbPath, dbUser, dbPw);
        }
    }

    //method for checking if database exists, tries to create and assign connectionObject
    private boolean databaseExists(String dbName, String dbPath, String user,String pwd) throws SQLException{
        String JDBC_CONNECTION_ADDRESS = dbPath+dbName;
        System.out.println(JDBC_CONNECTION_ADDRESS);
        try{
            this.connectionObject = DriverManager.getConnection(JDBC_CONNECTION_ADDRESS, user, pwd);
            ResultSet resultSet = connectionObject.getMetaData().getCatalogs();
            while(resultSet.next()){
            // String databasename = resultSet.getString(1); // only need to see if db exists
                resultSet.close();
                return true;
            }
            resultSet.close();
        }catch(Exception e){
        e.printStackTrace();
            }
        return false;
    }


    // Method for  overall initializing of the database comprising db creation and table creation
    private boolean initDatabase(String dbName, String dbPath, String user,String pwd){
        System.out.println("starting initdatabase method");
        try{
            //String JDBC_CONNECTION_ADDRESS = dbPath+dbName;
            String JDBC_CONNECTION_ADDRESS = dbPath;
            System.out.println("try connection to root mysql");
            this.connectionObject = DriverManager.getConnection(JDBC_CONNECTION_ADDRESS, user, pwd);
            System.out.println("try connection to root mysql SUCCESS ");
            if(createDatabase(dbName)){
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
    private boolean createDatabase(String dbName) throws SQLException{
        String createDB_string = "CREATE DATABASE "+dbName;
        System.out.println("Trying to create new Database");
        if(connectionObject!=null){
            Statement sqlstatement = connectionObject.createStatement();
            sqlstatement.executeUpdate(createDB_string);
            sqlstatement.close();
            System.out.println("Trying to create new Database SUCCESS");
            return true;
        }
        return false;
    }

private void createPatientsTable(){

String createTablePatientsString = "CREATE TABLE patients("+
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
        

}

private void createHealthRecordTable(){
    String createTableHealthRecordsString = "CREATE TABLE HealthRecords("+
    "recID INT PRIMARY KEY NOT_NULL AUTO_INCREMENT,"+
    "FOREIGN KEY patientID INT NOT NULL,"+
    "datetime TIMESTAMP,"+
    "operation VARCHAR(400),"+
    "responsible VARCHAR(100),"+
    "followUp VARCHAR(150),"+
    ")";
}

}