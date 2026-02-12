package com.group_13;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseNonStatic{

    // Class attributes instantiation
    Connection connectionObject;
    private String dbName;

    public DataBaseNonStatic(Connection connObject, String dbName){
        this.connectionObject = connObject;
        this.dbName = dbName;
    }
    
    // Method for opening the database
    public void open(String dbName, String dbPath, String dbUser, String dbPw) throws SQLException{
        if(dbName == null||dbName.isBlank() 
        || dbPath == null||dbPath.isBlank()){
            throw new InvalidParameterException();
        }
        this.dbName = dbName;
        String dbSpace = "USE "+this.dbName;
        if(databaseExists(dbName, dbPath, dbUser, dbPw)){
            System.out.println("Found previous database.");
            if(!patientsTableExists(dbSpace)){
                createPatientsTable(dbSpace);
            }
            if(!recordTableExists(dbSpace)){
                createHealthRecordTable(dbSpace);
            }if(!userTableExists(dbSpace)){
                createUserTable(dbSpace);
            }

        }else{
            initDatabase(dbName, dbPath, dbUser, dbPw);
            //create tables with database
            if (createPatientsTable(dbSpace)){
                System.out.println("Patientstable created.");
            }else{
                System.out.println("Patientstable creation failed.");
            }
            if (createHealthRecordTable(dbSpace)){
                System.out.println("Records table created.");
            }else{
                System.out.println("records creation failed.");
            }
            if (createUserTable(dbSpace)){
                System.out.println("Users table created.");
            }else{
                System.out.println("Users creation failed.");
            }
        }
    }
 //check if pat exists
    private boolean patientsTableExists(String dbSpace){
        System.out.println("Check if patient table exists");
        try{
            String patientsTableName = "patients"; 
            ResultSet resultSet = connectionObject.getMetaData().getTables(null, null, patientsTableName, null);
            if(resultSet.next()){
                System.out.println("Check if patient table exists True");
                return true;
            }
            else{
                System.out.println("Check if patient table exists FALSE");
                return false;

            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
     //check if user record exists
    private boolean recordTableExists(String dbSpace){
        System.out.println("Check if recordtable exists");
        try{
            String recordsTableName = "HealthRecords"; 
            ResultSet resultSet = connectionObject.getMetaData().getTables(null, null, recordsTableName, null);
            if(resultSet.next()){
                System.out.println("Check if patient table exists True");
                return true;
            }
                    else{
                        System.out.println("Check if recordtable exists false");
                        return false;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
    //check if user table exists
    private boolean userTableExists(String dbSpace){
        System.out.println("Check if user table exists");
        try{
            String userTableName = "UserCredentials"; 
            ResultSet resultSet = connectionObject.getMetaData().getTables(null, null, userTableName, null);
            if(resultSet.next()){
                System.out.println("Check if usertable exists True");
                return true;
            }
                    else{
                        System.out.println("Check if usertable exists false");
                        return false;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return false;
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
        System.out.println(" ###### Connection failed ######");
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
                return true;
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
        String createDB_string = "CREATE DATABASE IF NOT EXISTS "+dbName;
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

private boolean createPatientsTable(String dbSpace) throws SQLException{
    System.out.println("Attempting to create patients table");
    String createTablePatientsString = "CREATE TABLE IF NOT EXISTS patients("+
        "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
        "socialSecNum VARCHAR(150),"+
        "fname VARCHAR(150) NOT NULL,"+ // use temporary if needed
        "lname VARCHAR(150),"+
        "dateofbirth DATE,"+
        "address VARCHAR(150),"+
        "phone VARCHAR(150),"+
        "emergency_contact VARCHAR(150),"+
        "homehospital VARCHAR(150)"+
        ")";
            if(connectionObject!=null){
                Statement sqlStatement = connectionObject.createStatement();
                sqlStatement.execute(dbSpace);
                sqlStatement.executeUpdate(createTablePatientsString);
                sqlStatement.close();
                return true;
            }
            return false;

}

private boolean createHealthRecordTable(String dbSpace) throws SQLException{
    System.out.println("Attempting to create healthrecords table");
    String createTableHealthRecordsString = "CREATE TABLE IF NOT EXISTS HealthRecords("+
    "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
    "patientid INT NOT NULL,"+
    "datetime TIMESTAMP,"+
    "operation VARCHAR(400),"+
    "responsible VARCHAR(100),"+
    "followup VARCHAR(150)"+
    ")";
    if(connectionObject!=null){
        Statement sqlStatement = connectionObject.createStatement();
        sqlStatement.execute(dbSpace); 
        sqlStatement.executeUpdate(createTableHealthRecordsString);
        sqlStatement.close();
        return true;
    }
    return false;
}


private boolean createUserTable(String dbSpace) throws SQLException{
    System.out.println("Attempting to create users table");
    String createUserTableString = "CREATE TABLE IF NOT EXISTS UserCredentials("+
    "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
    "username VARCHAR(32) NOT NULL,"+
    "password VARCHAR(32),"+
    "privileges INT"+
    ")";
    if(connectionObject!=null){
        Statement sqlStatement = connectionObject.createStatement();
        sqlStatement.execute(dbSpace); 
        sqlStatement.executeUpdate(createUserTableString);
        sqlStatement.close();
        return true;
    }
    return false;
}




}