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
                .uri(URI.create(PATIENTS_ENDPOINT + "?id="
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
        String queryString = "?id="+patient.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + queryString))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse deletePatient(Patient patient) throws Exception {
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + "?id="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    private String patientToQueryString(Patient patient) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        System.out.println("paskanvittu"+patient.toString());

        if (patient.getId() != null && !patient.getId().isBlank()){
            params.put("id", patient.getId());
            System.out.println("ei null id ");
}
        if (patient.getFName() != null && !patient.getFName().isBlank()){
            params.put("fname", patient.getFName());
            System.out.println("ei null dname ");
}
        if (patient.getLName() != null&& !patient.getLName().isBlank()){
            params.put("lname", patient.getLName());
        System.out.println("ei null lname ");
}
        if (patient.getDateofbirth() != null&& !patient.getDateofbirth().isBlank()){
            params.put("dateofbirth", patient.getDateofbirth());
        System.out.println("ei null dob ");
}
        if (patient.getAddress() != null&& !patient.getAddress().isBlank()){
            params.put("address", patient.getAddress());
        System.out.println("ei null address ");
}
        if (patient.getSocialsecnum() != null&& !patient.getSocialsecnum().isBlank()){
            params.put("socialsecnum", patient.getSocialsecnum());
        System.out.println("ei null sosecnum ");
}
        if (patient.getPhone() != null&& !patient.getPhone().isBlank()){
            params.put("phone", patient.getPhone());
        System.out.println("ei null nmbr ");
}
        if (patient.getEmergencycontact() != null&& !patient.getEmergencycontact().isBlank()){
            params.put("emergency_contact", patient.getEmergencycontact());
        System.out.println("ei null e_contact ");
}
        if (patient.getHomehospital() != null&& !patient.getHomehospital().isBlank()){
            params.put("homehospital", patient.getHomehospital());
            System.out.println("ei null homostal ");
        }
        if (params.isEmpty()){
            System.out.println("ei empty query ");
            return "";
}
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