package com.group_13.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing a medical record.
 */

public class Record {
    private String id;
    private String patientid;
    private String datetime;
    private String operation;
    private String responsible;
    private String followUp;

    public Record() {
    }

    public Record(String ID, String patientID, String datetime, String operation,
                  String responsible, String followUp) {
        this.id = ID;
        this.patientid = patientID;
        this.datetime = datetime;
        this.operation = operation;
        this.responsible = responsible;
        this.followUp = followUp;
    }

    public String getId() {
        return id;
    }

    public void setId(String ID) {
        this.id = ID;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientID) {
        this.patientid = patientID;
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
