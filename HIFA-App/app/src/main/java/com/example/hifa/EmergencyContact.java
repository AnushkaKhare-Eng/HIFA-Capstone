package com.example.hifa;

import java.io.Serializable;

public class EmergencyContact implements Serializable {

    private String name;
    private String phoneNo;

    public EmergencyContact(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
