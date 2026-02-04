package com.hospital;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void showPatientMenu() {
        while (true) {
            System.out.println("\n========== PATIENT MANAGEMENT ==========");
            System.out.println("1. Register New Patient");
            System.out.println("2. View Patient by ID");
            System.out.println("3. View All Patients");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewPatientById();
                    break;
                case 3:
                    viewAllPatients();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    return; // Go back to main menu
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void registerPatient() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter age: ");
        int age = getIntInput();
        System.out.print("Enter gender (M/F/Other): ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Enter address: ");
        String address = scanner.nextLine().trim();

        Patient patient = new Patient(name, age, gender, contact, address);
        if (patientDAO.addPatient(patient)) {
            System.out.println("✅ Patient registered successfully! Assigned ID: " + patient.getPatientId());
        } else {
            System.out.println("❌ Failed to register patient.");
        }
    }

    private static void viewPatientById() {
        System.out.print("Enter Patient ID: ");
        int id = getIntInput();
        Patient p = patientDAO.getPatientById(id);
        if (p != null) {
            System.out.println("📄 Patient Details: " + p);
        } else {
            System.out.println("❌ Patient not found with ID: " + id);
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientDAO.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("📭 No patients found.");
        } else {
            System.out.println("\n📋 All Patients:");
            for (Patient p : patients) {
                System.out.println(p);
            }
        }
    }

    private static void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int id = getIntInput();
        Patient existing = patientDAO.getPatientById(id);
        if (existing == null) {
            System.out.println("❌ Patient not found.");
            return;
        }

        System.out.println("Current details: " + existing);
        System.out.print("Enter new name (or press Enter to keep): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) existing.setName(name);

        System.out.print("Enter new age (or 0 to keep current): ");
        int age = getIntInput();
        if (age > 0) existing.setAge(age);

        System.out.print("Enter new gender (or press Enter to keep): ");
        String gender = scanner.nextLine().trim();
        if (!gender.isEmpty()) existing.setGender(gender);

        System.out.print("Enter new contact (or press Enter to keep): ");
        String contact = scanner.nextLine().trim();
        if (!contact.isEmpty()) existing.setContact(contact);

        System.out.print("Enter new address (or press Enter to keep): ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) existing.setAddress(address);

        if (patientDAO.updatePatient(existing)) {
            System.out.println(" Patient updated successfully!");
        } else {
            System.out.println(" Failed to update patient.");
        }
    }

    private static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        int id = getIntInput();
        if (patientDAO.deletePatient(id)) {
            System.out.println(" Patient deleted successfully!");
        } else {
            System.out.println(" Failed to delete patient. ID may not exist.");
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
            return getIntInput(); // Recursive retry
        }
    }
}