package com.group_13;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;


public class Server {
    static public HttpClient client = null;

    public static SSLContext getSSLContext(String nodeKeystoreP12Path,
                                            String truststoreP12Path,
                                            char[] password){

        // ---- Load node key material (client cert + private key) ----
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {

            e.printStackTrace();
        }
        try (FileInputStream in = new FileInputStream(nodeKeystoreP12Path)) {
            try {
                keyStore.load(in, password);
            } catch (NoSuchAlgorithmException e) {
              System.out.println("nosuchfile");
                e.printStackTrace();
            } catch (CertificateException e) {
                System.out.println("certti faili");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("filua ei löydy");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        KeyManagerFactory kmf = null;
        try {
            kmf = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm()
            );
        } catch (NoSuchAlgorithmException e) {
            
            e.printStackTrace();
        }
        try {
            kmf.init(keyStore, password);
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ---- Load trust material (cluster CA) ----
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (FileInputStream in = new FileInputStream(truststoreP12Path)) {
            trustStore.load(in, password);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            );
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            tmf.init(trustStore);
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ---- Build SSL context for mTLS ----
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sslContext;
    }


    private static void createHttpClient(SSLContext sslContext)
    {
        client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
    }

    private static void initHospitalNetwork(String confFilePath) throws Exception {
        HospitalNetwork.getInstance().loadFromFile(confFilePath);
    }

    private static void createRootUserIfNotExists()
    {
        if (!UserAuthenticator.getInstance().userExists("root")) {
            System.out.println("Root user didn't found! Generating new root user");
            UserAuthenticator.getInstance().addUser("root", "root", 0);
        }
    }

    public static void main(String[] args) throws IOException, Exception {

        String configFile = null;
        int serverID = (args.length == 0) ? 0 : Integer.parseInt(args[0]);

        String os_name = System.getProperty("os.name");

        // Testaan ajaa windows ympäristössä useampaa serveriä erillisissä prosesseissa
        if (os_name.toLowerCase().matches(".*windows.*")) {

            if (serverID < 2) {
                new ProcessBuilder(
                        "cmd", "/c", "start", "Server " + (serverID + 1),
                        "cmd", "/k", "java -jar target\\server-1.0-SNAPSHOT.jar " + (serverID + 1)).start();
            }

            configFile = String.format("node%d_conf.txt", serverID);

        } else {
            // Tässä asetukset servulle jos OS joku muu ku windows.
            configFile = String.format("node%d_conf.txt", serverID);
        }

        initHospitalNetwork(configFile);

        String serverName = HospitalNetwork.getInstance().getLocalNode().getName();
        String fullAddress = HospitalNetwork.getInstance().getLocalNode().getAddress();
        Integer portNumber = Integer.parseInt(fullAddress.split(":")[1]);

        if (os_name.toLowerCase().matches(".*windows.*")) {
            System.out.print("\033]0;" + ("Server: " + fullAddress + " " + serverName) + "\007");
        }

        System.out.println("Current hospital network:");
        System.out.println(HospitalNetwork.getInstance());

        DataBaseManager.getOwnDataBase();
        createRootUserIfNotExists();

        SSLContext sslContext = getSSLContext("certs/" + HospitalNetwork.getInstance().getLocalNode().getName() + ".p12",
                                              "certs/ds26-truststore.p12",
                                              "Gambiinakiuas522".toCharArray());


        createHttpClient(sslContext);

        try {
            System.out.println("Server address: " + fullAddress);
            System.out.println(serverName + " started...");


            HttpsServer server = HttpsServer.create(new InetSocketAddress(portNumber),0);

            server.setHttpsConfigurator(new HttpsConfigurator(sslContext)
            {
                @Override
                public void configure (HttpsParameters params) {
                    SSLParameters sslparams = sslContext.getDefaultSSLParameters();

                    sslparams.setWantClientAuth(true);

                    params.setSSLParameters(sslparams); 
                }
            });

            //HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);

            server.createContext("/api", new RequestHandler());
            server.setExecutor(Executors.newCachedThreadPool());

            ReplicaSync.startSyncThread();

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
