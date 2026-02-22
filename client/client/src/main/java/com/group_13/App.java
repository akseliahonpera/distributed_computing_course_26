package com.group_13;


import javax.swing.SwingUtilities;

import com.group_13.api.BaseAPI;
import com.group_13.ui.LoginFrame;
import com.group_13.ui.ServerChoser;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Client starting..." );

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerChoser.getInstance().askServer();
                BaseAPI.setBaseURL(ServerChoser.getInstance().getServerAddress());
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }

        });


    }
}
