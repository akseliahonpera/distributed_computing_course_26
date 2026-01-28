package com.group_13.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing a medical record.
 */

public class Record {
    private String ID;
    private String patientID;
    private String datetime;
    private String operation;
    private String responsible;
    private String followUp;

    public Record() {
    }

    public Record(String ID, String patientID, String datetime, String operation,
                  String responsible, String followUp) {
        this.ID = ID;
        this.patientID = patientID;
        this.datetime = datetime;
        this.operation = operation;
        this.responsible = responsible;
        this.followUp = followUp;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getdatetime() {
        return datetime;
    }

    public void setdatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getfollowUp() {
        return followUp;
    }

    public void setfollowUp(String followUp) {
        this.followUp = followUp;
    }
}
