// File: com.hospital.ui.AppointmentFrame.java
package com.hospital.ui;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> cbPatient, cbDoctor;
    private JTextField txtDate, txtTime;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();

    public AppointmentFrame() {
        setTitle("📅 Appointment Management");
        setSize(950, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book / Cancel Appointment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblPatient = new JLabel("Patient:");
        cbPatient = new JComboBox<>();
        JLabel lblDoctor = new JLabel("Doctor:");
        cbDoctor = new JComboBox<>();
        JLabel lblDate = new JLabel("Date (yyyy-MM-dd):");
        txtDate = new JTextField(15);
        JLabel lblTime = new JLabel("Time (HH:mm:ss):");
        txtTime = new JTextField(15);

        loadPatientIds();
        loadDoctorIds();

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblPatient, gbc);
        gbc.gridx = 1; formPanel.add(cbPatient, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblDoctor, gbc);
        gbc.gridx = 1; formPanel.add(cbDoctor, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblDate, gbc);
        gbc.gridx = 1; formPanel.add(txtDate, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblTime, gbc);
        gbc.gridx = 1; formPanel.add(txtTime, gbc);

        JPanel btnPanel = new JPanel();
        JButton btnBook = new JButton("Book Appointment");
        JButton btnCancel = new JButton("Cancel Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnBook); btnPanel.add(btnCancel); btnPanel.add(btnRefresh);
        formPanel.add(btnPanel, new GridBagConstraints() {{
            gridx = 0; gridy = 4; gridwidth = 2; fill = HORIZONTAL;
        }});

        // Table
        String[] cols = {"ID", "Patient ID", "Doctor ID", "Date", "Time", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadAppointments();

        btnBook.addActionListener(e -> bookAppointment());
        btnCancel.addActionListener(e -> cancelAppointment());
        btnRefresh.addActionListener(e -> loadAppointments());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadPatientIds() {
        cbPatient.removeAllItems();
        for (Patient p : patientDAO.getAllPatients()) {
            cbPatient.addItem(p.getPatientId());
        }
    }

    private void loadDoctorIds() {
        cbDoctor.removeAllItems();
        for (Doctor d : doctorDAO.getAllDoctors()) {
            cbDoctor.addItem(d.getDoctorId());
        }
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        for (Appointment a : appointmentDAO.getAllAppointments()) {
            tableModel.addRow(new Object[]{
                a.getAppointmentId(), a.getPatientId(), a.getDoctorId(),
                a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus()
            });
        }
    }

    private void bookAppointment() {
        try {
            int patientId = (Integer) cbPatient.getSelectedItem();
            int doctorId = (Integer) cbDoctor.getSelectedItem();
            String dateStr = txtDate.getText().trim();
            String timeStr = txtTime.getText().trim();

            if (dateStr.isEmpty() || timeStr.isEmpty()) {
                throw new Exception("Date & Time required");
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            java.util.Date utilDate = df.parse(dateStr);
            Date sqlDate = new Date(utilDate.getTime());

            SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
            tf.setLenient(false);
            java.util.Date utilTime = tf.parse(timeStr);
            Time sqlTime = new Time(utilTime.getTime());

            Appointment a = new Appointment(patientId, doctorId, sqlDate, sqlTime);
            if (appointmentDAO.bookAppointment(a)) {
                JOptionPane.showMessageDialog(this, " Appointment booked! ID: " + a.getAppointmentId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAppointments();
                txtDate.setText("");
                txtTime.setText("");
            } else {
                JOptionPane.showMessageDialog(this, " Booking failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Invalid input: " + ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelAppointment() {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, " Select an appointment to cancel", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(sel, 0);
        String status = table.getValueAt(sel, 5).toString();
        if ("Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, " Already cancelled", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Cancel appointment ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (appointmentDAO.cancelAppointment(id)) {
                JOptionPane.showMessageDialog(this, " Appointment cancelled!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(this, " Cancel failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}