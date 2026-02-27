package com.group_13.service;

import com.group_13.api.RecordAPI;
import com.group_13.model.ApiResponse;
import com.group_13.model.Record;
import com.group_13.model.Patient;
import com.group_13.model.Result;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RecordService {
    private static final RecordService INSTANCE = new RecordService();
    private final RecordAPI recordAPI = RecordAPI.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static RecordService getInstance() {
        return INSTANCE;
    }

    // Helper method to handle API responses and parse JSON
    private <T> Result<T> handleResponse(ApiResponse response,
            Class<T> klass,
            String successMessage) {
        try {
            if (response.getStatusCode() == 200) {
                T data = objectMapper.readValue(response.getBody(), klass);
                return new Result<>(true, data, successMessage);
            } else {
                return new Result<>(false, null,
                        "HTTP error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            return new Result<>(false, null,
                    "Parse error: " + e.getMessage());
        }
    }

    public CompletableFuture<Result<Record[]>> getAllRecordsAsync() {
        return recordAPI.getAllRecordsAsync()
                .thenApply(response -> handleResponse(response, Record[].class,
                        "Successfully fetched records"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record[]> getAllRecords() throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getAllRecords();
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null,
                "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Record>> getRecordByIdAsync(Record record) {
        return recordAPI.getRecordByIdAsync(record)
                .thenApply(response -> handleResponse(response, Record.class,
                        "Successfully fetched record"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record> getRecordById(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordById(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record record1 = objectMapper.readValue(response.getBody(), Record.class);
            return new Result<Record>(success, record1, "Succesfully fetched record");
        }
        return new Result<Record>(success, null,
                "Failed to fetch record. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Record[]>> getRecordsByPatientIdAsync(Patient patient) {
        return recordAPI.getRecordsByPatientIdAsync(patient)
                .thenApply(response -> handleResponse(response, Record[].class,
                        "Successfully fetched records"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record[]> getRecordsByPatientId(Patient patient) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecordsByPatientId(patient);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null,
                "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Record[]>> getRecordAsync(Record record) {
        return recordAPI.getRecordAsync(record)
                .thenApply(response -> handleResponse(response, Record[].class,
                        "Successfully fetched records"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record[]> getRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.getRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully fetched records");
        }
        return new Result<Record[]>(success, null,
                "Failed to fetch records. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Record[]>> createRecordAsync(Record record) {
        return recordAPI.createRecordAsync(record)
                .thenApply(response -> handleResponse(response, Record[].class,
                        "Successfully created record"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record[]> createRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.createRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] record1 = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, record1, "Succesfully created record");
        }
        return new Result<Record[]>(success, null,
                "Failed to create record. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Record[]>> updateRecordAsync(Record record) {
        return recordAPI.updateRecordAsync(record)
                .thenApply(response -> handleResponse(response, Record[].class,
                        "Successfully updated record"))
                .exceptionally(ex -> new Result<>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Record[]> updateRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.updateRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            Record[] records = objectMapper.readValue(response.getBody(), Record[].class);
            return new Result<Record[]>(success, records, "Succesfully updated record");
        }
        return new Result<Record[]>(success, null,
                "Failed to update record. HTTP status code: " + response.getStatusCode());
    }

    public CompletableFuture<Result<Void>> deleteRecordAsync(Record record) {
        return recordAPI.deleteRecordAsync(record)
                .thenApply(response -> {
                    if (response.getStatusCode() == 200) {
                        return new Result<Void>(true, null, "Successfully deleted record");
                    } else {
                        return new Result<Void>(false, null,
                                "Failed to delete record. HTTP status code: " + response.getStatusCode());
                    }
                })
                .exceptionally(ex -> new Result<Void>(false, null, "Request failed: " + ex.getMessage()));
    }

    public Result<Void> deleteRecord(Record record) throws Exception {
        boolean success = false;
        ApiResponse response = recordAPI.deleteRecord(record);
        if (response.getStatusCode() == 200) {
            success = true;
            return new Result<Void>(success, null, "Succesfully deleted record");
        }
        return new Result<Void>(success, null,
                "Failed to delete record. HTTP status code: " + response.getStatusCode());
    }
}
