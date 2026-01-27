package com.group_13.service;

import com.group_13.api.PatientAPI;
import com.group_13.model.Patient;

public class PatientService {
    private PatientAPI patientAPI = new PatientAPI();

    public Patient[] getAllPatients() throws Exception {
        return patientAPI.getAllPatients();
    }

    public Patient getPatientById(String id) throws Exception {
        return patientAPI.getPatientById(id);
    }

    public void createPatient(Patient patient) throws Exception {
        patientAPI.createPatient(patient);
    }

    public void updatePatient(String id, Patient patient) throws Exception {
        patientAPI.updatePatient(id, patient);
    }

    public void deletePatient(String id) throws Exception {
        patientAPI.deletePatient(id);
    }
}