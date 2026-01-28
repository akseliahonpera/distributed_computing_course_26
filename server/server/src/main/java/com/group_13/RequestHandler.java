package com.group_13;


import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler
{
    public RequestHandler() {

    }

    public void handlePostRequest(HttpExchange t, Map<String, String> query, String table) throws SQLException, IOException
    {
        String bodyText = ServerUtility.GetBodyText(t);

        JSONObject object = new JSONObject(bodyText);

        DataBaseQueryHelper.insert(DataBase.getDatabase(), table, object);

        ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.OK);
    }

    public void handleGetRequest(HttpExchange t, Map<String, String> query, String table) throws SQLException, IOException
    {
        JSONArray result = DataBaseQueryHelper.query(DataBase.getDatabase(), table, query);

        ServerUtility.sendResponse(t, result.toString(), ServerUtility.HttpStatus.OK);
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
            responseJSON.append("token", token.getTokenStr());
            responseJSON.append("expiration", token.getExpirationTime());

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

            //DISABLED FOR TESTING PURPOSES!!!

            /*String token = ServerUtility.extractBearerToken(t);
            if (token == null || !TokenValidator.getInstance().isValidTokenStr(token)) {
                ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
                System.out.println("Unauthorized. No valid token!");
                if (token != null) {
                    System.out.println("Token: " + token);
                }
                return;
            }*/

            String table = "";
            switch (uri.getPath()) {
                case "/api/patients":
                    table = "patients";
                    break;
                case "/api/records":
                    table = "records";
                    break;
                default:
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.NOT_FOUND);
            }

            String method = t.getRequestMethod();
            if (method.equalsIgnoreCase("POST")) {
                handlePostRequest(t, query, table);
            } else if (method.equalsIgnoreCase("GET")) {
                handleGetRequest(t, query, table);
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