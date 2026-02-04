package com.hospital;

import java.util.Scanner;
import com.hospital.PatientMenu;
import com.hospital.DoctorMenu;
import com.hospital.AppointmentMenu; 

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(" Welcome to Hospital Management System!");
        while (true) {
            showMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    PatientMenu.showPatientMenu();
                    break;
                case 2:
                    DoctorMenu.showDoctorMenu();
                    break;
                case 3:
                    AppointmentMenu.showAppointmentMenu(); // ← Now active!
                    break;
                case 4:
                    System.out.println(" Thank you for using the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println(" Invalid option. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print(" Invalid number. Enter again: ");
            return getIntInput();
        }
    }
}