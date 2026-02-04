package com.hospital;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import java.util.List;
import java.util.Scanner;

public class DoctorMenu {
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void showDoctorMenu() {
        while (true) {
            System.out.println("\n========== DOCTOR MANAGEMENT ==========");
            System.out.println("1. Add New Doctor");
            System.out.println("2. View Doctor by ID");
            System.out.println("3. View All Doctors");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addDoctor();
                    break;
                case 2:
                    viewDoctorById();
                    break;
                case 3:
                    viewAllDoctors();
                    break;
                case 4:
                    updateDoctor();
                    break;
                case 5:
                    deleteDoctor();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void addDoctor() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine().trim();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Enter department: ");
        String department = scanner.nextLine().trim();

        Doctor doctor = new Doctor(name, specialization, contact, department);
        if (doctorDAO.addDoctor(doctor)) {
            System.out.println("✅ Doctor added successfully! Assigned ID: " + doctor.getDoctorId());
        } else {
            System.out.println("❌ Failed to add doctor.");
        }
    }

    private static void viewDoctorById() {
        System.out.print("Enter Doctor ID: ");
        int id = getIntInput();
        Doctor d = doctorDAO.getDoctorById(id);
        if (d != null) {
            System.out.println("📄 Doctor Details: " + d);
        } else {
            System.out.println("❌ Doctor not found with ID: " + id);
        }
    }

    private static void viewAllDoctors() {
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("📭 No doctors found.");
        } else {
            System.out.println("\n📋 All Doctors:");
            for (Doctor d : doctors) {
                System.out.println(d);
            }
        }
    }

    private static void updateDoctor() {
        System.out.print("Enter Doctor ID to update: ");
        int id = getIntInput();
        Doctor existing = doctorDAO.getDoctorById(id);
        if (existing == null) {
            System.out.println("❌ Doctor not found.");
            return;
        }

        System.out.println("Current details: " + existing);
        System.out.print("Enter new name (or press Enter to keep): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) existing.setName(name);

        System.out.print("Enter new specialization (or press Enter to keep): ");
        String specialization = scanner.nextLine().trim();
        if (!specialization.isEmpty()) existing.setSpecialization(specialization);

        System.out.print("Enter new contact (or press Enter to keep): ");
        String contact = scanner.nextLine().trim();
        if (!contact.isEmpty()) existing.setContact(contact);

        System.out.print("Enter new department (or press Enter to keep): ");
        String department = scanner.nextLine().trim();
        if (!department.isEmpty()) existing.setDepartment(department);

        if (doctorDAO.updateDoctor(existing)) {
            System.out.println("✅ Doctor updated successfully!");
        } else {
            System.out.println("❌ Failed to update doctor.");
        }
    }

    private static void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        int id = getIntInput();
        if (doctorDAO.deleteDoctor(id)) {
            System.out.println("🗑️ Doctor deleted successfully!");
        } else {
            System.out.println("❌ Failed to delete doctor. ID may not exist.");
        }
    }

    // Utility method to safely read integer input
    private static int getIntInput() {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return 0;
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.print("⚠️ Invalid number. Enter again: ");
            return getIntInput();
        }
    }
}