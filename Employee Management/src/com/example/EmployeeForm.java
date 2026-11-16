package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class EmployeeForm extends JFrame {
    private JTextField txtEmpId;
    private JTextField txtName;
    private JTextField txtDepartment;
    private JTextField txtSalary;
    private JButton btnSubmit;
    private JButton btnViewAll;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeForm() {
        setTitle("Employee Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        formPanel.add(new JLabel("Employee ID:"));
        txtEmpId = new JTextField();
        formPanel.add(txtEmpId);

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Department:"));
        txtDepartment = new JTextField();
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Salary:"));
        txtSalary = new JTextField();
        formPanel.add(txtSalary);

        btnSubmit = new JButton("Submit");
        btnViewAll = new JButton("View All Employees");
        formPanel.add(btnSubmit);
        formPanel.add(btnViewAll);

        String[] columns = {"Employee ID", "Name", "Department", "Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEmployees();
            }
        });
    }

    private void saveEmployee() {
        String idText = txtEmpId.getText().trim();
        String name = txtName.getText().trim();
        String department = txtDepartment.getText().trim();
        String salaryText = txtSalary.getText().trim();

        if (idText.isEmpty() || name.isEmpty() || department.isEmpty() || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int empId = Integer.parseInt(idText);
            double salary = Double.parseDouble(salaryText);

            boolean success = EmployeeDAO.addEmployee(empId, name, department, salary);
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                clearFields();
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID and Salary.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        try {
            List<Object[]> list = EmployeeDAO.getAllEmployees();
            for (Object[] row : list) {
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtEmpId.setText("");
        txtName.setText("");
        txtDepartment.setText("");
        txtSalary.setText("");
    }

    public static void main(String[] args) {
        new EmployeeForm().setVisible(true);
    }
}
