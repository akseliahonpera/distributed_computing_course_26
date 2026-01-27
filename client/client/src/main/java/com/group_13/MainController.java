package com.group_13;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.*;




public class MainController {

    private static MainController INSTANCE = null;
    private JFrame mainWindow = null;
    

    // Private Constructor
    private MainController() {
        mainWindow = new JFrame();
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainWindow.setTitle("Client-Proto");
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        mainWindow.setLayout(gridBagLayout);
        LoginPanel loginPanel = new LoginPanel();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        mainWindow.add(loginPanel, c);
        mainWindow.setPreferredSize(new Dimension(1080, 1080));
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);

    }

    // Start GUI
    public static void start() {
        if (INSTANCE == null) {
            INSTANCE = new MainController();
        }
    }





}