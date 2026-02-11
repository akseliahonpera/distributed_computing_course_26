package com.group_13;
import com.group_13.model.Patient;
import com.group_13.model.Result;
import com.group_13.service.PatientService;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class PatientServiceTest {

    private Patient testPatient;
    private PatientService patientService;

    public PatientServiceTest(){}

    @Before
    public void setUp() {

        patientService = PatientService.getInstance();

        testPatient = new Patient();
        testPatient.setFName("Testi");
        testPatient.setLName("Potilas");
        testPatient.setAddress("Lasaretti 5 A");
        testPatient.setSocialsecnum("123456789");
        testPatient.setPhone("020202");
        testPatient.setHomehospital("KYS");
        testPatient.setEmergencycontact("Pentti");
    }

    @Test
    public void testCreatePatient() throws Exception {
        Result result = patientService.createPatient(testPatient);
        System.out.println(result.getMessage());
        assertTrue(result.isSuccess());
    }

    // @Test
    // public void     
    


}