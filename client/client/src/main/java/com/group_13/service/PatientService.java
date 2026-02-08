package com.group_13.service;

import com.group_13.api.PatientAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;
import com.group_13.model.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientService {
    private PatientAPI patientAPI = new PatientAPI();
    private ObjectMapper objectMapper = new ObjectMapper();

    public Result<Patient[]> getAllPatients() throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getAllPatients();
        if (response.getStatusCode() == 200) {
            success = true;
            Patient[] patients = objectMapper.readValue(response.getbody(), Patient[].class);
            return new Result<Patient[]>(success, patients, "Succesfully fetched patients");
        }
        return new Result<Patient[]>(success, null, "Failed to fetch patients. HTTP status code: " + response.getStatusCode());
    }

    public Result<Patient> getPatientById(String id) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.getPatientById(id);
        if (response.getStatusCode() == 200) {
            success = true;
            Patient patient = objectMapper.readValue(response.getbody(), Patient.class);
            return new Result<Patient>(success, patient, "Succesfully fetched patient");
        }
        return new Result<Patient>(success, null, "Failed to fetch patient. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> createPatient(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.createPatient(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully created patient");
        }
        return new Result<Void>(success, null, "Failed to create patient. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> updatePatient(String id, Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.updatePatient(id, patient);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully updated patient");
        }
        return new Result<Void>(success, null, "Failed to update patient. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> deletePatient(String id) throws Exception {
        boolean success = false;
        ApiResponse response = patientAPI.deletePatient(id);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully deleted patient");
        }
        return new Result<Void>(success, null, "Failed to delete patient. HTTP status code: " + response.getStatusCode());
    }
}