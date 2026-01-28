package com.group_13.service;

import com.group_13.api.RecordAPI;
import com.group_13.model.Record;
import com.group_13.model.Result;

public class RecordService {
    private RecordAPI recordAPI = new RecordAPI();

    public Result<Record[]> getAllRecords() throws Exception {
        return null;
    }

    public Result<Record> getRecordById(String id) throws Exception {
        return null;
    }

    public Result<Record[]> getRecordsByPatientId(String patientId) throws Exception {
        return null;
    }

    public Result<Void> createRecord(Record record) throws Exception {
        return null;
    }

    public Result<Void> updateRecord(String id, Record record) throws Exception {
        return null;
    }

    public Result<Void> deleteRecord(String id) throws Exception {
        return null;
    }
}
