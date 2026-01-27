package com.group_13.service;

import com.group_13.api.RecordAPI;
import com.group_13.model.Record;

public class RecordService {
    private RecordAPI recordAPI = new RecordAPI();

    public Record[] getAllRecords() throws Exception {
        return recordAPI.getAllRecords();
    }

    public Record getRecordById(String id) throws Exception {
        return recordAPI.getRecordById(id);
    }

    public Record[] getRecordsByPatientId(String patientId) throws Exception {
        return recordAPI.getRecordsByPatientId(patientId);
    }

    public void createRecord(Record record) throws Exception {
        recordAPI.createRecord(record);
    }

    public void updateRecord(String id, Record record) throws Exception {
        recordAPI.updateRecord(id, record);
    }

    public void deleteRecord(String id) throws Exception {
        recordAPI.deleteRecord(id);
    }
}
