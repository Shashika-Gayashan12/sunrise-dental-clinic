package com.sunrise.dentalclinic.entity;

public class Dentist {

    private Long id;
    private String dentistName;
    private String specialization;
    private String contactNumber;

    public Dentist() {
    }

    public Dentist(Long id,
                   String dentistName,
                   String specialization,
                   String contactNumber) {

        this.id = id;
        this.dentistName = dentistName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
    }

    public Dentist(String dentistName,
                   String specialization,
                   String contactNumber) {

        this.dentistName = dentistName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Dentist{" +
                "id=" + id +
                ", dentistName='" + dentistName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}