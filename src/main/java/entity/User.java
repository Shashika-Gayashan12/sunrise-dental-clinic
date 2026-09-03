package com.sunrise.dentalclinic.entity;

public class User {


    private Long id;
    private String username;
    private String password;
    private String role;
    private String status;
    private Long dentistId;

    public User() {
    }

    public User(
            Long id,
            String username,
            String password,
            String role,
            String status) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.dentistId = null;
    }

    public User(
            Long id,
            String username,
            String password,
            String role,
            String status,
            Long dentistId) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.dentistId = dentistId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long dentistId) {
        this.dentistId = dentistId;
    }


}
