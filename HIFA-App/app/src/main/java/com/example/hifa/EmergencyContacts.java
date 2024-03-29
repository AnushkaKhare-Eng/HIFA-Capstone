package com.example.hifa;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmergencyContacts implements Serializable {
    private String name;
    private  String phone;
    private Map<String, String> emergencyContactmap;
    private List<EmergencyContact> emergencyContactsList;

    public EmergencyContacts() {
        emergencyContactmap = new HashMap<>();
        emergencyContactsList = new ArrayList<EmergencyContact>();
    }

    public EmergencyContacts(String name, String phone) {
        this.name = name;
        this.phone = phone;
        emergencyContactmap = new HashMap<>();
        emergencyContactsList = new ArrayList<EmergencyContact>();
    }

    public Map<String, String> getEmergencyContactmap() {
        return emergencyContactmap;
    }

    public List<EmergencyContact> getEmergencyContactsList() {
        return emergencyContactsList;
    }

//    public Map<String, String> checkInputMap(String phoneNumberString, String nameString){
//        if (emergencyContactmap.containsKey(nameString)) {
//            // Update the value associated with the key
//            emergencyContactmap.put(keyToUpdate, "newValue");
//            emergencyContacts.updateContactInfo(keyToUpdate,"newValue");
//
//
//        } else {
//            tempMap.put(keyToUpdate, "newValue");
//            emergencyContacts.addContactInfo(keyToUpdate,"newValue");
//
//        }
//    }

    public void setEmergencyContactmap(Map<String, String> emergencyContactmap) {
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

        emergencyContactsList.add(new EmergencyContact(nameNew, phoneNumNew));
    }
    // need to update the information

    public void updateContactInfo(String nameNew, String phoneNumNew) {

        if (emergencyContactmap.containsKey(nameNew)) {
            emergencyContactmap.put(nameNew, phoneNumNew);
        }
    }

    public void deleteContactInfo(String name, String phoneNum) {
        if (emergencyContactmap.size()>1){
            if (emergencyContactmap.containsKey(name)) {
                emergencyContactmap.remove(name, phoneNum);
            }
        }
    }

}
