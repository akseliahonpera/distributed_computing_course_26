package com.group_13;

import javax.swing.SwingUtilities;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Client starting..." );

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController.start();
            }

        });


    }
}
