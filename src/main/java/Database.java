import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3308/chitchatdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load JDBC Driver: " + e.getMessage());
        }
    }

    public void insertMessage(String sender, String message, LocalDateTime log_time) {
    	String sql = "INSERT INTO chat_logs (sender, message, log_time) VALUES (?, ?, ?)";
    	
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        	PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, message);
            stmt.setString(3, log_time.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging to database: " + e.getMessage());
        }
    }

    public static String listTasks() {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");

            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("task_description");
                String status = rs.getString("status");

                result.append(id)
                      .append(". ")
                      .append(desc)
                      .append(" - ")
                      .append(status)
                      .append(" ")
                      .append("<button onclick='markDone(")
                      .append(id)
                      .append(")'>Mark Done</button>")
                      .append(" <button onclick='deleteTask(")
                      .append(id)
                      .append(")'>Delete</button>")
                      .append("<br>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void markTaskDone(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE tasks SET status='completed' WHERE id=" + id;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM tasks WHERE id=" + id;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
