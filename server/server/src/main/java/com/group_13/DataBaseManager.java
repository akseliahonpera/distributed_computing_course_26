package com.group_13;

public class DataBaseManager
{
    private static DataBase ownDB = null;

    private static DataBaseTable getPatientsTableDefinition()
    {
        DataBaseTable patientsTable = new DataBaseTable("patients");

        patientsTable.addColumn("id",                "INT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        patientsTable.addColumn("socialsecnum",      "VARCHAR(150)");
        patientsTable.addColumn("fname",             "VARCHAR(32)");
        patientsTable.addColumn("lname",             "VARCHAR(32)");
        patientsTable.addColumn("dateofbirth",       "DATE");
        patientsTable.addColumn("address",           "VARCHAR(150)");
        patientsTable.addColumn("phone",             "VARCHAR(150)");
        patientsTable.addColumn("emergency_contact", "VARCHAR(150)");
        patientsTable.addColumn("homehospital",      "VARCHAR(150)");

        return patientsTable;
    }

    private static DataBaseTable getRecordsTableDefinition()
    {
        DataBaseTable recordTable = new DataBaseTable("records");

        recordTable.addColumn("id",                "INT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        recordTable.addColumn("patientid",         "INT NOT NULL");
        recordTable.addColumn("datetime",          "TIMESTAMP");
        recordTable.addColumn("operation",         "VARCHAR(400)");
        recordTable.addColumn("responsible",       "VARCHAR(100)");
        recordTable.addColumn("followup",          "VARCHAR(150)");

        return recordTable;
    }

    private static DataBaseTable getUsersTableDefinition()
    {
        DataBaseTable userTable = new DataBaseTable("users");

        userTable.addColumn("id",                "INT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        userTable.addColumn("username",          "VARCHAR(32) NOT NULL");
        userTable.addColumn("passwordhash",      "VARCHAR(1024)");
        userTable.addColumn("privileges",        "INT");

        return userTable;
    }

    private static DataBase initDataBase(String dbName, String dbPath, String dbUser, String dbPw)
    {
        DataBase db = new DataBase(dbName, dbPath, dbUser, dbPw);

        db.defineTable(getPatientsTableDefinition());
        db.defineTable(getRecordsTableDefinition());
        db.defineTable(getUsersTableDefinition());

        return db;
    }

    public static synchronized DataBase getOwnedDataBase() {
        if (ownDB == null) {
            String dbPath = "jdbc:mysql://localhost:3306/";
            String dbName = "DS26Local2";
            String dbUser = "DS26Server";
            String dbPw = "Gambiinakiuas522";

            ownDB = initDataBase(dbName, dbPath, dbUser, dbPw);
        }
        return ownDB;
    }

    public static synchronized DataBase getReplicaDataBase(int nodeID) {
        return null;
    }
};