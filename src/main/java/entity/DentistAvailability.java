package com.sunrise.dentalclinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class DentistAvailability {

    private Long id;
    private Long dentistId;
    private String dayOfWeek;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public DentistAvailability() {
    }

    // Used when creating a new availability
    public DentistAvailability(
            Long dentistId,
            String dayOfWeek,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime) {

        this.dentistId = dentistId;
        this.dayOfWeek = dayOfWeek;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Used when loading availability from database
    public DentistAvailability(
            Long id,
            Long dentistId,
            String dayOfWeek,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime) {

        this.id = id;
        this.dentistId = dentistId;
        this.dayOfWeek = dayOfWeek;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long dentistId) {
        this.dentistId = dentistId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}