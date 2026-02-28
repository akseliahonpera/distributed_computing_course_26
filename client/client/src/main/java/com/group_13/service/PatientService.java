package com.group_13.service;

import com.group_13.api.AuthAPI;
import com.group_13.api.PatientAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;
import com.group_13.model.Result;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientService {
    private static final PatientService INSTANCE = new PatientService();
    private final PatientAPI patientAPI = PatientAPI.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PatientService() {
    }

    public static PatientService getInstance() {
        return INSTANCE;
    }

    // Helper method to handle API responses and parse JSON
    private <T> Result<T> handleResponse(ApiResponse response,
            Class<T> klass,
            String successMessage) {
        try {
            if (response.getStatusCode() == 200) {
                T data = objectMapper.readValue(response.getBody(), klass);
                return new Result<>(true, data, successMessage);
            } else {
                return new Result<>(false, null,
                        "HTTP error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            return new Result<>(false, null,
                    "Parse error: " + e.getMessage());
        }
    }

    public CompletableFuture<Result<Patient[]>> getAllPatientsAsync() {
        return patientAPI.getAllPatientsAsync()
                .thenApply(response -> handleResponse(response, Patient[].class,
                        "Successfully fetched patients"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Patient[]> getAllPatients() throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getAllPatients();
        if (response.getStatusCode() == 200) {
            success = true;
            Patient[] patients = objectMapper.readValue(response.getBody(), Patient[].class);
            return new Result<Patient[]>(success, patients, "Succesfully fetched patients");
        }
        return new Result<Patient[]>(success, null,
                "Failed to fetch patients. HTTP status code: " + response.getStatusCode());
    }
    public CompletableFuture<Result<Patient[]>> getPatientByIdAsync(Patient patient) {
        return patientAPI.getPatientByIdAsync(patient)
                .thenApply(response -> handleResponse(response, Patient[].class,
                        "Successfully fetched patient"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Patient> getPatientById(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getPatientById(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient patient1 = objectMapper.readValue(response.getBody(), Patient.class);
            // Minne tämä query tulos pukataan??
            return new Result<Patient>(success, patient1, "Succesfully fetched patient");
        }
        return new Result<Patient>(success, null,
                "Failed to fetch patient. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Patient[]>> getPatientAsync(Patient patient) {
        return patientAPI.getPatientAsync(patient)
                .thenApply(response -> handleResponse(response, Patient[].class,
                        "Successfully fetched patient"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Patient[]> getPatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getPatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient[] patient1 = objectMapper.readValue(response.getBody(), Patient[].class);
            test_shit(patient1);
            // Minne tämä query tulos pukataan??
            return new Result<Patient[]>(success, patient1, "Succesfully fetched patient");
        }
        return new Result<Patient[]>(success, null,
                "Failed to fetch patient. HTTP status code: " + response.getStatusCode());
    }

    private void test_shit(Patient[] patient) {
        for (int i = 0; i < patient.length; i++) {
            System.out.println("pat_test_: " + i + "sisältö " + patient[i]);
        }
    }

    public CompletableFuture<Result<Patient[]>> createPatientAsync(Patient patient) {
        return patientAPI.createPatientAsync(patient)
                .thenApply(response -> handleResponse(response, Patient[].class,
                        "Successfully created patient"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Patient[]> createPatient(Patient patient) throws Exception {
        System.out.println("Trying to create patienet");
        System.out.println(patient);
        boolean success = false;
        ApiResponse response = patientAPI.createPatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient[] patientData = objectMapper.readValue(response.getBody(), Patient[].class);
            System.out.println("Patient create api call Success");
            return new Result<Patient[]>(success, patientData, "Succesfully created patient");
        }
        System.out.println("Patient create api call Failed");
        return new Result<Patient[]>(success, null,

                "Failed to create patient. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Patient[]>> updatePatientAsync(Patient patient) {
        return patientAPI.updatePatientAsync(patient)
                .thenApply(response -> handleResponse(response, Patient[].class,
                        "Successfully updated patient"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Patient[]> updatePatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.updatePatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient[] patientData = objectMapper.readValue(response.getBody(), Patient[].class);
            return new Result<Patient[]>(success, patientData, "Succesfully updated patient");
        }
        return new Result<Patient[]>(success, null,
                "Failed to update patient. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Void>> deletePatientAsync(Patient patient) {
        return patientAPI.deletePatientAsync(patient)
                .thenApply(response -> {
                    if (response.getStatusCode() == 200) {
                        return new Result<Void>(true, null, "Successfully deleted patient");
                    } else {
                        return new Result<Void>(false, null,
                                "Failed to delete patient. HTTP status code: " + response.getStatusCode());
                    }
                })
                .exceptionally(ex -> new Result<Void>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Void> deletePatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.deletePatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully deleted patient");
        }
        return new Result<Void>(success, null,
                "Failed to delete patient. HTTP status code: " + response.getStatusCode());
    }
}