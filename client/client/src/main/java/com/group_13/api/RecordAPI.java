package com.group_13.api;

import java.net.http.HttpRequest;
import com.group_13.model.Record;

public class RecordAPI extends BaseAPI {
    private static final String RECORDS_ENDPOINT = BASE_URL + "/records";

    public RecordAPI() {
        super();
    }

    public Record[] getAllRecords() throws Exception {
        // implement getting all records
        // this should return only the most rudimentary info of the records
        // not sure if this is needed
        return null;
    }

    public Record getRecordById(String id) throws Exception {
        // implement getting a record by ID
        // this should return the full info of the record
        return null;
    }

    public Record[] getRecordsByPatientId(String patientId) throws Exception {
        // implement getting records by patient ID
        // should probably return only the most rudimentary info of the records
        return null;
    }

    public Record createRecord(Record record) throws Exception {
        // implement creating a new record
        return null;
    }

    public void updateRecord(String id, Record record) throws Exception {
        // implement updating an existing record
    }

    public void deleteRecord(String id) throws Exception {
        // implement deleting a record
    }
}
