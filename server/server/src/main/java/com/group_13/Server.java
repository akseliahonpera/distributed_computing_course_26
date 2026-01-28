package com.group_13;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Server
{
    private static SSLContext myServerSSLContext(String keystorePath, String keystorePw) throws Exception
    {
        char[] passphrase = keystorePw.toCharArray();

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keystorePath), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ssl;
    }

    public static void main( String[] args ) throws IOException, Exception
    {
        try
        {
            System.out.println("Opening database");
            
            String dbPath = "jdbc:mysql://localhost:3306/";
            String dbName = "DS26";
            String dbuser = "DS26Server";
            String dbPw = "Gambiinakiuas522";

            DataBase testDb = DataBase.getDatabase();
            testDb.open(dbName, dbPath, dbuser, dbPw);


            System.out.println("Starting server!");

            //SSL DISABLED FOR TESTING PURPOSES!!!

            //SSLContext sslContext = myServerSSLContext("keystore.jks", "salasana1");

            //HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);

            /*server.setHttpsConfigurator (new HttpsConfigurator(sslContext) 
            {
                public void configure (HttpsParameters params) {

                    //InetSocketAddress remote = params.getClientAddress();
                    SSLContext c = getSSLContext();
                    SSLParameters sslparams = c.getDefaultSSLParameters();

                    params.setSSLParameters(sslparams);
                }
            });*/

            HttpServer server = HttpServer.create(new InetSocketAddress(8001),0);

            HttpContext serverContext = server.createContext("/api", new RequestHandler());

            //serverContext.setAuthenticator(new UserAuthenticator("api"));

            server.setExecutor(Executors.newCachedThreadPool()); 
            server.start(); 
        } catch (FileNotFoundException e) {
            System.out.println("Certificate not found");

            e.printStackTrace();
        } catch (Exception e) {

            System.out.println(e.getMessage());

            e.printStackTrace();
        }

    }
}
