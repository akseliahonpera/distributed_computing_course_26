package com.group_13;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.*;
import java.net.URI;

public class RequestHandler implements HttpHandler
{
    public RequestHandler() {

    }

    static String getRequestBodyText(HttpExchange t)  throws IOException
    {
        InputStreamReader stream = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);

        int c;
        StringBuilder buf = new StringBuilder();
        while ((c = reader.read()) != -1) {
            buf.append((char) c);
        }

        String text = buf.toString();

        stream.close();

        return text;
    }


    private void handlePatientsRequest(HttpExchange t) throws IOException {
        String method = t.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            String bodyText = ServerUtility.GetBodyText(t);

            //New patient
            //Body text contains JSON which contains person info

        } else if (method.equalsIgnoreCase("GET")) {
            Map<String, String> query = ServerUtility.parseQuery(t);
            
            if (query.containsKey("findName")) {
                String name = query.get("findName");

                //Query database to find all PatientID with given name
                //Send patients IDs
            } else if (query.containsKey("findHetu")) {
                String hetu = query.get("findHetu");

                //Query database to find all PatientID with given Hetu
                //Send patients IDs
            } else if (query.containsKey("PatientID")) {
                String id = query.get("PatientID");

                //Query database to get patient info
                //Send patient data using JSON
            }

        }
    }
    private void handleRecordsRequest(HttpExchange t) throws IOException {
        String method = t.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> query = ServerUtility.parseQuery(t);
            String patientID = query.get("PatientID");
            String recordJSON = ServerUtility.GetBodyText(t);

            //Parse JSON and store data to database
        } else if (method.equalsIgnoreCase("GET")) {
            Map<String, String> query = ServerUtility.parseQuery(t);

            if (query.containsKey("PatientID")) {
                String patientID = query.get("PatientID");

                //Query database using PatientID to get all records for given patient
            } else if (query.containsKey("RecordID")) {
                String recordID = query.get("RecordID");

                //Query database using RecordID
                //Send record using JSON
            }
        }
    }

    public void handle(HttpExchange t) throws IOException {
        try 
        {
            URI uri = t.getRequestURI();
            switch (uri.getPath()) {
                case "/api/patients":
                    handlePatientsRequest(t);
                    break;
                case "/api/records":
                    handleRecordsRequest(t);
                    break;
                default:
                    t.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}