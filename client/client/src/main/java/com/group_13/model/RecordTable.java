/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group_13.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import com.group_13.model.Record;

/**
 * A table model for displaying medical record information in a JTable.
 * @author JONIK
 */

public class RecordTable extends AbstractTableModel {

    private List<Record> records = new ArrayList<>();

    private final String[] columns = {
            "ID", "Datetime", "Responsible"
    };

    public void setRecords(List<Record> list) {
        records = new ArrayList<>(list);
        fireTableDataChanged();
    }

    public void deleteRecord(Record record) {
        records.remove(record);
        fireTableDataChanged();
    }

    public void updateRecord(Record updatedRecord) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId().equals(updatedRecord.getId())) {
                records.set(i, updatedRecord);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
    }

    public void addRecord(Record record) {
        records.add(record);
        fireTableDataChanged();
    }

    public Record getRecord(int row) {
        return records.get(row);
    }

    public List<Record> getAllRecords() {
        return records;
    }

    @Override
    public int getRowCount() {
        return records.size();
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
        Record r = records.get(row);

        return switch (col) {
            case 0 -> r.getId();
            case 1 -> r.getDatetime();
            case 2 -> r.getResponsible();
            default -> "";
        };
    }
}