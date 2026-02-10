package com.group_13.model;

/**
 * Generic Result class to encapsulate CRUD operation results.
 * May contain Record, Patient, or other data types. Also includes a message for the user.
 * Intended to be produced by Service classes, and consumed by Controller classes.
 * @param <T> the type of data being returned
 */

public class Result<T> {
    private boolean success; // indicates if the operation was successful
    private T data; // the data returned by the operation, if any (Patient, Record, Null)
    private String message; // a message describing the result, for user display/debugging

    public Result(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
