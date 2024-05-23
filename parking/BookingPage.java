package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BookingPage {

    private JFrame frame;
    private JTextField customerIdField;
    private JTextField slotIdField;
    private JTextField vehicleTypeField;
    private JTextField bookingDateTimeField;
    private Connection connection;

    public BookingPage() {
        initialize();
        connectToDatabase(); // Establish database connection
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 526));
        frame.setTitle("Booking Page");

        // Define custom color
        Color customTextColor = new Color(133, 232, 235);

        // Create a JLabel with the background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\ANEESH MINJ\\Downloads\\PARK-ZILLA (2).png"));
        backgroundLabel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make the panel transparent
        panel.setLayout(new GridBagLayout());
        backgroundLabel.add(panel, gbc);

        JLabel customerIdLabel = new JLabel("Customer ID:");
        setupLabel(customerIdLabel, customTextColor);
        customerIdField = createTextField(30, customTextColor);

        JLabel slotIdLabel = new JLabel("Slot ID:");
        setupLabel(slotIdLabel, customTextColor);
        slotIdField = createTextField(30, customTextColor);

        JLabel vehicleTypeLabel = new JLabel("Please enter four_wheeler or two_wheeler:");
        setupLabel(vehicleTypeLabel, customTextColor);
        vehicleTypeField = createTextField(30, customTextColor);

        JLabel bookingDateTimeLabel = new JLabel("Booking Date & Time:");
        setupLabel(bookingDateTimeLabel, customTextColor);
        bookingDateTimeField = createTextField(30, customTextColor);

        JButton bookButton = createButton("Book", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookSlot();
            }
        });

        JButton logoutButton = createButton("Logout", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        addComponents(panel, gbc, customerIdLabel, customerIdField, slotIdLabel, slotIdField, vehicleTypeLabel,
                vehicleTypeField, bookingDateTimeLabel, bookingDateTimeField, logoutButton, bookButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void bookSlot() {
        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            int slotId = Integer.parseInt(slotIdField.getText());
            String vehicleType = vehicleTypeField.getText();
            String bookingDateTime = bookingDateTimeField.getText();

            // Check if slot is available

            // Perform booking based on vehicle type
            String tableName;
            if ("two_wheeler".equals(vehicleType)) {
                tableName = "booked_slots";
            } else if ("four_wheeler".equals(vehicleType)) {
                tableName = "booked_slots_four_wheelers";
            } else {
                // Handle invalid vehicle type
                JOptionPane.showMessageDialog(frame, "Invalid vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert booking into the appropriate table
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + tableName + " (customer_id, slot_id, booking_time, slot_number, vehicle_type) " +
                            "VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, customerId);
            statement.setInt(2, slotId);
            statement.setString(3, bookingDateTime);
            statement.setInt(4, slotId); // Assuming slot_id and slot_number are the same
            statement.setString(5, vehicleType);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Slot booked successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to book the slot.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Invalid input or database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3307/parking_management_system";
        String username = "root"; // Change this
        String password = "root"; // Change this

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        frame.dispose(); // Close the booking page
        new LogoutPage(); // Open the logout page
    }

    private JTextField createTextField(int columns, Color textColor) {
        JTextField textField = new JTextField(columns);
        textField.setForeground(textColor);
        return textField;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(listener);
        return button;
    }

    private void setupLabel(JLabel label, Color textColor) {
        label.setForeground(textColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void addComponents(JPanel panel, GridBagConstraints gbc, JComponent... components) {
        for (JComponent component : components) {
            gbc.gridy++;
            panel.add(component, gbc);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BookingPage window = new BookingPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
