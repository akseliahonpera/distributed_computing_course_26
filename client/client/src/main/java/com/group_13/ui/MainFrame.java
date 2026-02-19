/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.group_13.ui;

/**
 *
 * @author JONIK
 */
public class MainFrame extends javax.swing.JFrame {

    private final PatientPanel patientPanel;
    private final RecordPanel recordPanel;
    private final javax.swing.JSplitPane jSplitPane;

    public MainFrame() {
        // Poistin koko netbeansin generoiman initComponentsin

        patientPanel = PatientPanel.getInstance();
        recordPanel = RecordPanel.getInstance();
        jSplitPane = new javax.swing.JSplitPane();

        setTitle("Health Record System");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1024, 768));

        jSplitPane.setDividerLocation(508);
        jSplitPane.setDividerSize(6);
        jSplitPane.setResizeWeight(0.5);
        jSplitPane.setLeftComponent(patientPanel);
        jSplitPane.setRightComponent(recordPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );

        // Pass the selected patient to the record panel 
        patientPanel.setPatientSelectionListener(patient -> {
            recordPanel.showRecordsForPatient(patient);
            recordPanel.clearSelection();
        });

        pack();
    }
}