package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserPage extends JFrame implements ActionListener {
    private JButton viewBookingsButton;
    private JButton bookTwoWheelerButton;
    private JButton bookFourWheelerButton;
    private JButton logoutButton;

    // MySQL connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/parking_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private int customerId; // Field to store customer ID

    public UserPage(int customerId) {
        this.customerId = customerId; // Store the customer ID
        setTitle("User Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 526); // Set a smaller size
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a JPanel with a BorderLayout to hold the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setOpaque(false); // Make the panel transparent
        setContentPane(new JLabel(new ImageIcon("C:\\Users\\ANEESH MINJ\\Downloads\\PARK-ZILLA (2).png"))); // Corrected path

        // Create buttons
        viewBookingsButton = createButton("View My Bookings");
        bookTwoWheelerButton = createButton("Book Two Wheeler Slot");
        bookFourWheelerButton = createButton("Book Four Wheeler Slot");
        logoutButton = createButton("Logout");

        // Add action listeners
        viewBookingsButton.addActionListener(this);
        bookTwoWheelerButton.addActionListener(this);
        bookFourWheelerButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add buttons to the button panel
        buttonPanel.add(viewBookingsButton);
        buttonPanel.add(bookTwoWheelerButton);
        buttonPanel.add(bookFourWheelerButton);
        buttonPanel.add(logoutButton);

        // Add the button panel to the content pane
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        getContentPane().add(buttonPanel, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
        button.setPreferredSize(new Dimension(200, 40)); // Set preferred size
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewBookingsButton) {
            // Display the MySQL query for viewing bookings
            String query = "SELECT * FROM booked_slots WHERE customer_id = ?;";

            // Execute the SQL query and fetch the results
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set the customer ID parameter based on the logged-in customer
                statement.setInt(1, customerId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Process the ResultSet and construct a string to display the results
                    StringBuilder resultStringBuilder = new StringBuilder();
                    while (resultSet.next()) {
                        int slotId = resultSet.getInt("slot_id");
                        int customerId = resultSet.getInt("customer_id");
                        // Add more fields as needed
                        resultStringBuilder.append("Slot ID: ").append(slotId).append(", Customer ID: ").append(customerId).append("\n");
                    }

                    // Display the results
                    JOptionPane.showMessageDialog(this, "MySQL Query Result:\n" + resultStringBuilder.toString());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving booking data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == bookTwoWheelerButton) {
            TwoWheelers twoWheelers = new TwoWheelers();
            twoWheelers.frame.setVisible(true);
            dispose();
        } else if (e.getSource() == bookFourWheelerButton) {
            FourWheelers fourWheelers = new FourWheelers();
            fourWheelers.frame.setVisible(true);
            dispose();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }

    private void logout() {
        // Close the database connection
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dispose(); // Dispose the current frame
    }

    public static void main(String[] args) {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Assuming you have obtained the customer ID from the login/signup process
        int customerId = 1; // Get the customer ID from the login/signup process

        // Create and show the user page
        SwingUtilities.invokeLater(() -> {
            UserPage userPage = new UserPage(customerId);
            userPage.setVisible(true);
        });
    }
}
