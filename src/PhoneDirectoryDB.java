import java.sql.*;

public class PhoneDirectoryDB {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PhoneDirectory";
    // private static final String USER = "root";
    // private static final String PASS = "manichandan@06";
    
    private Connection conn;

    // Constructor to initialize the connection
    public PhoneDirectoryDB(String USER, String PASS) throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Method to add an entry to the directory
    public void addEntry(String name, String phone) throws SQLException {
        String sql = "INSERT INTO directory (name, phone) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, phone);
        pstmt.executeUpdate();
    }

    // Method to search for a phone by name
    public String searchPhoneByName(String name) throws SQLException {
        String sql = "SELECT phone FROM directory WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("phone");
        } else {
            return null;
        }
    }

    // Method to search for a name by phone
    public String searchNameByPhone(String phone) throws SQLException {
        String sql = "SELECT name FROM directory WHERE phone = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, phone);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        } else {
            return null;
        }
    }

    // Method to delete an entry by phone number
    public boolean deleteEntryByPhone(String phone) throws SQLException {
        String sql = "DELETE FROM directory WHERE phone = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, phone);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; // Return true if a row was deleted, false otherwise
    }

    // Method to delete an entry by name
    public boolean deleteEntryByName(String name) throws SQLException {
        String sql = "DELETE FROM directory WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; // Return true if a row was deleted, false otherwise
    }

    // Close the database connection
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
