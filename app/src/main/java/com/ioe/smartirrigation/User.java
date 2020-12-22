package com.ioe.smartirrigation;

public class User {

    public String soiltemp;
    public int soilmoisture;
    public String soilhumidity;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String soilhumidity, String soiltemp, int soilmoisture) {
        this.soilhumidity = soilhumidity;
        this.soiltemp = soiltemp;
        this.soilmoisture = soilmoisture;
    }

}