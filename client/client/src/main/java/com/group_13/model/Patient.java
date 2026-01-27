package com.group_13.model;

public class Patient {
    private String id;
    private String FirstName;
    private String LastName;
    private String DOB;
    private String ssn;
    private String address;
    private String phone;
    private String phone2;
    private String homeHospital;

    public Patient() {
    }

    public Patient(String id, String firstName, String lastName, String DOB, String ssn,
                   String address, String phone, String phone2, String homeHospital) {
        this.id = id;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.DOB = DOB;
        this.ssn = ssn;
        this.address = address;
        this.phone = phone;
        this.phone2 = phone2;
        this.homeHospital = homeHospital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
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

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getHomeHospital() {
        return homeHospital;
    }

    public void setHomeHospital(String homeHospital) {
        this.homeHospital = homeHospital;
    }
}
