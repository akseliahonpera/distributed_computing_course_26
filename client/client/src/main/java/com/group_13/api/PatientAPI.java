package com.group_13.api;

import java.net.http.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.group_13.model.Patient;
import com.group_13.model.ApiResponse;

/**
 * Handle API requests related to Patients.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Patient resources.
 */

public class PatientAPI extends BaseAPI {
    private static final PatientAPI INSTANCE = new PatientAPI();
    private static final String PATIENTS_ENDPOINT = BASE_URL + "/patients";

    private PatientAPI() {
        super();
    }

    public static PatientAPI getInstance() {
        return INSTANCE;
    }

    public ApiResponse getAllPatients() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }


    public ApiResponse getPatientById(Patient patient) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + "?PatientID="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse getPatient(Patient patient) throws Exception {

        String queryString = patientToQueryString(patient);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + queryString))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse createPatient(Patient patient) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(patient);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse updatePatient(Patient patient) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(patient);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse deletePatient(Patient patient) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + "?patientID="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    private String patientToQueryString(Patient patient) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        if (patient.getId() != null)
            params.put("Id", patient.getId());

        if (patient.getFName() != null)
            params.put("fName", patient.getFName());

        if (patient.getLName() != null)
            params.put("lName", patient.getLName());

        if (patient.getDateofbirth() != null)
            params.put("dateofbirth", patient.getDateofbirth());

        if (patient.getAddress() != null)
            params.put("address", patient.getAddress());

        if (patient.getSocialsecnum() != null)
            params.put("socialSecNum", patient.getSocialsecnum());

        if (patient.getPhone() != null)
            params.put("phone", patient.getPhone());

        if (patient.getEmergencycontact() != null)
            params.put("emergency_contact", patient.getEmergencycontact());

        if (patient.getHomehospital() != null)
            params.put("homehospital", patient.getHomehospital());

        if (params.isEmpty())
            return "";

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