package com.group_13.service;

import com.group_13.api.AuthAPI;
import com.group_13.api.PatientAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;
import com.group_13.model.Result;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    public Result<Patient> getPatientById(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getPatientById(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient patient1 = objectMapper.readValue(response.getBody(), Patient.class);
            return new Result<Patient>(success, patient1, "Succesfully fetched patient");
        }
        return new Result<Patient>(success, null,
                "Failed to fetch patient. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> createPatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.createPatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully created patient");
        }
        return new Result<Void>(success, null,
                "Failed to create patient. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> updatePatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.updatePatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully updated patient");
        }
        return new Result<Void>(success, null,
                "Failed to update patient. HTTP status code: " + response.getStatusCode());
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