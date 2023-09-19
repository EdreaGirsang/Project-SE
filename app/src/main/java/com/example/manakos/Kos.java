package com.example.manakos;

public class Kos {

    String Name;
    String KID;
    int Available;

    public Kos(){}

    public Kos(String name, int available, String KID) {
        Name = name;
        Available = available;
        this.KID = KID;
    }

    public String getKID() {
        return KID;
    }

    public void setKID(String KID) {
        this.KID = KID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAvailable() {
        return Available;
    }

    public void setAvailable(int available) {
        Available = available;
    }
}
