package com.evans.pillreminder.helpers;

public class MedicineDosage {
    private String pillName;
    private int pillDosage;

    public MedicineDosage(String pillName, int pillDosage) {
        this.pillName = pillName;
        this.pillDosage = pillDosage;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getPillDosage() {
        return pillDosage;
    }

    public void setPillDosage(int pillDosage) {
        this.pillDosage = pillDosage;
    }
}
