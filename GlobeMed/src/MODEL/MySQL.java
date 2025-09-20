package MODEL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class MySQL {

    private static Connection connection;

    public static Statement createConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/globemed_db",
                    "root",
                    "@Pasanhansaka2003"
            );
        }
        return connection.createStatement();
    }

    // Original iud method
    public static void iud(String query) {
        try {
            createConnection().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Overloaded iud method
    public static void iud(String query, Object... params) {
        try {
            PreparedStatement pstmt = getConnection().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Original search method
    public static ResultSet search(String query) throws Exception {
        try {
            return createConnection().executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    // Overloaded search method
    public static ResultSet search(String query, Object... params) throws Exception {
        try {
            PreparedStatement pstmt = getConnection().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                createConnection();
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
