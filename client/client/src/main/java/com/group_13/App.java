package com.group_13;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.group_13.api.BaseAPI;
import com.group_13.ui.LoginFrame;
import com.group_13.ui.ServerChooser;

public class App {
    public static void main(String[] args) {
        System.out.println("Client starting...");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerChooser serverChooser = new ServerChooser();
                serverChooser.askServer();
                String serverAddress = serverChooser.getServerAddress();
                if (serverAddress == null) {
                    JOptionPane.showMessageDialog(null,
                            "No server selected. Aborting.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                BaseAPI.setBaseURL(serverAddress);
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }

        });

    }
}
