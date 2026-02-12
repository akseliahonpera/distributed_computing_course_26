package com.group_13;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DataBaseTable
{
    private String name = null;
    private TreeMap<String, String> columnTypes = null;

    public DataBaseTable(String tableName) {
        name = tableName;
        columnTypes = new TreeMap<>();
    }

    public void addColumn(String varName, String varType) {
        columnTypes.put(varName, varType);
    }

    public boolean hasColumn(String varName) {
        return columnTypes.containsKey(varName);
    }

    public String getVarType(String varName) {
        return columnTypes.get(varName);
    }

    public ArrayList<String> getVarNames() {
        ArrayList<String> typeArray = new ArrayList<>();
        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
            typeArray.add(entry.getKey());
        }   
        return typeArray;
    }

    public String getName() {
        return name;
    }

    public boolean doesMatch(DataBaseTable other) {
        ArrayList<String> varNames = getVarNames();
        ArrayList<String> varNames2 = other.getVarNames();
        if (varNames.size() != varNames2.size()) {
            return false;
        }
        for (String vn : varNames) {
            if (!other.hasColumn(vn)) {
                return false;
            }
        }
        return true;
    }
}