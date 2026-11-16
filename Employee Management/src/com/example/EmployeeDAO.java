package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/company_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean addEmployee(int empId, String name, String department, double salary) throws SQLException {
        String sql = "INSERT INTO employees (emp_id, name, department, salary) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empId);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setDouble(4, salary);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static List<Object[]> getAllEmployees() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT emp_id, name, department, salary FROM employees";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("emp_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getDouble("salary")
                });
            }
        }
        return list;
    }
}
