package com.group_13.api;

import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import com.group_13.model.Patient;
import com.group_13.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * PatientAPI class to handle API requests related to Patients.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Patient resources.
 */

public class PatientAPI extends BaseAPI {
    private static final String PATIENTS_ENDPOINT = BASE_URL + "/patients";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PatientAPI() {
        super();
    }

    public ApiResponse getAllPatients() {
        // implement getting all patients
        return null;
    }

    public ApiResponse getPatientById(String id, String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + "?PatientID=" + id))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse getPatient(Patient patient, String token) throws Exception {

        String queryString = "?";

        if (patient.getId() != null) {
            return getPatientById(patient.getId(), token);
        }
        if (patient.getFirstName() != null) {
            queryString += "&PatientFirstName=" + patient.getFirstName();
        }
        if (patient.getLastName() != null) {
            queryString += "&PatientLastName=" + patient.getLastName();
        }
        if (patient.getDOB() != null) {
            queryString += "&PatientDOB=" + patient.getDOB();
        }
        if (patient.getAddress() != null) {
            queryString += "&PatientAddress=" + patient.getAddress();
        }   
        if (patient.getSsn() != null) {
            queryString += "&PatientSSN=" + patient.getSsn();
        }
        if (patient.getPhone() != null) {
            queryString += "&PatientPhone=" + patient.getPhone();
        }
        if (patient.getPhone2() != null) {
            queryString += "&PatientPhone2=" + patient.getPhone2();
        }
        if (patient.getHomeHospital() != null) {
            queryString += "&PatientHomeHospital=" + patient.getHomeHospital();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT + queryString))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse createPatient(Patient patient, String token) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(patient);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PATIENTS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse updatePatient(String id, Patient patient) {
        // implement updating an existing patient
        return null;
    }

    public ApiResponse deletePatient(String id) {
        // implement deleting a patient
        return null;
    }
}