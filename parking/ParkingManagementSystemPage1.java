package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParkingManagementSystemPage1 extends JFrame implements ActionListener {
    private JButton customerButton;
    private JButton adminButton;
    private JPanel contentPane;

    public ParkingManagementSystemPage1() {
        setTitle("Parking Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 526);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Set the content pane to use a custom JPanel
        contentPane = new JPanel() {
            // Override paintComponent to set background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image from file
                ImageIcon imageIcon = new ImageIcon("C:\\Users\\ANEESH MINJ\\Downloads\\PARK-ZILLA.png");
 // Replace "background.jpg" with your image file path
                // Draw the image to the panel
                g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        // Set layout to BorderLayout to position buttons at the bottom
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Create buttons
        customerButton = new JButton("Customer");
        adminButton = new JButton("Admin");

        // Set preferred button size
        Dimension buttonSize = new Dimension(200, 60);
        customerButton.setPreferredSize(buttonSize);
        adminButton.setPreferredSize(buttonSize);

        // Add action listeners
        customerButton.addActionListener(this);
        adminButton.addActionListener(this);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.add(customerButton);
        buttonPanel.add(adminButton);

        // Add buttons panel to the content pane at the bottom
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == customerButton) {
            // Assuming you have obtained the customer ID from the login/signup process
            // Replace '1' with the actual customer ID
            LoginSignupPage loginSignupPage = new LoginSignupPage();
            loginSignupPage.setVisible(true);
            this.dispose();
        } else if (e.getSource() == adminButton) {
            // Handle admin button click
            // Open the AdminPage
            AdminPage adminPage = new AdminPage();
            adminPage.setVisible(true);
            this.dispose(); // Close the current page
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ParkingManagementSystemPage1 app = new ParkingManagementSystemPage1();
            app.setVisible(true);
        });
    }
}
