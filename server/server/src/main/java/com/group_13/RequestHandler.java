package com.group_13;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;

public class RequestHandler implements HttpHandler
{
    public RequestHandler() {

    }

    private void forwardRequest(HospitalNode node, HttpExchange t, String bodyText, Map<String, String> query, String table, String method) throws Exception
    {
        //When API receives database write operation for object it doesn't own, it gets forwarded to owner node
        //This function works as proxy

        System.out.println("Forwarding " + method + " request for table " + table);

        String fullUrl = "https://" + node.getAddress() + "/api/" + table + (query.isEmpty() ? "" : "?" + ServerUtility.encodeParams(query));

        HttpRequest request;
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(fullUrl));

        if (method.equalsIgnoreCase("INSERT")) {
            request = builder.POST(HttpRequest.BodyPublishers.ofString(bodyText)).build();
        } else if (method.equalsIgnoreCase("UPDATE")) {
            request = builder.PUT(HttpRequest.BodyPublishers.ofString(bodyText)).build();
        } else if (method.equalsIgnoreCase("DELETE")) {
            request = builder.DELETE().build();
        } else {
            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        HttpResponse<String> response = Server.client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        ServerUtility.sendResponse(t, response.body(), response.statusCode());
    }

    private void storeDataBaseChange(String table, String type, long rowId, JSONObject newValues, JSONObject oldValues) throws Exception
    {
        DataBase localDB = DataBaseManager.getOwnDataBase();
        long epochTimeMs = Instant.now().toEpochMilli();

        JSONObject diff = newValues;
        if (oldValues != null) {
            //Change is UPDATE
            //Lets find what column values have been changed
            //Only changed values are stored
            diff = new JSONObject();
            for (String key : newValues.keySet()) {
                if (oldValues.has(key) && 
                    newValues.get(key).toString().equals(oldValues.get(key).toString())) {
                    continue;
                }
                diff.put(key, newValues.get(key));
            }
        }

        DataBaseQueryHelper.insertChange(localDB, table, type, rowId, epochTimeMs, diff);
    }

    private void handleSyncRequest(HttpExchange t, Map<String, String> query) throws Exception
    {
        DataBase localDB = DataBaseManager.getOwnDataBase();

        if (query.containsKey("logid")) {
            long logId = Long.parseLong(query.get("logid"));

            JSONObject row = DataBaseQueryHelper.getChanges(localDB, logId);

            ServerUtility.sendResponse(t, row.toString(), ServerUtility.HttpStatus.OK);
        } else {
            long afterId = Long.parseLong(query.get("since"));
            
            JSONArray changes = DataBaseQueryHelper.getChangesSince(localDB, afterId);

            ServerUtility.sendResponse(t, changes.toString(), ServerUtility.HttpStatus.OK);
        }
    }


