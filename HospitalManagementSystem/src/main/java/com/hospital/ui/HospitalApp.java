// File: com.hospital.ui.HospitalApp.java
package com.hospital.ui;

import javax.swing.*;
import java.awt.*;

public class HospitalApp extends JFrame {
    public HospitalApp() {
        setTitle(" Hospital Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(45, 118, 232));
        header.setPreferredSize(new Dimension(900, 80));
        JLabel title = new JLabel("Hospital Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Buttons
        JPanel buttons = new JPanel(new GridLayout(2, 2, 25, 25));
        buttons.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));

        JButton btnPatient = createButton(" Patient Management", new Color(70, 130, 180));
        btnPatient.addActionListener(e -> new PatientFrame().setVisible(true));
        buttons.add(btnPatient);

        JButton btnDoctor = createButton(" Doctor Management", new Color(50, 150, 100));
        btnDoctor.addActionListener(e -> new DoctorFrame().setVisible(true));
        buttons.add(btnDoctor);

        JButton btnAppointment = createButton(" Appointment Booking", new Color(180, 100, 70));
        btnAppointment.addActionListener(e -> new AppointmentFrame().setVisible(true));
        buttons.add(btnAppointment);

        JButton btnExit = createButton(" Exit", new Color(200, 60, 60));
        btnExit.addActionListener(e -> System.exit(0));
        buttons.add(btnExit);

        add(buttons, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(" 2026 | Core Java + JDBC | SYED ABUZAR", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HospitalApp();
        });
    }
}