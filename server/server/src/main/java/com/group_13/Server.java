package com.group_13;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

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

    private static void initHospitalNetwork(String confFilePath) throws Exception {
        HospitalNetwork.getInstance().loadFromString(Files.readString(Path.of(confFilePath)));
    }

    public static void main( String[] args ) throws IOException, Exception
    {
        initHospitalNetwork("node2_conf.txt");

        System.out.println(HospitalNetwork.getInstance());

        DataBaseManager.getOwnDataBase();
        
        try
        {
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
        

            HttpServer server = HttpServer.create(new InetSocketAddress(6969),0);

            server.createContext("/api", new RequestHandler());
            server.setExecutor(Executors.newCachedThreadPool()); 
            server.start(); 
        } catch (FileNotFoundException e) {
            System.out.println("Certificate not found");

            e.printStackTrace();
        } catch (IOException e) {

            System.out.println(e.getMessage());

            e.printStackTrace();
        }

    }
}
