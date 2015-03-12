package persistance.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCExample {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String bddAdress = "jdbc:mysql://localhost/persistance";
			String login = "root";
			String password = "root";
			Connection connection = DriverManager.getConnection(bddAdress, login, password);

			String requete = "SELECT * FROM Client where prenom = ?";
			PreparedStatement stmt = connection.prepareStatement(requete);
			stmt.setString(1, "test");

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " "+rs.getString(3));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
