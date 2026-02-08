package com.group_13.service;

import com.group_13.api.RecordAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Record;
import com.group_13.model.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecordService {
    private RecordAPI recordAPI = new RecordAPI();
    private ObjectMapper objectMapper = new ObjectMapper();

    public Result<Record[]> getAllRecords() throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getAllRecords();
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getbody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null, "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public Result<Record> getRecordById(String id) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordById(id);
        if (response.getStatusCode() == 200) {
            success = true;
            Record record = objectMapper.readValue(response.getbody(), Record.class);
            return new Result<Record>(success, record, "Succesfully fetched record");
        }
        return new Result<Record>(success, null, "Failed to fetch record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Record[]> getRecordsByPatientId(String patientId) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordsByPatientId(patientId);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getbody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null, "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> createRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.createRecord(record);
        if (response.getStatusCode() == 201) {
            success = true;
            return new Result<Void>(success, null, "Succesfully created record");
        }
        return new Result<Void>(success, null, "Failed to create record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> updateRecord(String id, Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.updateRecord(id, record);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully updated record");
        }
        return new Result<Void>(success, null, "Failed to update record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> deleteRecord(String id) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.deleteRecord(id);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully deleted record");
        }
        return new Result<Void>(success, null, "Failed to delete record. HTTP status code: " + response.getStatusCode());
    }
}