    public void handlePostRequest(HttpExchange t, Map<String, String> query, String table) throws Exception
    {
        System.out.println("Handling POST request for table " + table + " with query: " + query);
        String bodyText = ServerUtility.GetBodyText(t);

        JSONObject object = new JSONObject(bodyText);

        //Records are supposed to owned by same node which owns Patient
        if (object.has("patientid")) {
            //Check if we own Patient
            //If we don't, forward request to owner node
            long id = object.getLong("patientid");
            if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
                forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, bodyText, query, table, "INSERT");
                return;
            }
        }

        long newRowId = DataBaseQueryHelper.insert(DataBaseManager.getOwnDataBase(), table, object);

        JSONArray newRowJson = DataBaseQueryHelper.queryWithRowId(DataBaseManager.getOwnDataBase(), table, newRowId);

        //Store this insert to changelog
        storeDataBaseChange(table, "INSERT", newRowId, newRowJson.getJSONObject(0), null);

        ServerUtility.sendResponse(t, newRowJson.toString(), ServerUtility.HttpStatus.OK);
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

        //Check if we own object/row which clients want to delete
        //If we don't, forward request to owner node
        if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
            forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, null, query, table, "DELETE");
            return;
        }

        DataBaseQueryHelper.delete(DataBaseManager.getOwnDataBase(), table, id);

        //Store this delete operation to changelog
        storeDataBaseChange(table, "DELETE", id, null, null);

        ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.OK);
    }

    public void handleUpdateRequest(HttpExchange t, Map<String, String> query, String table) throws Exception
    {
        System.out.println("Handling UPDATE request for table " + table + " with query: " + query);
        String bodyText = ServerUtility.GetBodyText(t);
        JSONObject object = new JSONObject(bodyText);

        long id = Long.parseLong(query.get("id"));

        //Check if we own object/row which clients want to update
        //If we don't, forward request to owner node
        if (HospitalNetwork.getInstance().getNodeByRowId(id).isReplica()) {
            forwardRequest(HospitalNetwork.getInstance().getNodeByRowId(id), t, bodyText, query, table, "UPDATE");
            return;
        }

        JSONArray oldRowJson = DataBaseQueryHelper.queryWithRowId(DataBaseManager.getOwnDataBase(), table, id);

        DataBaseQueryHelper.update(DataBaseManager.getOwnDataBase(), table, object, id);

        JSONArray newRowJson = DataBaseQueryHelper.queryWithRowId(DataBaseManager.getOwnDataBase(), table, id);

        //Store this update to changelog
        //Old row is needed to check which column values gets affected
        storeDataBaseChange(table, "UPDATE", id, newRowJson.getJSONObject(0), oldRowJson.getJSONObject(0));

        ServerUtility.sendResponse(t, newRowJson.toString(), ServerUtility.HttpStatus.OK);
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
            //No token for this client
            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
        }
    }

    boolean checkAuthorization(HttpExchange t) throws Exception
    {
        //Mutual TLS is used for node-node communication
        //Normal TLS + bearer authorization is used for client-node communication
        //Bearer token is issued by /api/auth/token endpoint when credentials are valid (username + password)

        String token = ServerUtility.extractBearerToken(t);

        boolean isValidToken =  (token != null && TokenValidator.getInstance().isValidTokenStr(token));
        boolean isMtlsUsed = false;

        if (t instanceof HttpsExchange https) {
            SSLSession session = https.getSSLSession();
            try {
                //Throws if client didn't present valid certificate
                session.getPeerPrincipal().getName();
                
                //System.out.println("New connection with mTLS. ClientDn: " + clientDn);

                isMtlsUsed = true;
            } catch (SSLPeerUnverifiedException ignored) {
                //System.out.println("New connection without mTLS. Authorization required!");

                isMtlsUsed = false;
            }
        }

        if (isValidToken) {
            //If token leaks, attacker could keep token alive infinitely
            //Maybe client should request new token when token expires instead of refreshing token on every request
            TokenValidator.getInstance().refreshTokenStr(token);
        }

        return (isMtlsUsed || isValidToken);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void handle(HttpExchange t) throws IOException {
        try 
        {
            URI uri = t.getRequestURI();
            Map<String, String> query = ServerUtility.parseQuery(t);

            if (uri.getPath().equals("/api/auth/token")) {
                //This function check if client sent valid credentials
                //If credentials are correct, token is issued
                HandleAuthRequest(t);
                return;
            }

            //Lets check authorization
            if (!checkAuthorization(t)) {
                System.out.println("Unauthorized client, mTLS or valid bearer token is required.");
                ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
                return;
            }

            String endpoint;
            switch (uri.getPath()) {
                case "/api/patients" -> endpoint = "patients";
                case "/api/records" -> endpoint = "records";
                case "/api/sync" -> endpoint = "sync";
                default -> {
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.NOT_FOUND);
                    return;
                }
            }

            String method = t.getRequestMethod();

            if (endpoint.equals("sync")) {
                if (method.equalsIgnoreCase("GET")) {
                    handleSyncRequest(t, query);
                } else {
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.FORBIDDEN);
                }
                return;
            }
            if (method.equalsIgnoreCase("POST")) {
                handlePostRequest(t, query, endpoint);
            } else if (method.equalsIgnoreCase("GET")) {
                handleGetRequest(t, query, endpoint);
            } else if (method.equalsIgnoreCase("DELETE")) {
                handleDeleteRequest(t, query, endpoint);
            } else if (method.equalsIgnoreCase("PUT")) {
                handleUpdateRequest(t, query, endpoint);
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