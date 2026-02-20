package com.group_13;


import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler
{
    public RequestHandler() {

    }

    private void forwardRequest(HospitalNode node, HttpExchange t, Map<String, String> query, String table, String method)
    {
        //TODO. Implement request forwarding
        //All writes (DELETE, UPDATE, INSERT) needs to go through owner
        //Requests could use same API for simplicity
        System.out.println("Forwarding " + method + " request for table " + table);
    }

    private void storeDataBaseChange(String table, String type, long rowId) throws Exception
    {
        DataBase localDB = DataBaseManager.getOwnDataBase();
        long epochTimeMs = Instant.now().toEpochMilli();

        DataBaseQueryHelper.insertChange(localDB, table, type, rowId, epochTimeMs);
    }

    private void handleSyncRequest(HttpExchange t, Map<String, String> query) throws Exception
    {
        DataBase localDB = DataBaseManager.getOwnDataBase();

        long epochTimeMs = Long.parseLong(query.get("since"));
        
        JSONArray changes = DataBaseQueryHelper.getChangesSince(localDB, epochTimeMs);

        ServerUtility.sendResponse(t, changes.toString(), ServerUtility.HttpStatus.OK);
    }


    public void handlePostRequest(HttpExchange t, Map<String, String> query, String table) throws Exception
    {
        System.out.println("Handling POST request for table " + table + " with query: " + query);
        String bodyText = ServerUtility.GetBodyText(t);

        JSONObject object = new JSONObject(bodyText);

        // kommasin tämän pois koska luodut recordit jäi tähän jumiin ja koko paska jääty -Joni
        // Patienttien kanssa ei ole ongelmaa koska niillä ei ole patientId:tä luotaessa, mutta recordeilla on.

        // unohdin lisätä tarkistuksen kenelle patient kuuluu
        // Jäätyminen johtu siitä, että jokainen recordi joka sisälti viittauksen patienttiin yritettiin ohjata toiselle sairaalanodelle
        // ohjausta ei ollut implementoitu -> serveri ei ikinä lähetä http requestin vastausta -> clientti jää odottamaan
        // -Ara
        if (object.has("patientid")) {
            long id = object.getLong("patientid");
            if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
                forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, query, table, "POST");
                return;
            }
        }

        long newRowId = DataBaseQueryHelper.insert(DataBaseManager.getOwnDataBase(), table, object);

        storeDataBaseChange(table, "INSERT", newRowId);

        ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.OK);
    }

    public void handleGetRequest(HttpExchange t, Map<String, String> query, String table) throws Exception
    {
        System.out.println("Handling GET request for table " + table + " with query: " + query);

        JSONArray allResults = new JSONArray();
        ArrayList<HospitalNode> nodes = HospitalNetwork.getInstance().getAllNodes();
        for (HospitalNode n : nodes) {
            JSONArray results = DataBaseQueryHelper.query(DataBaseManager.getDataBase(n), table, query);
            for (int i = 0; i < results.length(); i++) {
                allResults.put(results.get(i));
            }
        }
        ServerUtility.sendResponse(t, allResults.toString(), ServerUtility.HttpStatus.OK);
    }

    public void handleDeleteRequest(HttpExchange t, Map<String, String> query, String table) throws  Exception
    {
        System.out.println("Handling DELETE request for table " + table + " with query: " + query);
        long id = Long.parseLong(query.get("id"));

        if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
            forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, query, table, "DELETE");
            return;
        }

        DataBaseQueryHelper.delete(DataBaseManager.getOwnDataBase(), table, id);

        storeDataBaseChange(table, "DELETE", id);

        ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.OK);
    }

    public void handleUpdateRequest(HttpExchange t, Map<String, String> query, String table) throws Exception
    {
        System.out.println("Handling UPDATE request for table " + table + " with query: " + query);
        JSONObject object = new JSONObject(ServerUtility.GetBodyText(t));

        long id = Long.parseLong(query.get("id"));

        if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
            forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, query, table, "UPDATE");
            return;
        }

        DataBaseQueryHelper.update(DataBaseManager.getOwnDataBase(), table, object, id);

        storeDataBaseChange(table, "UPDATE", id);

        ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.OK);
    }


    private void HandleAuthRequest(HttpExchange t) throws IOException {
        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
        }
        
        String credentialsJSON = ServerUtility.GetBodyText(t);
        JSONObject object = new JSONObject(credentialsJSON);

        String username = object.getString("user");
        String password = object.getString("password");
        
        if (UserAuthenticator.getInstance().isValidUser(username, password)) {
            Token token = TokenValidator.getInstance().newToken();

            JSONObject responseJSON = new JSONObject();
            responseJSON.put("token", token.getTokenStr());
            responseJSON.put("expiration", token.getExpirationTime());

            ServerUtility.sendResponse(t, responseJSON.toString(), ServerUtility.HttpStatus.OK);
        } else {
            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void handle(HttpExchange t) throws IOException {
        System.out.println("Handling");
        try 
        {
            URI uri = t.getRequestURI();
            Map<String, String> query = ServerUtility.parseQuery(t);
            System.out.println(uri.toString());

            if (uri.getPath().equals("/api/auth/token")) {
                HandleAuthRequest(t);
                return;
            }

            String token = ServerUtility.extractBearerToken(t);
            if (token == null || !TokenValidator.getInstance().isValidTokenStr(token)) {
                ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
                System.out.println("Unauthorized. No valid token!");
                if (token != null) {
                    System.out.println("Token: " + token);
                }
                return;
            }

            String table;
            switch (uri.getPath()) {
                case "/api/patients" -> table = "patients";
                case "/api/records" -> table = "records";
                case "/api/sync" -> table = "sync";
                default -> {
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.NOT_FOUND);
                    return;
                }
            }

            String method = t.getRequestMethod();

            if (table.equals("sync")) {
                if (method.equalsIgnoreCase("GET")) {
                    handleSyncRequest(t, query);
                } else {
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.FORBIDDEN);
                }
                return;
            }
            if (method.equalsIgnoreCase("POST")) {
                handlePostRequest(t, query, table);
            } else if (method.equalsIgnoreCase("GET")) {
                handleGetRequest(t, query, table);
            } else if (method.equalsIgnoreCase("DELETE")) {
                handleDeleteRequest(t, query, table);
            } else if (method.equalsIgnoreCase("PUT")) {
                handleUpdateRequest(t, query, table);
            } else {
                ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}