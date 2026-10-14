package com.lifematch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;


    // EMPTY CONSTRUCTOR
    public User() {
    }


    // CONSTRUCTOR
    public User(
            String username,
            String password,
            String role) {

        this.username = username;
        this.password = password;
        this.role = role;
    }


    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    // USERNAME
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // PASSWORD
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // ROLE
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
