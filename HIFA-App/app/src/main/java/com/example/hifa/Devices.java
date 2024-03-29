
        package com.example.hifa;

        import java.io.Serializable;

public class Devices implements Serializable {
    private String userEmail;
    private long deviceId;

    // Constructor
    public Devices(String userEmail, long deviceId) {
        this.userEmail = userEmail;
        this.deviceId = deviceId;
    }

    // Getters and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setDeviceName(String deviceName) {
        this.userEmail = userEmail;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
}
