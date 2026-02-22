package com.group_13;

import java.util.TreeMap;

public class DataBaseManager
{
    private static final TreeMap<Integer, DataBase> DBs = new TreeMap<>();

    private static DataBaseTable getPatientsTableDefinition()
    {
        DataBaseTable patientsTable = new DataBaseTable("patients");

        patientsTable.addColumn("id",                "BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        patientsTable.addColumn("socialsecnum",      "VARCHAR(150)");
        patientsTable.addColumn("fname",             "VARCHAR(32)");
        patientsTable.addColumn("lname",             "VARCHAR(32)");
        patientsTable.addColumn("dateofbirth",       "date DEFAULT NULL");
        patientsTable.addColumn("address",           "VARCHAR(150)");
        patientsTable.addColumn("phone",             "VARCHAR(150)");
        patientsTable.addColumn("emergencycontact",  "VARCHAR(150)");
        patientsTable.addColumn("homehospital",      "VARCHAR(150)");
        
        return patientsTable;
    }

    private static DataBaseTable getRecordsTableDefinition()
    {
        DataBaseTable recordTable = new DataBaseTable("records");

        recordTable.addColumn("id",                "BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        recordTable.addColumn("patientid",         "BIGINT");
        recordTable.addColumn("datetime",          "DATETIME DEFAULT CURRENT_TIMESTAMP");
        recordTable.addColumn("operation",         "VARCHAR(400)");
        recordTable.addColumn("responsible",       "VARCHAR(100)");
        recordTable.addColumn("followup",          "VARCHAR(150)");

        return recordTable;
    }

    private static DataBaseTable getUsersTableDefinition()
    {
        DataBaseTable userTable = new DataBaseTable("users");

        userTable.addColumn("id",                "BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        userTable.addColumn("username",          "VARCHAR(32) NOT NULL");
        userTable.addColumn("passwordhash",      "VARCHAR(1024)");
        userTable.addColumn("privileges",        "INT");

        return userTable;
    }

    private static DataBaseTable getChangeLogTableDefinition()
    {
        DataBaseTable clTable = new DataBaseTable("changelog");

        clTable.addColumn("id",          "BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        clTable.addColumn("timestamp",   "BIGINT");   //This contains timestamp of when modification was made
        clTable.addColumn("tablename",   "CHAR(32)"); //This contains which table was modified
        clTable.addColumn("rowid",       "BIGINT");   //This contains which row was modified
        clTable.addColumn("type",        "CHAR(32)"); //This contains operation type ("UPDATE", "INSERT", "DELETE")

        return clTable;
    }

    private static DataBaseTable getChangesTableDefinition()
    {
        DataBaseTable clTable = new DataBaseTable("changes");

        clTable.addColumn("id",          "BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT");
        clTable.addColumn("logid",       "BIGINT");         //This contains which change log row these changes are
        clTable.addColumn("colname",     "CHAR(32)");       //This contains which columns name of changed value
        clTable.addColumn("colvalue",    "VARCHAR(400)");   //This contains which new value for change

        return clTable;
    }

    private static String getDataBaseName(HospitalNode node)
    {
        int localNodeId = HospitalNetwork.getInstance().getLocalNode().getId();

        StringBuilder sb = new StringBuilder();
        sb.append("DS26_");
        sb.append(HospitalNetwork.getInstance().getLocalNode().getName());
        sb.append("_");
        if (node.getId() == localNodeId) {
            sb.append("local");
        } else {
            sb.append("replica_");
            sb.append(node.getId());
        }
        return sb.toString();
    }

    private static DataBase initDataBase(String dbName, String dbPath, String dbUser, String dbPw, boolean isReplica, long startId)
    {
        DataBase db = new DataBase(dbName, dbPath, dbUser, dbPw);

        db.defineTable(getPatientsTableDefinition(), startId);
        db.defineTable(getRecordsTableDefinition(), startId);
        db.defineTable(getUsersTableDefinition(), startId);

        if (!isReplica) {
            //Changelog is maintained for syncronization
            //changelog table contains
            //  - operation type (INSERT, UPDATE, DELETE)
            //  - target table name
            //  - row which was affected on target table
            //  - timestamp when operation was made

            //changes table contains column name - value pairs for each changelog entry
            
            //Other nodes query these table through API
            //They can replay all changes to syncronize replica database
            db.defineTable(getChangeLogTableDefinition(), 0);
            db.defineTable(getChangesTableDefinition(), 0);
        }

        return db;
    }

    public static synchronized DataBase getOwnDataBase() {
        return getDataBase(HospitalNetwork.getInstance().getLocalNode());
    }

    public static synchronized DataBase getDataBase(HospitalNode node) {
        DataBase db = DBs.get(node.getId());
        if (db == null) {
            boolean isReplica = (node.getId() != HospitalNetwork.getInstance().getLocalNode().getId());

            String dbPath = "jdbc:mysql://localhost:3306/";
            String dbName = getDataBaseName(node);
            //String dbName = "ds26";
            String dbUser = "root";
            String dbPw = "Gambiinakiuas522";


            //Owner of objects are encoded to id top bits
            //This makes all IDs globally unique
            //And makes possible to determine owner efficiently in API
            long startId = node.getId();
            startId <<= 48;

            db = initDataBase(dbName, dbPath, dbUser, dbPw, isReplica, startId);

            DBs.put(node.getId(), db);
        }

        return db;
    }
};