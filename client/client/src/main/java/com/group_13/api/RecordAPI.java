package com.group_13.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.util.LinkedHashMap;
import java.util.Map;

import com.group_13.model.Record;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;

/**
 * RecordAPI class to handle API requests related to Records.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Record resources.
 */

public class RecordAPI extends BaseAPI {
    private static final String RECORDS_ENDPOINT = BASE_URL + "/records";

    public RecordAPI() {
        super();
    }

    public ApiResponse getAllRecords() throws Exception {
        // implement getting all records
        // this should return only the most rudimentary info of the records
        // not sure if this is needed
        return null;
    }

    public ApiResponse getRecordById(String id) throws Exception {
        // implement getting a record by ID
        // this should return the full info of the record
        return null;
    }

    public ApiResponse getRecordsByPatientId(String patientId) throws Exception {
        // implement getting records by patient ID
        // should probably return only the most rudimentary info of the records
        return null;
    }

    public ApiResponse createRecord(Record record) throws Exception {
        // implement creating a new record
        return null;
    }

    public ApiResponse updateRecord(String id, Record record) throws Exception {
        // implement updating an existing record
        return null;
    }

    public ApiResponse deleteRecord(String id) throws Exception {
        // implement deleting a record
        return null;
    }

    private String recordToQueryString(Record record) throws UnsupportedEncodingException {
        Map<String, String> params = new LinkedHashMap<>();

        if (record.getID() != null)
            params.put("ID", record.getID());
        if (record.getPatientID() != null)
            params.put("patientID", record.getPatientID());
        if (record.getDatetime() != null)
            params.put("datetime", record.getDatetime());
        if (record.getOperation() != null)
            params.put("operation", record.getOperation());
        if (record.getResponsible() != null)
            params.put("responsible", record.getResponsible());
        if (record.getFollowUp() != null)
            params.put("followUp", record.getFollowUp());
        if (params.isEmpty())
            return "";

        StringBuilder query = new StringBuilder("?");
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first)
                query.append("&");
            query.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            first = false;
        }

        return query.toString();
    }
}
