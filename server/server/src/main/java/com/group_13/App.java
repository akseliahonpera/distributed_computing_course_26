package com.group_13;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "Hello World! ||||||||||||||||||||||||||||||||||||||||||||||||||||||||" );
        int i = 0;
        String dbPath = "jdbc:mysql://localhost:3306/";
        String dbName = "DS26";
        String dbuser = "root";
        String dbPw = "Gambiinakiuas522";


        try{   
            while(i<3){
                System.out.println("Testing database class initiation."); 
                DataBase testDb = DataBase.getDatabase();

                System.out.println("Testing opening database."); 
                testDb.open(dbName, dbPath, dbuser, dbPw);
                Thread.sleep(2000);
                i++;
            } 
        }catch(Exception e){
        e.printStackTrace();
        System.out.println("database test failed");
        }
    }
}