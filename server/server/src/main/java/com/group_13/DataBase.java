package com.group_13;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataBase {
    private HikariDataSource dataSource = null;
    private String dbName = null;

    public DataBase(String dbName, String dbPath, String dbUser, String dbPw) {
        try {
            this.dbName = dbName;

            dataSource = createDataSource(dbPath, dbName, dbUser, dbPw);
            if (dataSource == null) {
                System.out.println("Failed to connect database!!");
                return;
            }
            if (!databaseExists(dbName)) {
                System.out.println("Database not found! Creating new DB");
                createDatabase(dbName);
            } else {
                System.out.println("Database found!");
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to open database!");
        }
    }

    public Connection getConnection() throws Exception
    {
        Connection conn = dataSource.getConnection();
        conn.setCatalog(dbName);
        return conn;
    }

    private static HikariDataSource createDataSource(String dbPath, String dbName, String dbUser, String dbPw)
    {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbPath + dbName);
            config.setUsername(dbUser);
            config.setPassword(dbPw);

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000); // 30s
            config.setIdleTimeout(600000);      // 10 min
            config.setMaxLifetime(1800000);     // 30 min

            return new HikariDataSource(config);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private boolean databaseExists(String dbName) throws Exception {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while (rs.next()) {
                String catalog = rs.getString(1);
                if (dbName.equalsIgnoreCase(catalog)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private void createDatabase(String dbName) {
        System.out.println("Creating database: " + dbName);
        String createDB_string = "CREATE DATABASE " + dbName;
        try (Connection conn = getConnection(); Statement sqlstatement = conn.createStatement()) {
            sqlstatement.execute(createDB_string);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Database creation failed");
        }
    }

    private boolean tableColumnsMatches(DataBaseTable table) {
        try (Connection conn = getConnection()) {
            DataBaseTable table2 = new DataBaseTable(table.getName());
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet rs = meta.getColumns(dbName, null, table.getName(), null)) {
                while (rs.next()) {
                    table2.addColumn(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return table.doesMatch(table2);
        } catch (Exception e) {
            return false;
        }
    }

    private void createTable(DataBaseTable table) {
        System.out.println("Creating table: " + table.getName());

        try (Connection conn = getConnection(); Statement sqlStatement = conn.createStatement()) {
            StringBuilder sb = new StringBuilder();

            sb.append("CREATE TABLE ");
            sb.append(table.getName());
            sb.append(" (");

            boolean first = true;
            for (String column : table.getVarNames()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(column);
                sb.append(" ");
                sb.append(table.getVarType(column));
                first = false;
            }
            sb.append(")");
            sqlStatement.execute(sb.toString());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Table creation failed!");
        }
    }

    private boolean tableExists(String tableName) {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(dbName, null, tableName, null)) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void dropTable(String tableName) {
        System.out.println("Dropping table: " + tableName);
        try (Connection conn = getConnection(); 
             Statement sqlStatement = conn.createStatement()) {
            sqlStatement.execute("DROP TABLE `" + dbName + "`.`" + tableName + "`");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Drop failed!");
        }
    }

    public void defineTable(DataBaseTable table) {
        if (!tableExists(table.getName())) {
            createTable(table);
        }
        if (!tableColumnsMatches(table)) {
            System.out.println("WARNING!!!!");
            System.out.println("Table columns doesn't match definition!");
            dropTable(table.getName());
            createTable(table);
        } else {
            System.out.println("Table " + table.getName() + " Ok!");
        }
    }
}