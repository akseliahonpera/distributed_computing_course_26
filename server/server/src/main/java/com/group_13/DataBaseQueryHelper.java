package com.group_13;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

class DataBaseQueryHelper
{
    static public Set<String> getTableColumns(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        Set<String> columnSet = new TreeSet<>();
        ResultSet rs = meta.getColumns(null, null, tableName, null);
        while (rs.next()) {
            columnSet.add(rs.getString("COLUMN_NAME"));
        }
        return columnSet;
    }

    static public PreparedStatement buildQuery(Connection conn, String tableName, Map<String,String> params) throws SQLException
    {
        Set<String> validColumns = getTableColumns(conn, tableName);
        StringBuilder querySB = new StringBuilder("SELECT * FROM ");

        querySB.append(tableName);
        querySB.append(" WHERE ");

        ArrayList<String> values = new ArrayList<>();
        boolean first = true;
        for (String key : params.keySet()) {
            if (!validColumns.contains(key) ||
                params.get(key).length() == 0) {
                continue;
            }
            if (!first) {
                querySB.append(" AND ");
            }
            querySB.append(key);
            querySB.append(" = ?");
            values.add(params.get(key));
            first = false;
        }

        if (values.size() < 1) {
            querySB = new StringBuilder("SELECT * FROM ");
            querySB.append(tableName);
            return conn.prepareStatement(querySB.toString());
        }

        PreparedStatement stmt = conn.prepareStatement(querySB.toString());

        System.out.println(querySB.toString());

        int index = 1;
        for (String v : values) {
            stmt.setString(index++, v);
        }

        System.out.println(stmt.toString());
        
        return stmt;
    }

    /*Querybuilder for partial field values */
    static public PreparedStatement buildPartialQuery(Connection conn, String tableName, Map<String,String> params) throws SQLException
    {
        Set<String> validColumns = getTableColumns(conn, tableName);
        StringBuilder querySB = new StringBuilder("SELECT * FROM ");

        querySB.append(tableName);
        querySB.append(" WHERE ");

        ArrayList<String> values = new ArrayList<>();
        boolean first = true;
        for (String key : params.keySet()) {
            if (!validColumns.contains(key) ||
                params.get(key).length() == 0) {
                continue;
            }
            if (!first) {
                querySB.append(" AND ");
            }
            querySB.append(key);

            querySB.append(" LIKE ?");
           
            values.add("%" +params.get(key)+"%");
    
            first = false;
        }

        if (values.size() < 1) {
            querySB = new StringBuilder("SELECT * FROM ");
            querySB.append(tableName);
            return conn.prepareStatement(querySB.toString());
        }

        PreparedStatement stmt = conn.prepareStatement(querySB.toString());

        System.out.println(querySB.toString());

        int index = 1;
        for (String v : values) {
            stmt.setString(index++, v);
        }

        System.out.println(stmt.toString());
        
        return stmt;
    }

    static JSONArray queryWithRowId(DataBase db, String tableName, long rowId) throws SQLException, Exception
    {
        TreeMap<String,String> params = new TreeMap<>();

        params.put("id", Long.toString(rowId));

        return query(db, tableName, params);
    }

