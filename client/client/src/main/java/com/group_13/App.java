package com.group_13;

import javax.swing.SwingUtilities;
import com.group_13.ui.LoginFrame;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Client starting..." );

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }

        });


    }
}
