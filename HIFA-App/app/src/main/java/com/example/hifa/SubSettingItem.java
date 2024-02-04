package com.example.hifa;

public class SubSettingItem {

    String setting_title;
    int image;

    public SubSettingItem(String setting_name, int image) {
        this.setting_title = setting_name;
        this.image = image;
    }

    public String getSetting_title() {
        return setting_title;
    }

    public int getImage() {
        return image;
    }

    public void setSetting_title(String setting_title) {
        this.setting_title = setting_title;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
