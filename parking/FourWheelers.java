package project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FourWheelers {

    private static final int MAX_SLOTS = 20;
    private static boolean[] slots = new boolean[MAX_SLOTS];

    JFrame frame;
    private JTextArea textArea;
    private Connection connection;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FourWheelers window = new FourWheelers();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FourWheelers() {
        initialize();
        connectToDatabase(); // Establish database connection
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel bookingPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Booking System", null, bookingPanel, null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        bookingPanel.add(buttonPanel, BorderLayout.WEST);

        JButton viewSlotsButton = createStyledButton("View Slots");
        JButton viewBookingsButton = createStyledButton("View Bookings");
        JButton makeBookingButton = createStyledButton("Make Booking"); // Renamed from "Make Payment"
        JButton logoutButton = createStyledButton("Logout");

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(viewSlotsButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewBookingsButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(makeBookingButton); // Changed button name
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(logoutButton);

        JPanel outputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("vehicle.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        outputPanel.setLayout(new BorderLayout());
        bookingPanel.add(outputPanel, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setOpaque(false);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        viewSlotsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewSlots();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBookings();
            }
        });

        makeBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeBooking(); // Call makeBooking method
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void viewBookings() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (slots[i]) {
                model.addElement("Slot " + (i + 1) + ": Booked");
            }
        }
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(5);
        JOptionPane.showMessageDialog(frame, new JScrollPane(list), "Bookings", JOptionPane.PLAIN_MESSAGE);
    }

    private void viewSlots() {
        // Execute SQL query to retrieve available slots from the database
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT slot_number FROM booked_slots_four_wheelers");
            while (resultSet.next()) {
                int slotNumber = resultSet.getInt("slot_number");
                slots[slotNumber - 1] = true; // Mark slot as booked
            }

            // Display available slots
            Object[][] data = new Object[MAX_SLOTS][2];
            for (int i = 0; i < MAX_SLOTS; i++) {
                data[i][0] = "Slot " + (i + 1);
                data[i][1] = slots[i] ? "Booked" : "Available";
            }

            String[] columnNames = {"Slot Number", "Status"};

            JTable table = new JTable(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (value != null && value.toString().equals("Available")) {
                        cellComponent.setBackground(Color.GREEN);
                    } else {
                        cellComponent.setBackground(Color.RED);
                    }
                    return cellComponent;
                }
            });

            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(frame, scrollPane, "Available Slots", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to retrieve available slots", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makeBooking() {
        // Open the booking page
        BookingPage bookingPage = new BookingPage();
        bookingPage.setVisible(true);
        // Close the current frame if needed
        frame.dispose();
    }

    private void logout() {
        // Close the database connection
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.dispose();
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
}
