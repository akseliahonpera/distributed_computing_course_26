package com.group_13;


import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler
{
    public RequestHandler() {

    }

    private void handlePatientsRequest(HttpExchange t, Map<String, String> query) throws IOException {
        String method = t.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            String bodyText = ServerUtility.GetBodyText(t);

            System.out.println("POST new patient  Content: " + bodyText);
            //New patient
            //Body text contains JSON which contains person info

        } else if (method.equalsIgnoreCase("GET")) {
            if (query.containsKey("findName")) {
                String name = query.get("findName");

                System.out.println("GET patient with findName=" + name);

                //Query database to find all PatientID with given name
                //Send patients IDs
            } else if (query.containsKey("findHetu")) {
                String hetu = query.get("findHetu");

                System.out.println("GET patient with findHetu=" + hetu);

                //Query database to find all PatientID with given Hetu
                //Send patients IDs
            } else if (query.containsKey("PatientID")) {
                String patientID = query.get("PatientID");

                System.out.println("GET patient with PatientID=" + patientID);
                //Query database to get patient info
                //Send patient data using JSON
            }

        }
    }
    private void handleRecordsRequest(HttpExchange t, Map<String, String> query) throws IOException {
        String method = t.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            String patientID = query.get("PatientID");
            String recordJSON = ServerUtility.GetBodyText(t);

            System.out.println("POST record PatientID=" + patientID + "  Content: " + recordJSON);
            //Parse JSON and store data to database
        } else if (method.equalsIgnoreCase("GET")) {
            if (query.containsKey("PatientID")) {
                String patientID = query.get("PatientID");

                System.out.println("GET records with PatientID=" + patientID);

                //Query database using PatientID to get all records for given patient
            } else if (query.containsKey("RecordID")) {
                String recordID = query.get("RecordID");

                System.out.println("GET records with RecordID=" + recordID);

                //Query database using RecordID
                //Send record using JSON
            }
        }
    }

    private void HandleAuthRequest(HttpExchange t, Map<String, String> query) throws IOException {
        String method = t.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
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
        } else {
            ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        System.out.println("Handling");
        try 
        {
            URI uri = t.getRequestURI();
            Map<String, String> query = ServerUtility.parseQuery(t);
            System.out.println(uri.toString());

            if (uri.getPath().equals("/api/auth/token")) {
                HandleAuthRequest(t, query);
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

            switch (uri.getPath()) {
                case "/api/patients":
                    handlePatientsRequest(t, query);
                    break;
                case "/api/records":
                    handleRecordsRequest(t, query);
                    break;
                default:
                    ServerUtility.sendResponse(t, "", ServerUtility.HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}