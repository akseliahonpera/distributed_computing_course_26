/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group_13.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author JONIK
 */
public class RecordTable extends AbstractTableModel {

    private List<Record> allRecords = new ArrayList<>();
    private List<Record> visibleRecords = new ArrayList<>();

    private final String[] columns = {
        "ID", "Datetime", "Responsible"
    };

    public void setRecords(List<Record> list) {
        allRecords = new ArrayList<>(list);
        visibleRecords = new ArrayList<>(list);
        fireTableDataChanged();
    }
    
    public void deleteRecord(Record record) {
        allRecords.remove(record);
        visibleRecords.remove(record);
        fireTableDataChanged();
    }
    
    public void showRecordsForPatient(String patientId) {

        visibleRecords.clear();

        for (Record r : allRecords) {
            if (r.getPatientID().equals(patientId)) {
                visibleRecords.add(r);
            }
        }

        fireTableDataChanged();
    }

    public Record getRecord(int row) {
        return visibleRecords.get(row);
    }
    
    public List<Record> getAllRecords() {
        return visibleRecords;
    }

    @Override
    public int getRowCount() {
        return visibleRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Record r = visibleRecords.get(row);

        return switch (col) {
            case 0 -> r.getID();
            case 1 -> r.getDatetime();
            case 2 -> r.getResponsible();
            default -> "";
        };
    }
}