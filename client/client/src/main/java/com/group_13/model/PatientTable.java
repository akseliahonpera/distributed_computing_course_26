/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group_13.model;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class PatientTable extends AbstractTableModel {

    private List<Patient> patients = new ArrayList<>();

    private final String[] columns = {
            "ID", "First Name", "Last Name"
    };

    public void setPatients(List<Patient> list) {
        patients = list;
        fireTableDataChanged();
    }

    public void updatePatient(Patient updatedPatient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(updatedPatient.getId())) {
                patients.set(i, updatedPatient);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        fireTableDataChanged();
    }

    public Patient getPatient(int row) {
        return patients.get(row);
    }

    public void deletePatient(Patient patient) {
        patients.remove(patient);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return patients.size();
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
        Patient p = patients.get(row);

        return switch (col) {
            case 0 -> p.getId();
            case 1 -> p.getFName();
            case 2 -> p.getLName();
            default -> "";
        };
    }
}