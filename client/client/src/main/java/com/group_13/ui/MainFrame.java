package com.group_13.ui;

/**
 *  The main frame of the application. Contains the patient panel and record panel in a split pane.
 *  Listens to patient selection changes in the patient panel and updates the record panel based on that.
 * @author JONIK
 */

// NOTE: This code, along with the rest of the UI code was originally generated using Netbeans GUI builder, altough heavily modified in places.

public class MainFrame extends javax.swing.JFrame {

    private final PatientPanel patientPanel;
    private final RecordPanel recordPanel;
    private final javax.swing.JSplitPane jSplitPane;

    private final int WIDTH = 1200;
    private final int HEIGHT = 800;

    public MainFrame() {
        // Poistin koko netbeansin generoiman initComponentsin

        patientPanel = new PatientPanel();
        recordPanel = new RecordPanel();
        jSplitPane = new javax.swing.JSplitPane();

        setTitle("Health Record System");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(WIDTH, HEIGHT));

        jSplitPane.setDividerLocation(WIDTH / 2);
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
    }
}