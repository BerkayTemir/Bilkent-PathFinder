package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static void main(String[] args) {

        String jdbcURL = "jdbc:postgresql://localhost:5432/bilkent_pathfinder";
        String username = "postgres";
        String password = System.getenv("DB_PASSWORD");

        try {
            Connection connection =
                DriverManager.getConnection(jdbcURL, username, password);

            System.out.println("Connected successfully!");

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}