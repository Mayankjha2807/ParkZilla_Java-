package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutPage {

    private JFrame frame;

    public LogoutPage() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 526));
        frame.setTitle("Logout");

        // Create a JLabel with the background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\\\Users\\\\ANEESH MINJ\\\\Downloads\\\\PARK-ZILLA (2).png"));
        backgroundLabel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make the panel transparent
        backgroundLabel.add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel("You have been logged out.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the logout page
                new LoginSignupPage(); // Open the login page
            }
        });
        panel.add(loginButton);

        // Set the background label as the content pane
        frame.setContentPane(backgroundLabel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
