package com.sunrise.dentalclinic.entity;

public class Patient {

    private Long id;
    private String patientName;
    private String address;
    private String contactNumber;

    public Patient() {
    }

    public Patient(Long id, String patientName, String address, String contactNumber) {
        this.id = id;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Patient(String patientName, String address, String contactNumber) {
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}