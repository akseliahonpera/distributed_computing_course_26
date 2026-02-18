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


public class Server {
    private static SSLContext myServerSSLContext(String keystorePath, String keystorePw) throws Exception {
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
            configFile = "node2_conf.txt";
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
        try {
            System.out.println("Server address: " + fullAddress);
            System.out.println(serverName + " started...");

            // SSL DISABLED FOR TESTING PURPOSES!!!

            // SSLContext sslContext = myServerSSLContext("keystore.jks", "salasana1");

            // HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);

            /*
             * server.setHttpsConfigurator (new HttpsConfigurator(sslContext)
             * {
             * public void configure (HttpsParameters params) {
             * 
             * //InetSocketAddress remote = params.getClientAddress();
             * SSLContext c = getSSLContext();
             * SSLParameters sslparams = c.getDefaultSSLParameters();
             * 
             * params.setSSLParameters(sslparams);
             * }
             * });
             */

            HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);

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
