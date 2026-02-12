package com.group_13;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
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

        if (params.keySet().size() < 0) {
            return conn.prepareStatement(querySB.toString());
        }
        querySB.append(" WHERE ");
        boolean first = true;
        for (String key : params.keySet()) {
            if (!validColumns.contains(key)) {
                continue;
            }

            if (!first) {
                querySB.append(" AND ");
            }
            querySB.append(key);
            querySB.append(" = ?");

            first = false;
        }
        PreparedStatement stmt = conn.prepareStatement(querySB.toString());

        System.out.println(querySB.toString());

        int index = 1;
        for (String key : params.keySet()) {
            stmt.setString(index++, params.get(key));
        }

        System.out.println(stmt.toString());
        
        return stmt;
    }


    static JSONArray query(DataBase db, String tableName, Map<String,String> params) throws SQLException
    {
        ResultSet rs = buildQuery(db.getConnection(), tableName, params).executeQuery();

        ResultSetMetaData meta = rs.getMetaData();

        JSONArray jsonArray = new JSONArray();

        int columnCount = meta.getColumnCount();
        while (rs.next()) {
            JSONObject obj = new JSONObject();

            for (int i = 1; i <= columnCount; i++) {
                obj.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    static void insert(DataBase db, String tableName, JSONObject object) throws SQLException
    {


     
        System.out.println(object.toString());
  
        Set<String> validColumns = getTableColumns(db.getConnection(), tableName);

        StringBuilder insertSB = new StringBuilder("INSERT INTO ");
        insertSB.append(tableName);
        
        ArrayList<Object> values = new ArrayList<>();

        boolean first = true;
        for (String key : object.keySet()) {
            if (!validColumns.contains(key)) {
                continue;
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
            return;
        }

        insertSB.append(") VALUES (");
        insertSB.append(String.join(", ", Collections.nCopies(values.size(), "?")));
        insertSB.append(")");

        System.out.println(insertSB.toString());

        PreparedStatement stmt = db.getConnection().prepareStatement(insertSB.toString());
        int index = 1;
        for (Object v : values) {
            stmt.setObject(index++, v);
        }

        stmt.execute();
    }


    
 static void update(DataBase db, String tableName, JSONObject object, int id) throws SQLException, Exception
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
                if (!validColumns.contains(key)) {
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


      
 static void delete(DataBase db, String tableName, int id) throws SQLException, Exception
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

}