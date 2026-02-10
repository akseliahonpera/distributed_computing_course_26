package com.group_13.api;


import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.group_13.model.Record;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;

/**
 * Handles API requests related to Records.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Record resources.
 */

public class RecordAPI extends BaseAPI {
    private static final RecordAPI INSTANCE = new RecordAPI();
    private static final String RECORDS_ENDPOINT = BASE_URL + "/records";

    private RecordAPI() {
        super();
    }

    public static RecordAPI getInstance() {
        return INSTANCE;
    }

    public ApiResponse getAllRecords() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse getRecordById(Record record) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?ID="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse getRecordsByPatientId(Patient patient) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?patientID="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse createRecord(Record record) throws Exception {
        
        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse updateRecord(Record record) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?ID="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse deleteRecord(Record record) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?ID="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    private String recordToQueryString(Record record) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        // if (record.getID() != null)
        //     params.put("ID", record.getID());
        // if (record.getPatientID() != null)
        //     params.put("patientID", record.getPatientID());
        // if (record.getDatetime() != null)
        //     params.put("datetime", record.getDatetime());
        // if (record.getOperation() != null)
        //     params.put("operation", record.getOperation());
        // if (record.getResponsible() != null)
        //     params.put("responsible", record.getResponsible());
        // if (record.getFollowUp() != null)
        //     params.put("followUp", record.getFollowUp());
        // if (params.isEmpty())
        //     return "";

        StringBuilder query = new StringBuilder("?");
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first)
                query.append("&");
            query.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            first = false;
        }

        return query.toString();
    }
}
