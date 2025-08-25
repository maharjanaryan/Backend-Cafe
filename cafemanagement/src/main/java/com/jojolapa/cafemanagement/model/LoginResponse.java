package com.jojolapa.cafemanagement.model;

public class LoginResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public LoginResponse(Long id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}