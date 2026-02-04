// File: com.hospital.ui.DoctorFrame.java
package com.hospital.ui;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtSpec, txtContact, txtDept;
    private DoctorDAO doctorDAO = new DoctorDAO();

    public DoctorFrame() {
        setTitle("👨‍⚕️ Doctor Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Doctor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(20);
        JLabel lblSpec = new JLabel("Specialization:");
        txtSpec = new JTextField(15);
        JLabel lblContact = new JLabel("Contact:");
        txtContact = new JTextField(15);
        JLabel lblDept = new JLabel("Department:");
        txtDept = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblSpec, gbc);
        gbc.gridx = 1; formPanel.add(txtSpec, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblContact, gbc);
        gbc.gridx = 1; formPanel.add(txtContact, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblDept, gbc);
        gbc.gridx = 1; formPanel.add(txtDept, gbc);

        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnSave); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        formPanel.add(btnPanel, new GridBagConstraints() {{
            gridx = 0; gridy = 4; gridwidth = 2; fill = HORIZONTAL;
        }});

        String[] columns = {"ID", "Name", "Specialization", "Contact", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadDoctors();

        btnSave.addActionListener(e -> saveDoctor());
        btnUpdate.addActionListener(e -> updateDoctor());
        btnDelete.addActionListener(e -> deleteDoctor());
        btnRefresh.addActionListener(e -> loadDoctors());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int sel = table.getSelectedRow();
                if (sel >= 0) {
                    txtName.setText(table.getValueAt(sel, 1).toString());
                    txtSpec.setText(table.getValueAt(sel, 2).toString());
                    txtContact.setText(table.getValueAt(sel, 3).toString());
                    txtDept.setText(table.getValueAt(sel, 4).toString());
                }
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadDoctors() {
        tableModel.setRowCount(0);
        for (Doctor d : doctorDAO.getAllDoctors()) {
            tableModel.addRow(new Object[]{d.getDoctorId(), d.getName(), d.getSpecialization(), d.getContact(), d.getDepartment()});
        }
    }

    private void saveDoctor() {
        try {
            String name = txtName.getText().trim();
            String spec = txtSpec.getText().trim();
            String contact = txtContact.getText().trim();
            String dept = txtDept.getText().trim();
            if (name.isEmpty()) throw new Exception("Name required");

            Doctor d = new Doctor(name, spec, contact, dept);
            if (doctorDAO.addDoctor(d)) {
                JOptionPane.showMessageDialog(this, " Doctor saved! ID: " + d.getDoctorId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDoctors();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Save failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Invalid input", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateDoctor() {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, " Select a doctor", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) table.getValueAt(sel, 0);
            String name = txtName.getText().trim();
            String spec = txtSpec.getText().trim();
            String contact = txtContact.getText().trim();
            String dept = txtDept.getText().trim();

            Doctor d = new Doctor(name, spec, contact, dept);
            d.setDoctorId(id);
            if (doctorDAO.updateDoctor(d)) {
                JOptionPane.showMessageDialog(this, " Doctor updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDoctors();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Invalid input", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDoctor() {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, " Select a doctor", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(sel, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete doctor ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (doctorDAO.deleteDoctor(id)) {
                JOptionPane.showMessageDialog(this, " Doctor deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDoctors();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "W Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtSpec.setText("");
        txtContact.setText("");
        txtDept.setText("");
        table.clearSelection();
    }
}