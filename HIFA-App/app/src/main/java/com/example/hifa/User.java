package com.example.hifa;

import android.os.Parcelable;

import androidx.annotation.Keep;

import java.io.Serializable;

public class User implements Serializable {

    private String email;
    private  String password;
    private float age;
    private String driversLicense;
    private String phoneNumber;
    private String healthcard;
    private String firstname;
    private String lastname;

    @Keep
    public User() {
        // Default constructor logic, if needed
    }
    public User (String email, String password, String firstname, String lastname, int age, String healthcard, String driversLicense, String phoneNumber ){
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.healthcard=healthcard;
        this.driversLicense = driversLicense;
        this.phoneNumber = phoneNumber;
    }

    // adding getters and setters for each of the attribute
    public String getEmail(){return email;}
    public String getPassword(){return password;}

    public String getFirstname(){return firstname;}

    public String getLastname() {
        return lastname;
    }

    public String getHealthcard() {
        return healthcard;
    }

    public float getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDriversLicense() {
        return driversLicense;
    }
    // setters


    public void setAge(int age) {
        this.age = age;
    }

    public void setDriversLicense(String driversLicense) {
        this.driversLicense = driversLicense;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setHealthcard(String healthcard) {
        this.healthcard = healthcard;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
