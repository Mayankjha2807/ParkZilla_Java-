package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignupPage extends JFrame implements ActionListener {
    private JTextField txtCustomerId;
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JTextField txtTwoWheelerCarNo;
    private JTextField txtFourWheelerCarNo;
    private JButton btnSignUp;
    private JButton btnLogin; // Added login button

    private Connection connection;

    public SignupPage() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background Image
        ImageIcon backgroundImage = new ImageIcon("C:\\\\Users\\\\ANEESH MINJ\\\\Downloads\\\\PARK-ZILLA (2).png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Panel for signup fields
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblCustomerId = new JLabel("Customer ID:");
        lblCustomerId.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblCustomerId, gbc);

        txtCustomerId = new JTextField(15);
        txtCustomerId.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtCustomerId, gbc);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblPassword, gbc);

        pwdPassword = new JPasswordField(15);
        pwdPassword.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(pwdPassword, gbc);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblEmail, gbc);

        txtEmail = new JTextField(15);
        txtEmail.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtEmail, gbc);

        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblPhoneNumber, gbc);

        txtPhoneNumber = new JTextField(15);
        txtPhoneNumber.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtPhoneNumber, gbc);

        JLabel lblTwoWheelerCarNo = new JLabel("Two Wheeler Car No:");
        lblTwoWheelerCarNo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblTwoWheelerCarNo, gbc);

        txtTwoWheelerCarNo = new JTextField(15);
        txtTwoWheelerCarNo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtTwoWheelerCarNo, gbc);

        JLabel lblFourWheelerCarNo = new JLabel("Four Wheeler Car No:");
        lblFourWheelerCarNo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(lblFourWheelerCarNo, gbc);

        txtFourWheelerCarNo = new JTextField(15);
        txtFourWheelerCarNo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        signupPanel.add(txtFourWheelerCarNo, gbc);

        btnSignUp = new JButton("Sign Up");
        signupPanel.add(btnSignUp, gbc);

        btnLogin = new JButton("Login"); // Create login button
        signupPanel.add(btnLogin, gbc);

        add(signupPanel, BorderLayout.CENTER);

        btnSignUp.addActionListener(this);
        btnLogin.addActionListener(this); // Add action listener for login button

        setSize(1000, 526); // Set dimension
        setLocationRelativeTo(null);

        connectToDatabase();
        setVisible(true);
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3307/parking_management_system";
        String username = "root";
        String password = "root";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignUp) {
            signUp();
        } else if (e.getSource() == btnLogin) { // Handle login button action
            openLoginPage();
        }
    }

    private void signUp() {
        int customerId = Integer.parseInt(txtCustomerId.getText());
        String username = txtUsername.getText();
        String password = String.valueOf(pwdPassword.getPassword());
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String twoWheelerCarNo = txtTwoWheelerCarNo.getText();
        String fourWheelerCarNo = txtFourWheelerCarNo.getText();

        try {
            // Check if the customer ID already exists
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM customer WHERE customer_id = ?");
            checkStatement.setInt(1, customerId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Customer ID already exists. Please choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // If customer ID doesn't exist, proceed with sign-up
                insertCustomerAndLogin(customerId, username, password, email, phoneNumber, twoWheelerCarNo, fourWheelerCarNo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while attempting to sign up.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertCustomerAndLogin(int customerId, String username, String password, String email, String phoneNumber, String twoWheelerCarNo, String fourWheelerCarNo) throws SQLException {
        PreparedStatement insertCustomerStatement = connection.prepareStatement("INSERT INTO customer (customer_id, email, phone_number, two_wheeler_car_no, four_wheeler_car_no) VALUES (?, ?, ?, ?, ?)");
        insertCustomerStatement.setInt(1, customerId);
        insertCustomerStatement.setString(2, email);
        insertCustomerStatement.setString(3, phoneNumber);
        insertCustomerStatement.setString(4, twoWheelerCarNo);
        insertCustomerStatement.setString(5, fourWheelerCarNo);
        insertCustomerStatement.executeUpdate();

        insertLoginCredentials(customerId, username, password);
    }

    private void insertLoginCredentials(int customerId, String username, String password) throws SQLException {
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO login (customer_id, username, password) VALUES (?, ?, ?)");
        insertStatement.setInt(1, customerId);
        insertStatement.setString(2, username);
        insertStatement.setString(3, password);
        insertStatement.executeUpdate();

        JOptionPane.showMessageDialog(this, "Sign Up Successful!");
    }

    private void openLoginPage() {
        // Open login page
        dispose(); // Close current window
        new LoginSignupPage(); // Open login page
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupPage::new);
    }
}
