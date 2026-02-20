
package com.group_13.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ServerChoser extends JDialog {

    private static ServerChoser INSTANCE = null;
    private int serverIndex;

    private ServerChoser() {}

    public void askServer() {
        setMinimumSize(new Dimension(240, 240));
        setTitle("Set Server");
        setLocation(new Point(500, 500));
        setResizable(false);
        setModal(true);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        add(panel);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gridBagLayout);
        String[] choices = { "Oulu", "Tampere", "Helsinki" };
        JComboBox<String> cb = new JComboBox<String>(choices);
        cb.setPreferredSize(new Dimension(100, 25));
        JButton okButton = new JButton();
        okButton.setPreferredSize(new Dimension(50, 25));
        okButton.setText("OK");

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;

        panel.add(cb, c);

        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(25, 0, 0, 0);
        panel.add(okButton, c);

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                serverIndex = (int) cb.getSelectedIndex();
                dispose();
            }

        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static ServerChoser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerChoser();
        }
        return INSTANCE;

    }

    public String getServerAddress() {
        return String.format("http://127.0.0.1:800%d/", serverIndex + 1);
    }

    public static void main(String args[]) {
        ServerChoser.getInstance().askServer();
        System.out.println(ServerChoser.getInstance().getServerAddress());
    }

}