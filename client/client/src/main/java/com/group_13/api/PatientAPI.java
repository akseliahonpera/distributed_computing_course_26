package com.group_13.api;

import com.group_13.model.Patient;
import java.net.http.HttpRequest;

public class PatientAPI extends BaseAPI {
    private static final String PATIENTS_ENDPOINT = BASE_URL + "/patients";

    public PatientAPI() {
        super();
    }

    public Patient[] getAllPatients() {
        // implement getting all patients
    }

    public Patient getPatientById(String id) {
        // implement getting a patient by ID
    }

    public void createPatient(Patient patient) {
        // implement creating a new patient
    }

    public void updatePatient(String id, Patient patient) {
        // implement updating an existing patient
    }

    public void deletePatient(String id) {
        // implement deleting a patient
    }
