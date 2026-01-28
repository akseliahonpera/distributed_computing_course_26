package com.group_13.api;

import java.net.http.HttpRequest;
import com.group_13.model.Patient;
import com.group_13.model.ApiResponse;

/**
 * PatientAPI class to handle API requests related to Patients.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Patient resources.
 */

public class PatientAPI extends BaseAPI {
    private static final String PATIENTS_ENDPOINT = BASE_URL + "/patients";

    public PatientAPI() {
        super();
    }

    public ApiResponse getAllPatients() {
        // implement getting all patients
        return null;
    }

    public ApiResponse getPatientById(String id) {
        // implement getting a patient by ID
        return null;
    }

    public ApiResponse getPatientByName(String name) {
        // implement getting a patient by name
        return null;
    }

    public ApiResponse getPatientBySSN(String ssn) {
        // implement getting a patient by soscial security number
        return null;
    }

    public ApiResponse createPatient(Patient patient) {
        // implement creating a new patient
        return null;
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