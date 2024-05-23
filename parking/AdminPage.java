package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPage extends JFrame implements ActionListener {
    private JButton viewBookedSlotsButton;
    private JButton viewCustomerDetailsButton;
    private JButton logoutButton;

    public AdminPage() {
        setTitle("Admin Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 526);
        setLocationRelativeTo(null);

        // Create a JLabel with the background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\ANEESH MINJ\\Downloads\\PARK-ZILLA (2).png"));
        backgroundLabel.setLayout(new GridBagLayout());

        // Create buttons
        viewBookedSlotsButton = new JButton("View Booked Slots");
        viewCustomerDetailsButton = new JButton("View Customer Details");
        logoutButton = new JButton("Logout");

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(150, 40);
        viewBookedSlotsButton.setPreferredSize(buttonSize);
        viewCustomerDetailsButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        // Add action listeners
        viewBookedSlotsButton.addActionListener(this);
        viewCustomerDetailsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add buttons to the background label
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Padding
        backgroundLabel.add(viewBookedSlotsButton, gbc);

        gbc.gridy = 1;
        backgroundLabel.add(viewCustomerDetailsButton, gbc);

        gbc.gridy = 2;
        backgroundLabel.add(logoutButton, gbc);

        // Set the background label as the content pane
        setContentPane(backgroundLabel);

        // Make the frame visible
        setVisible(true);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewBookedSlotsButton) {
            // Show dialog to select the vehicle type (two-wheeler or four-wheeler)
            String[] options = {"Two-Wheeler", "Four-Wheeler"};
            int choice = JOptionPane.showOptionDialog(this, "Select vehicle type:", "Select Vehicle Type", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            // Based on the selected option, call the corresponding method to view booked slots
            if (choice == 0) {
                // View booked slots for two-wheelers
                viewBookedSlots("booked_slots");
            } else if (choice == 1) {
                // View booked slots for four-wheelers
                viewBookedSlots("booked_slots_four_wheelers");
            }
        } else if (e.getSource() == viewCustomerDetailsButton) {
            // View customer details
            viewCustomerDetails();
        } else if (e.getSource() == logoutButton) {
            // Logout
            JOptionPane.showMessageDialog(this, "Logged out");
            // Perform logout actions here, such as closing the current window and opening the login page
            // For simplicity, I'll just close the admin page
            dispose(); // Close the admin page
        }
    }


    private void viewBookedSlots(String tableName) {
        // Execute MySQL query to retrieve booked slots data
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/parking_management_system", "root", "root");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = statement.executeQuery();

            // Process the ResultSet (display data or further processing)
            // Example: Display booked slots data in a dialog
            StringBuilder slotsData = new StringBuilder();
            while (resultSet.next()) {
                slotsData.append("Slot ID: ").append(resultSet.getInt("slot_id")).append(", Customer ID: ").append(resultSet.getInt("customer_id")).append("\n");
            }
            JOptionPane.showMessageDialog(this, slotsData.toString(), "Booked Slots", JOptionPane.INFORMATION_MESSAGE);

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving booked slots data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void viewCustomerDetails() {
        // Execute MySQL query to retrieve all customer details data
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/parking_management_system", "root", "root");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = statement.executeQuery();

            // Process the ResultSet (display data or further processing)
            // Example: Display customer details data in a dialog
            StringBuilder customerData = new StringBuilder();
            while (resultSet.next()) {
                customerData.append("Customer ID: ").append(resultSet.getInt("customer_id"))
                        .append(", Email: ").append(resultSet.getString("email"))
                        .append(", Phone Number: ").append(resultSet.getString("phone_number"))
                        .append(", Two Wheeler Car No: ").append(resultSet.getString("two_wheeler_car_no"))
                        .append(", Four Wheeler Car No: ").append(resultSet.getString("four_wheeler_car_no"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, customerData.toString(), "Customer Details", JOptionPane.INFORMATION_MESSAGE);

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving customer details data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void createDatabaseAndTables() {
        try {
            // Connect to MySQL (assuming MySQL server is running locally on port 3306)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/", "root", "root");

            // Create the database
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS parking_management_system");
            statement.close();

            // Use the database
            statement = connection.createStatement();
            statement.execute("USE parking_management_system");
            statement.close();

            // Create tables
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS booked_slots (" +
                    "slot_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "customer_id INT," +
                    "slot_number INT," +
                    "booking_time DATETIME," +
                    "duration INT," +
                    "cost DECIMAL(10, 2)" +
                    ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customer_details (" +
                    "customer_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100)," +
                    "email VARCHAR(100)," +
                    "phone_number VARCHAR(20)" +
                    ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS cost_table (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "cost DECIMAL(10, 2)" +
                    ")");

            // Insert sample data into cost_table (assuming only one row for the cost)
            statement.executeUpdate("INSERT INTO cost_table (cost) VALUES (10.00)");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminPage adminPage = new AdminPage();
            adminPage.setVisible(true);
        });
    }
}
