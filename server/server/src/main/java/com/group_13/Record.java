package com.group_13;

public class Record {
    private String id;
    private String patientId;
    private String dateTime;
    private String operation;
    private String responsible;
    private String other;

    public Record() {
    }

    public Record(String id, String patientId, String dateTime, String operation,
                  String responsible, String other) {
        this.id = id;
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.operation = operation;
        this.responsible = responsible;
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
