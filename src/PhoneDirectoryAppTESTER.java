import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class PhoneDirectoryAppTESTER {

    // JDBC driver and DB URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/PhoneDirectory";
    
    // Database credentials
    static final String USER = "root";
    static final String PASS = "manichandan@06"; // replace with your DB password
    
    private JFrame frame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextArea resultArea;
    private Connection conn;

    public PhoneDirectoryAppTESTER() {
        // Initialize GUI components
        frame = new JFrame("Phone Directory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField(20);
        
        JButton addButton = new JButton("Add");
        JButton searchByNameButton = new JButton("Search by Name");
        JButton searchByPhoneButton = new JButton("Search by Phone");
        
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addButton);
        panel.add(searchByNameButton);
        panel.add(searchByPhoneButton);
        panel.add(new JScrollPane(resultArea));
        
        frame.getContentPane().add(panel);
        
        // JDBC connection
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add event listeners
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
    }

    // Method to add an entry to the directory
    private void addEntry() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in both name and phone number.");
            return;
        }

        try {
            String sql = "INSERT INTO directory (name, phone) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
            resultArea.setText("Entry added successfully!");
            nameField.setText("");
            phoneField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error adding entry.");
        }
    }

    // Method to search by name
    private void searchByName() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a name.");
            return;
        }

        try {
            String sql = "SELECT phone FROM directory WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String phone = rs.getString("phone");
                resultArea.setText("Phone Number: " + phone);
            } else {
                resultArea.setText("No entry found for the name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error fetching details.");
        }
    }

    // Method to search by phone number
    private void searchByPhone() {
        String phone = phoneField.getText();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a phone number.");
            return;
        }

        try {
            String sql = "SELECT name FROM directory WHERE phone = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                resultArea.setText("Name: " + name);
            } else {
                resultArea.setText("No entry found for the phone number: " + phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error fetching details.");
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PhoneDirectoryAppTESTER().frame.setVisible(true);
            }
        });
    }
}
