package com.group_13.service;

import com.group_13.api.PatientAPI;
import com.group_13.model.Patient;
import com.group_13.model.Result;

public class PatientService {
    private PatientAPI patientAPI = new PatientAPI();

    public Result<Patient[]> getAllPatients() throws Exception {
        return null;
    }

    public Result<Patient> getPatientById(String id) throws Exception {
        return null;
    }

    public Result<Void> createPatient(Patient patient) throws Exception {
        return null;
    }

    public Result<Void> updatePatient(String id, Patient patient) throws Exception {
        return null;
    }

    public Result<Void> deletePatient(String id) throws Exception {
        return null;
    }
}