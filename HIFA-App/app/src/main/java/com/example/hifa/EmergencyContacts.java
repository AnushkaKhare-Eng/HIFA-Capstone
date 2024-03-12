package com.example.hifa;

import java.util.List;

public class EmergencyContacts {
    private List<String> nameList;
    private  List<String> phoneList;

    public EmergencyContacts(List<String> nameList, List<String> phoneList){
        this.nameList = nameList;
        this.phoneList = phoneList;
    }

    public List<String> getPhoneNumber() {
        return phoneList;
    }

    public List<String> getNames() {
        return nameList;
    }

    public String getName() {
        return nameList.get(0);
    }

    public void updateName(String name) {

        nameList.add(name);
        this.nameList = nameList;
    }

    public void updatePhoneNumber(String phoneNumber) {
        phoneList.add(phoneNumber);

        this.phoneList = phoneList;
    }
}
