package com.evans.pillreminder.helpers;

public class Appointment {
    String firstName, lastName, description;
    //    Date appointmentDateTime;
    String appointmentDate, appointmentTime;

    public Appointment(String firstName, String lastName, String description, String appointmentDate, String appointmentTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }


}
