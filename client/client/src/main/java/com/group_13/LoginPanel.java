package com.group_13;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

    public LoginPanel() {

        setBackground(Color.BLACK);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridBagLayout);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = .0;
        c.weighty = .0;
        c.fill = GridBagConstraints.BOTH;
        JPanel subJPanel = new JPanel();
        subJPanel.setBackground(Color.PINK);
        add(subJPanel, c);

    
        Font labelFont = new Font("Dialog", Font.BOLD, 17);
        Font textFieldFont = new Font("Dialog", Font.BOLD, 19);
        Dimension textFieldDimension = new Dimension(250, 30);


        JLabel userNameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        userNameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 30));

        JTextField userNameField = new JTextField();
        JTextField passwordField = new JTextField();
        userNameField.setFont(textFieldFont);
        passwordField.setFont(textFieldFont);
        
        userNameField.setPreferredSize(textFieldDimension);
        passwordField.setPreferredSize(textFieldDimension);

        userNameField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        
        GridBagConstraints c2 = new GridBagConstraints();
        subJPanel.setLayout(gridBagLayout);
        c2.insets = new Insets(25, 25, 25, 25);
        c2.fill = GridBagConstraints.BOTH;
        c2.anchor = GridBagConstraints.LINE_START;
        c2.gridx = 0;
        c2.gridy = 0;
        c2.weightx = 0;
        c2.weighty = .5;
        subJPanel.add(userNameLabel, c2);

        c2.gridx = 0;
        c2.gridy = 1;
        subJPanel.add(passwordLabel, c2);

        c2.fill = GridBagConstraints.BOTH;
        c2.anchor = GridBagConstraints.LINE_START;
        c2.weightx = 1;
        c2.weighty = .1;
        c2.gridwidth = 1;
        c2.insets = new Insets(25, 50, 25, 50);
        c2.gridx = 1;
        c2.gridy = 0;
        subJPanel.add(userNameField, c2);
        c2.gridx = 1;
        c2.gridy = 1;
        c2.insets = new Insets(25, 50, 25, 50);
        subJPanel.add(passwordField, c2);

        c2.anchor = GridBagConstraints.LAST_LINE_START;
        c2.fill = GridBagConstraints.NONE;
        c.weighty = 1;
        c2.insets = new Insets(25, 25, 10, 0);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 6;
        subJPanel.add(okButton, c2);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("TEST");
            }
        });



    }




}
