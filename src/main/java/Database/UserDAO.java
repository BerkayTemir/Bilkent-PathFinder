package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    public boolean createUser(String username, String name, String surname, String hashedPassword)
        throws SQLException {

        String sql = "INSERT INTO users (username, first_name, last_name, password_hash) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, hashedPassword);

            return stmt.executeUpdate() == 1;
        }
    }
}
