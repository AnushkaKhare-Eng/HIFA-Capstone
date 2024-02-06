package com.example.hifa;

import java.io.Serializable;

public class User implements Serializable {

    private String email;
    private  String password;
    private float age;
    private int driversLicense;
    private int healthcard;
    private String firstname;
    private String lastname;

    public User (String email, String password, String firstname, String lastname, float age, int healthcard, int driversLicense ){
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.healthcard=healthcard;
        this.driversLicense = driversLicense;
    }

// adding getters and setters for each of the attribute
    public String getEmail(){return email;}
    public String getPassword(){return password;}

    public String getFirstname(){return firstname;}

    public String getLastname() {
        return lastname;
    }

    public int getHealthcard() {
        return healthcard;
    }

    public float getAge() {
        return age;
    }

    public int getDriversLicense() {
        return driversLicense;
    }
    // setters


    public void setAge(float age) {
        this.age = age;
    }

    public void setDriversLicense(int driversLicense) {
        this.driversLicense = driversLicense;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setHealthcard(int healthcard) {
        this.healthcard = healthcard;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
