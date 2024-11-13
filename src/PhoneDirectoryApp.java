import javax.swing.*;
import java.sql.SQLException;
import java.util.Scanner;

public class PhoneDirectoryApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=====================*NOTE*=====================");
        System.out.println("        Space's can be taken as character        ");
        System.out.println("================================================");
        System.out.println("Enter user name:");
        String USER= sc.nextLine() ;
        System.out.println("Enter the password:");
        String PASS = sc.nextLine() ;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    PhoneDirectoryDB db = new PhoneDirectoryDB(USER,PASS);  
                    // PhoneDirectoryDB db = new PhoneDirectoryDB();  
                    new PhoneDirectoryUI(db);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error initializing the application: " + e.getMessage());
                }
            }
        });
        System.out.println();
        sc.close();
    }
}