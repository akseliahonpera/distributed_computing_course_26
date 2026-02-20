package com.group_13.service;

import com.group_13.api.RecordAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Record;
import com.group_13.model.Patient;
import com.group_13.model.Result;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RecordService {
    private static final RecordService INSTANCE = new RecordService();
    private final RecordAPI recordAPI = RecordAPI.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static RecordService getInstance() {
        return INSTANCE;
    }

    public Result<Record[]> getAllRecords() throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getAllRecords();
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null, "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public Result<Record> getRecordById(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordById(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record record1 = objectMapper.readValue(response.getBody(), Record.class);
            return new Result<Record>(success, record, "Succesfully fetched record");
        }
        return new Result<Record>(success, null, "Failed to fetch record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Record[]> getRecordsByPatientId(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordsByPatientId(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null, "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public Result<Record[]> getRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null, "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> createRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.createRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully created record");
        }
        return new Result<Void>(success, null, "Failed to create record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> updateRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.updateRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully updated record");
        }
        return new Result<Void>(success, null, "Failed to update record. HTTP status code: " + response.getStatusCode());
    }

    public Result<Void> deleteRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.deleteRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully deleted record");
        }
        return new Result<Void>(success, null, "Failed to delete record. HTTP status code: " + response.getStatusCode());
    }
}
