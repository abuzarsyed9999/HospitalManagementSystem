package com.hospital.model;

public class Doctor {
    private int doctorId;
    private String name;
    private String specialization;
    private String contact;
    private String department;

    // Default constructor
    public Doctor() {}

    // Constructor (without ID, since it's auto-generated)
    public Doctor(String name, String specialization, String contact, String department) {
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
        this.department = department;
    }

    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
 
    @Override
    public String toString() {
        return "Doctor{" +
                "ID=" + doctorId +
                ", Name='" + name + '\'' +
                ", Specialization='" + specialization + '\'' +
                ", Contact='" + contact + '\'' +
                ", Department='" + department + '\'' +
                '}';
    }
}