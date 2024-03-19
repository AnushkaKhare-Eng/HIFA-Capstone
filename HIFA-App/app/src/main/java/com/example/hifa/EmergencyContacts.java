package com.example.hifa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmergencyContacts implements Serializable {
    private String name;
    private  String phone;
    private Map<String, String> emergencyContactmap;

    public EmergencyContacts(String name, String phone){

        emergencyContactmap = new HashMap<>();
        emergencyContactmap.put(name,phone);
        this.emergencyContactmap = emergencyContactmap;
    }

    public List<String> getPhoneNumber( ) {
        List<String> phoneNumList = new ArrayList<>(emergencyContactmap.values());
        return phoneNumList;

    }

    public List<String> getNames() {
        List<String> namesList = new ArrayList<>(emergencyContactmap.keySet());
        return namesList;

    }
    public void addContactInfo(String nameNew, String phoneNumNew) {

        emergencyContactmap.put(nameNew,phoneNumNew);
        this.emergencyContactmap = emergencyContactmap;
    }
    // need to update the information

    public void updateContactInfo(String nameNew, String phoneNumNew) {

        if (emergencyContactmap.containsKey(nameNew)) {
            emergencyContactmap.put(nameNew, phoneNumNew);
            this.emergencyContactmap = emergencyContactmap;
        }
    }

    public void deleteContactInfo(String name, String phoneNum) {
        if (emergencyContactmap.containsKey(name)) {
            emergencyContactmap.remove(name, phoneNum);
            this.emergencyContactmap = emergencyContactmap;
        }
    }

}
