package com.example.hifa;

public class EmergencyContacts {
    private String name;
    private float phoneNumber;

    public EmergencyContacts(String name, float phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public float getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(float phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