    static JSONArray query(DataBase db, String tableName, Map<String,String> params) throws SQLException, Exception
    {
        JSONArray jsonArray = new JSONArray();
        //Try matching query
        try (Connection conn = db.getConnection(); PreparedStatement stmt = buildQuery(conn, tableName, params)) {

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
           
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                
                for (int i = 1; i <= columnCount; i++) {
                    obj.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                jsonArray.put(obj);
            }
            
        }
        if (!jsonArray.isEmpty()){
            return jsonArray;
        }else{
             //Go for partial search if matching did not work.
        //Try matching query
        try (Connection conn = db.getConnection(); PreparedStatement stmt = buildPartialQuery(conn, tableName, params)) {

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
           
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                
                for (int i = 1; i <= columnCount; i++) {
                    obj.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                jsonArray.put(obj);
            }
            
        }
        return jsonArray;
    }
    }
    /*https://stackoverflow.com/questions/5902310/how-do-i-validate-a-timestamp
    
    Check if inoutstring is somewhat correct for timestamp for mysql80
    
    THIS SHOULD BE IMPLEMENTED IN FRONTEND!!! SUAATANA
    */
    public static boolean isTimeStampValid(String inputString)
    { 
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(inputString);
            return true;
        } catch(ParseException e) {
            return false;
        }
    }

    static long insert(DataBase db, String tableName, JSONObject object) throws SQLException, Exception
    {
        try (Connection conn = db.getConnection()) {
            Set<String> validColumns = getTableColumns(conn, tableName);

            StringBuilder insertSB = new StringBuilder("INSERT INTO ");
            insertSB.append(tableName);
            
            ArrayList<Object> values = new ArrayList<>();

            boolean first = true;
            for (String key : object.keySet()) {
                if (!validColumns.contains(key) ||
                    key.equalsIgnoreCase("id") || 
                    (object.get(key) instanceof String && object.getString(key).length() == 0)) {
                    continue;
                }
                /* Check if dateofbirth is in correct timeformat and skip dob if not 
                THIS SHOULD BE IMPLEMENTED IN FRONTEND!!! SEMMILÄ!!!! Pallit.. :kuolaava_eemee_emoji:
                */
                if (key.equalsIgnoreCase("dateofbirth")) {
                    String tempTime = object.getString(key);
                    if (!isTimeStampValid(tempTime)) {
                        continue;
                    }
                }
                if (first) {
                    insertSB.append("(");
                    first = false;
                } else {
                    insertSB.append(", ");
                }
                insertSB.append(key);
                values.add(object.get(key));
            }

            if (values.isEmpty()) {
                return -1;
            }

            insertSB.append(") VALUES (");
            insertSB.append(String.join(", ", Collections.nCopies(values.size(), "?")));
            insertSB.append(")");

            System.out.println(insertSB.toString());

            try (PreparedStatement stmt = conn.prepareStatement(insertSB.toString(), Statement.RETURN_GENERATED_KEYS)) {
                int index = 1;
                for (Object v : values) {
                    stmt.setObject(index++, v);
                }
                
                int rows = stmt.executeUpdate();
                if (rows < 1) {
                    return -1;
                }

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }


    
    static void update(DataBase db, String tableName, JSONObject object, long id) throws SQLException, Exception
        /* 
        Generic update method for tables based on primary key "id". 
        Takes jsonObject containing update information for database records.
        Paramaeter id contains the row primary key reference.
        */
    {
        try (Connection conn = db.getConnection()) {
            Set<String> validColumns = getTableColumns(conn, tableName);

            StringBuilder updateSB = new StringBuilder("UPDATE ");
            updateSB.append(tableName);
            ArrayList<Object> values = new ArrayList<>();

            boolean first = true;
            for (String key : object.keySet()) {
                if (!validColumns.contains(key) ||
                    key.equalsIgnoreCase("id") || 
                    (object.get(key) instanceof String && object.getString(key).length() == 0)) {
                    continue;
                }
                if (first) {
                    updateSB.append(" SET ");
                    first = false;
                } else {
                    updateSB.append(", ");
                }
                updateSB.append(key);
                values.add(object.get(key));
                updateSB.append(" = ? ");
            }
            if (values.isEmpty()) {
                return;
            }

            updateSB.append(" WHERE id=?");
            values.add(id);
            System.out.println(updateSB.toString());

            try (PreparedStatement stmt = conn.prepareStatement(updateSB.toString())) {
                int index = 1;
                for (Object v : values) {
                    stmt.setObject(index++, v);
                }
                stmt.execute();
                stmt.close();
            }
        }
    }


      
    static void delete(DataBase db, String tableName, long id) throws SQLException, Exception
        /* 
        Generic delete method for tables based on primary key "id". 
        Paramaeter id contains the row primary key reference.
        */
    {
        try (Connection conn = db.getConnection()) {

            StringBuilder updateSB = new StringBuilder("DELETE FROM ");
            updateSB.append(tableName);

            updateSB.append(" WHERE id= ?");
            System.out.println(updateSB.toString());

            try (PreparedStatement stmt = conn.prepareStatement(updateSB.toString())) {  
                stmt.setObject(1, id);
                stmt.execute();
                stmt.close();
            }
        }
    }

    static void insertChange(DataBase db, String table, String type, long rowId, long epochTimeMs, JSONObject changedRow) throws Exception
    {
       try (Connection conn = db.getConnection()) {
            String query = "INSERT INTO changelog (timestamp, tablename, rowid, type) VALUES (?, ?, ?, ?)";
            long id = -1;
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, epochTimeMs);
                stmt.setString(2, table);
                stmt.setLong(3, rowId);
                stmt.setString(4, type);
                
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }

            if (changedRow == null || id == -1) {
                return;
            } 

            query = "INSERT INTO changes (logid, colname, colvalue) VALUES (?, ?, ?)";

            for (String key : changedRow.keySet()) {
                if (key.equalsIgnoreCase("id")) {
                    //Should not be possible
                    continue;
                }

                String newValue = changedRow.get(key).toString();

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setLong(1, id);
                    stmt.setString(2, key);
                    stmt.setString(3, newValue);
                    stmt.execute();
                }
            }
        }
    }

    static JSONObject getChanges(DataBase db, long logId) throws Exception
    {
        JSONObject obj = new JSONObject();
        try (Connection conn = db.getConnection()) {
            String query = "SELECT colname, colvalue FROM changes WHERE logid = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, logId);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String name = rs.getString(1);
                    String value = rs.getString(2);

                    obj.put(name, value);
                }
            }
        }
        return obj;
    }

    static JSONArray getChangesSince(DataBase db, long epochTimeMs) throws Exception
    {
        JSONArray jsonArray = new JSONArray();

        try (Connection conn = db.getConnection()) {
            String query = "SELECT * FROM changelog WHERE timestamp > ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, epochTimeMs);

                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        obj.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    jsonArray.put(obj);
                }
            }
        }
        return jsonArray;
    }

}