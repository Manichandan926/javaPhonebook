import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class PhoneDirectoryUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextArea resultArea;
    private PhoneDirectoryDB phoneDirectoryDB;
    private Image backgroundImage;

    // Constructor to initialize the UI and bind actions
    public PhoneDirectoryUI(PhoneDirectoryDB db) {
        this.phoneDirectoryDB = db;

        // Load the background image
        backgroundImage = new ImageIcon(
                "C:\\Users\\N Mani Chandan\\Downloads\\Phone book\\Rahuls PhoneBook\\src\\image1.jpg").getImage();

        frame = new JFrame("Phone Directory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Custom JPanel with background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Set layout for background panel
        frame.setContentPane(backgroundPanel); // Set the panel as content pane

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridBagLayout()); // Set layout for card panel
        cardPanel.setBackground(new Color(255, 255, 255, 60)); // Semi-transparent white background
        cardPanel.setPreferredSize(new Dimension(600, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField(20);

        JButton addButton = new JButton("Add");
        JButton searchByNameButton = new JButton("Search by Name");
        JButton searchByPhoneButton = new JButton("Search by Phone");
        JButton deleteButton = new JButton("Delete");

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        // Add components to the card panel with GridBagLayout constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        cardPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cardPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        cardPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cardPanel.add(addButton, gbc);

        gbc.gridx = 1;
        cardPanel.add(searchByNameButton, gbc);

        gbc.gridx = 2;
        cardPanel.add(searchByPhoneButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        cardPanel.add(deleteButton, gbc); // Add the delete button

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(resultArea);
        cardPanel.add(scrollPane, gbc);

        // Add the card panel to the background panel
        backgroundPanel.add(cardPanel); // Center the card panel in the background panel

        // Event listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }
        });

        searchByNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchByName();
            }
        });

        searchByPhoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchByPhone();
            }
        });

        deleteButton.addActionListener(new ActionListener() { // Action for delete button
            public void actionPerformed(ActionEvent e) {
                deleteEntry();
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        frame.setVisible(true);
    }

    // Add entry button action
    private void addEntry() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in both name and phone number.");
            return;
        }

        try {
            phoneDirectoryDB.addEntry(name, phone);
            resultArea.setText("Entry added successfully!");
            nameField.setText("");
            phoneField.setText("");
        } catch (SQLException e) {
            resultArea.setText("Error adding entry: " + e.getMessage());
        }
    }

    // Search by name button action
    private void searchByName() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a name.");
            return;
        }

        try {
            String phone = phoneDirectoryDB.searchPhoneByName(name);
            if (phone != null) {
                resultArea.setText("Phone Number: " + phone);
            } else {
                resultArea.setText("No entry found for the name: " + name);
            }
        } catch (SQLException e) {
            resultArea.setText("Error fetching details: " + e.getMessage());
        }
    }

    // Search by phone button action
    private void searchByPhone() {
        String phone = phoneField.getText();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a phone number.");
            return;
        }

        try {
            String name = phoneDirectoryDB.searchNameByPhone(phone);
            if (name != null) {
                resultArea.setText("Name: " + name);
            } else {
                resultArea.setText("No entry found for the phone number: " + phone);
            }
        } catch (SQLException e) {
            resultArea.setText("Error fetching details: " + e.getMessage());
        }
    }

    // Delete button action
    private void deleteEntry() {
        String name = nameField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() && phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a name or phone number to delete.");
            return;
        }

        try {
            boolean deleted;
            if (!name.isEmpty()) {
                deleted = phoneDirectoryDB.deleteEntryByName(name);
            } else {
                deleted = phoneDirectoryDB.deleteEntryByPhone(phone);
            }

            if (deleted) {
                resultArea.setText("Entry deleted successfully!");
                nameField.setText("");
                phoneField.setText("");
            } else {
                resultArea.setText("No entry found to delete.");
            }
        } catch (SQLException e) {
            resultArea.setText("Error deleting entry: " + e.getMessage());
        }
    }
}
