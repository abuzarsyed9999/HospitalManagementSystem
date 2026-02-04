package com.hospital;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Appointment;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class AppointmentMenu {
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static void showAppointmentMenu() {
        while (true) {
            System.out.println("\n========== APPOINTMENT MANAGEMENT ==========");
            System.out.println("1. Book New Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. View Appointments by Patient ID");
            System.out.println("4. View Appointments by Doctor ID");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    viewAllAppointments();
                    break;
                case 3:
                    viewAppointmentsByPatient();
                    break;
                case 4:
                    viewAppointmentsByDoctor();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void bookAppointment() {
        // Step 1: Get and validate Patient ID
        System.out.print("Enter Patient ID: ");
        int patientId = getIntInput();
        if (patientDAO.getPatientById(patientId) == null) {
            System.out.println("❌ Patient not found with ID: " + patientId);
            return;
        }

        // Step 2: Get and validate Doctor ID
        System.out.print("Enter Doctor ID: ");
        int doctorId = getIntInput();
        if (doctorDAO.getDoctorById(doctorId) == null) {
            System.out.println("❌ Doctor not found with ID: " + doctorId);
            return;
        }

        // Step 3: Get and parse Date
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine().trim();
        Date appointmentDate;
        try {
            java.util.Date parsedDate = DATE_FORMAT.parse(dateStr);
            appointmentDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            System.out.println("❌ Invalid date format. Use yyyy-MM-dd (e.g., 2026-03-15)");
            return;
        }

        // Step 4: Get and parse Time
        System.out.print("Enter Appointment Time (HH:mm:ss): ");
        String timeStr = scanner.nextLine().trim();
        Time appointmentTime;
        try {
            java.util.Date parsedTime = TIME_FORMAT.parse(timeStr);
            appointmentTime = new Time(parsedTime.getTime());
        } catch (ParseException e) {
            System.out.println("❌ Invalid time format. Use HH:mm:ss (e.g., 14:30:00)");
            return;
        }

        // Step 5: Create and save appointment
        Appointment appointment = new Appointment(patientId, doctorId, appointmentDate, appointmentTime);
        if (appointmentDAO.bookAppointment(appointment)) {
            System.out.println("✅ Appointment booked successfully! ID: " + appointment.getAppointmentId());
        } else {
            System.out.println("❌ Failed to book appointment. Check IDs and try again.");
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("📭 No appointments found.");
        } else {
            System.out.println("\n📋 All Appointments:");
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void viewAppointmentsByPatient() {
        System.out.print("Enter Patient ID: ");
        int patientId = getIntInput();
        if (patientDAO.getPatientById(patientId) == null) {
            System.out.println("❌ Patient not found.");
            return;
        }
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatientId(patientId);
        if (appointments.isEmpty()) {
            System.out.println("📭 No appointments found for this patient.");
        } else {
            System.out.println("\n📋 Appointments for Patient ID " + patientId + ":");
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void viewAppointmentsByDoctor() {
        System.out.print("Enter Doctor ID: ");
        int doctorId = getIntInput();
        if (doctorDAO.getDoctorById(doctorId) == null) {
            System.out.println("❌ Doctor not found.");
            return;
        }
        List<Appointment> appointments = appointmentDAO.getAppointmentsByDoctorId(doctorId);
        if (appointments.isEmpty()) {
            System.out.println("📭 No appointments found for this doctor.");
        } else {
            System.out.println("\n📋 Appointments for Doctor ID " + doctorId + ":");
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    private static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        int appointmentId = getIntInput();
        Appointment existing = appointmentDAO.getAppointmentById(appointmentId);
        if (existing == null) {
            System.out.println("❌ Appointment not found.");
            return;
        }
        if ("Cancelled".equals(existing.getStatus())) {
            System.out.println("ℹ️ Appointment is already cancelled.");
            return;
        }
        if (appointmentDAO.cancelAppointment(appointmentId)) {
            System.out.println("🗑️ Appointment ID " + appointmentId + " has been cancelled.");
        } else {
            System.out.println("❌ Failed to cancel appointment.");
        }
    }

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