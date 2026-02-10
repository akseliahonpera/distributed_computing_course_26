package com.group_13.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing a patient.
 */

public class Patient {
    private String Id;
    private String fName;
    private String lName;
    private String dateofbirth;
    private String socialSecNum;
    private String address;
    private String phone;
    private String emergencyContact;
    private String homehospital;

    public Patient() {
    }

    public Patient(String Id, String fName, String lName, String dateofbirth, String socialSecNum,
            String address, String phone, String emergencyContact, String homehospital) {
        this.Id = Id;
        this.fName = fName;
        this.lName = lName;
        this.dateofbirth = dateofbirth;
        this.socialSecNum = socialSecNum;
        this.address = address;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.homehospital = homehospital;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getSocialSecNum() {
        return socialSecNum;
    }

    public void setSocialSecNum(String socialSecNum) {
        this.socialSecNum = socialSecNum;
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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getHomehospital() {
        return homehospital;
    }

    public void setHomehospital(String homehospital) {
        this.homehospital = homehospital;
    }
}