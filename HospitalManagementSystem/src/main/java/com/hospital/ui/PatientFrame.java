// File: com.hospital.ui.PatientFrame.java
package com.hospital.ui;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtAge, txtGender, txtContact, txtAddress;
    private PatientDAO patientDAO = new PatientDAO();

    public PatientFrame() {
        setTitle("Patient Management");
        setSize(900, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Register / Update Patient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(20);
        JLabel lblAge = new JLabel("Age:");
        txtAge = new JTextField(5);
        JLabel lblGender = new JLabel("Gender:");
        txtGender = new JTextField(10);
        JLabel lblContact = new JLabel("Contact:");
        txtContact = new JTextField(15);
        JLabel lblAddress = new JLabel("Address:");
        txtAddress = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblAge, gbc);
        gbc.gridx = 1; formPanel.add(txtAge, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblGender, gbc);
        gbc.gridx = 1; formPanel.add(txtGender, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblContact, gbc);
        gbc.gridx = 1; formPanel.add(txtContact, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblAddress, gbc);
        gbc.gridx = 1; formPanel.add(txtAddress, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnSave); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        formPanel.add(btnPanel, new GridBagConstraints() {{
            gridx = 0; gridy = 5; gridwidth = 2; fill = HORIZONTAL;
        }});

        // Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        loadPatients();

        // Events
        btnSave.addActionListener(e -> savePatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnRefresh.addActionListener(e -> loadPatients());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selected = table.getSelectedRow();
                if (selected >= 0) {
                    txtName.setText(table.getValueAt(selected, 1).toString());
                    txtAge.setText(table.getValueAt(selected, 2).toString());
                    txtGender.setText(table.getValueAt(selected, 3).toString());
                    txtContact.setText(table.getValueAt(selected, 4).toString());
                    txtAddress.setText(table.getValueAt(selected, 5).toString());
                }
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                p.getPatientId(), p.getName(), p.getAge(), p.getGender(),
                p.getContact(), p.getAddress()
            });
        }
    }

    private void savePatient() {
        try {
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = txtGender.getText().trim();
            String contact = txtContact.getText().trim();
            String address = txtAddress.getText().trim();

            if (name.isEmpty()) throw new Exception("Name required");

            Patient p = new Patient(name, age, gender, contact, address);
            if (patientDAO.addPatient(p)) {
                JOptionPane.showMessageDialog(this, "Patient saved! ID: " + p.getPatientId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPatients();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Save failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Invalid input: " + ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updatePatient() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, " Select a patient to update", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) table.getValueAt(selected, 0);
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = txtGender.getText().trim();
            String contact = txtContact.getText().trim();
            String address = txtAddress.getText().trim();

            Patient p = new Patient(name, age, gender, contact, address);
            p.setPatientId(id);
            if (patientDAO.updatePatient(p)) {
                JOptionPane.showMessageDialog(this, " Patient updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPatients();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Invalid input", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePatient() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, " Select a patient to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(selected, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete patient ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (patientDAO.deletePatient(id)) {
                JOptionPane.showMessageDialog(this, " Patient deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPatients();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        table.clearSelection();
    }
}