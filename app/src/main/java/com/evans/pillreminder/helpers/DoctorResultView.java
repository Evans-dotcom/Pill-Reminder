package com.evans.pillreminder.helpers;

import com.evans.pillreminder.R;

public class DoctorResultView {
    String firstName, lastName, doctorUID, doctorToken;
    int doctorImage = R.drawable.google_color;

    public DoctorResultView(String firstName, String lastName, String doctorUID, String doctorToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.doctorUID = doctorUID;
        this.doctorToken = doctorToken;
    }

    public DoctorResultView(String firstName, String lastName, String doctorUID, String doctorToken, int doctorImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.doctorUID = doctorUID;
        this.doctorToken = doctorToken;
        this.doctorImage = doctorImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDoctorUID() {
        return doctorUID;
    }

    public void setDoctorUID(String doctorUID) {
        this.doctorUID = doctorUID;
    }

    public String getDoctorToken() {
        return doctorToken;
    }

    public void setDoctorToken(String doctorToken) {
        this.doctorToken = doctorToken;
    }

    public int getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(int doctorImage) {
        this.doctorImage = doctorImage;
    }
}
