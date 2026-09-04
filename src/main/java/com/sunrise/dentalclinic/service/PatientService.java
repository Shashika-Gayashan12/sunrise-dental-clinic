package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.repository.PatientRepository;

import java.sql.SQLException;
import java.util.List;

public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService() {
        this.patientRepository = new PatientRepository();
    }

    public Patient addPatient(Patient patient) throws SQLException {

        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (patient.getPatientName() == null ||
                patient.getPatientName().isBlank()) {
            throw new IllegalArgumentException("Patient name is required");
        }

        if (patient.getAddress() == null ||
                patient.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }

        if (patient.getContactNumber() == null ||
                patient.getContactNumber().isBlank()) {
            throw new IllegalArgumentException("Contact number is required");
        }

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid patient ID");
        }

        return patientRepository.findById(id);
    }
}