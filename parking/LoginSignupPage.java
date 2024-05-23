package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginSignupPage extends JFrame implements ActionListener {
    private JTextField txtCustomerId;
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JButton btnLogin;
    private JButton btnSignUp; // Button for signup

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSignupPage::new);
    }

    public LoginSignupPage() {
        setTitle("Login / Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background Image
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\ANEESH MINJ\\Downloads\\PARK-ZILLA (2).png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Panel for login/signup fields
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblCustomerId = new JLabel("Customer ID:");
        lblCustomerId.setForeground(new Color(133, 232, 235));; // Setting text color
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblCustomerId, gbc);

        txtCustomerId = new JTextField(15);
        txtCustomerId.setForeground(new Color(0, 0, 0));; // Setting text color
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(txtCustomerId, gbc);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(new Color(133, 232, 235));; // Setting text color
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setForeground(new Color(0, 0, 0));; // Setting text color
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(new Color(133, 232, 235));; // Setting text color
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(lblPassword, gbc);

        pwdPassword = new JPasswordField(15);
        pwdPassword.setForeground(new Color(0, 0, 0));; // Setting text color
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(pwdPassword, gbc);

        btnLogin = new JButton("Login");
        btnLogin.setForeground(Color.black); // Setting text color
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(btnLogin, gbc);

        btnSignUp = new JButton("Sign Up");
        btnSignUp.setForeground(Color.black);; // Setting text color
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginPanel.add(btnSignUp, gbc);

        add(loginPanel, BorderLayout.CENTER);

        // Action listeners for login and signup buttons
        btnLogin.addActionListener(this);
        btnSignUp.addActionListener(this);

        setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
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
        if (e.getSource() == btnLogin) {
            login();
        } else if (e.getSource() == btnSignUp) {
            signUp();
        }
    }

    private void login() {
        int customerId = Integer.parseInt(txtCustomerId.getText());
        String username = txtUsername.getText();
        String password = String.valueOf(pwdPassword.getPassword());

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM login WHERE username = ? AND password = ? AND customer_id = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                openUserPage(customerId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while attempting to log in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
    	SignupPage signupPage = new SignupPage();
        signupPage.setVisible(true);
        // Close the current frame if needed
        dispose();
    }


    private void insertCustomerAndLogin(int customerId, String username, String password) throws SQLException {
        PreparedStatement insertCustomerStatement = connection.prepareStatement("INSERT INTO customer (customer_id) VALUES (?)");
        insertCustomerStatement.setInt(1, customerId);
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

    private void openUserPage(int customerId) {
        SwingUtilities.invokeLater(() -> {
            UserPage userPage = new UserPage(customerId);
            userPage.setVisible(true);
        });
    }
}
