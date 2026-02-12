package com.group_13;

public class test_patient {
    private String id;
    private String fname;
    private String lname;
    private String dateofbirth;
    private String socialsecnum;
    private String address;
    private String phone;
    private String emergency_contact;
    private String homeHospital;

    public test_patient() {
    }

    public test_patient(String id, String firstName, String lastName, String DOB, String ssn,
                   String address, String phone, String phone2, String homeHospital) {
        this.id = id;
        this.fname = firstName;
        this.lname = lastName;
        this.dateofbirth = DOB;
        this.socialsecnum = ssn;
        this.address = address;
        this.phone = phone;
        this.emergency_contact = phone2;
        this.homeHospital = homeHospital;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public void setSocialsecnum(String socialsecnum) {
        this.socialsecnum = socialsecnum;
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

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getHomeHospital() {
        return homeHospital;
    }

    public void setHomeHospital(String homeHospital) {
        this.homeHospital = homeHospital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
