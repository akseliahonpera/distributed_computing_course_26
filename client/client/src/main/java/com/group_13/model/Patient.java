package com.group_13.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing a patient.
 */

public class Patient {
    private String id;
    private String fname;
    private String lname;
    private String dateofbirth;
    private String socialsecnum;
    private String address;
    private String phone;
    private String emergencycontact;
    private String homehospital;

    public Patient() {
    }


    public Patient(String fname, String lname, String dateofbirth, String socialsecnum,
            String address, String phone, String emergencycontact, String homehospital) {
        this.fname = fname;
        this.lname = lname;
        this.dateofbirth = dateofbirth;
        this.socialsecnum = socialsecnum;
        this.address = address;
        this.phone = phone;
        this.emergencycontact = emergencycontact;
        this.homehospital = homehospital;
    }

    public Patient(String id, String fName, String lName, String dateofbirth, String socialSecNum,
            String address, String phone, String emergencyContact, String homehospital) {
        this.id = id;
        this.fname = fName;
        this.lname = lName;
        this.dateofbirth = dateofbirth;
        this.socialsecnum = socialSecNum;
        this.address = address;
        this.phone = phone;
        this.emergencycontact = emergencyContact;
        this.homehospital = homehospital;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    @Override
    public String toString() {
        return "Patient [id=" + id + ", fname=" + fname + ", lname=" + lname + ", dateofbirth=" + dateofbirth
                + ", socialsecnum=" + socialsecnum + ", address=" + address + ", phone=" + phone + ", emergencycontact="
                + emergencycontact + ", homehospital=" + homehospital + "]";
    }


    public String getFName() {
        return fname;
    }

    public void setFName(String fName) {
        this.fname = fName;
    }

    public String getLName() {
        return lname;
    }

    public void setLName(String lName) {
        this.lname = lName;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getSocialsecnum() {
        return socialsecnum;
    }

    public void setSocialsecnum(String socialSecNum) {
        this.socialsecnum = socialSecNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencycontact() {
        return emergencycontact;
    }

    public void setEmergencycontact(String emergencyContact) {
        this.emergencycontact = emergencyContact;
    }

    public String getHomehospital() {
        return homehospital;
    }

    public void setHomehospital(String homehospital) {
        this.homehospital = homehospital;
    }
}