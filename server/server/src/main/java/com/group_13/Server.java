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
                                            char[] password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException{

        // ---- Load node key material (client cert + private key) ----
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream keyStoreFile = new FileInputStream(nodeKeystoreP12Path);
             FileInputStream trustStorefile = new FileInputStream(truststoreP12Path)) {
            keyStore.load(keyStoreFile, password);
            trustStore.load(trustStorefile, password);
        } catch (Exception e) {
            System.out.println("Cannot read keystores. Check that cert folder is contains correct keystore files!");
            return null;
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        kmf.init(keyStore, password);
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

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
        System.out.println("arg1"+args[0]);
        String configFile = null;
        int serverID = (args.length == 0) ? 0 : Integer.parseInt(args[0]);
      

        configFile = String.format("node%d_conf.txt", serverID);
        
        initHospitalNetwork(configFile);

        String serverName = HospitalNetwork.getInstance().getLocalNode().getName();
        String fullAddress = HospitalNetwork.getInstance().getLocalNode().getAddress();
        Integer portNumber = Integer.parseInt(fullAddress.split(":")[1]);

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

            //ReplicaSync.startSyncThread();//handle sync in separate node

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
