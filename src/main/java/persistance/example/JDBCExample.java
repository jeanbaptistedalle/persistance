package persistance.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JDBCExample {
	
	static class Client{
		public Integer idClient;
		public String nom;
		public String prenom;
		public Client(){}
	}

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			//Connection
			String bddAdress = "jdbc:mysql://localhost/persistance";
			String login = "root";
			String password = "root";
			Connection connection = DriverManager.getConnection(bddAdress, login, password);

			//Preparation de la requête
			String requeteClient = "SELECT * FROM Client where prenom = ?";
			PreparedStatement stmtClient = connection.prepareStatement(requeteClient);
			stmtClient.setString(1, "test");

			//Récupération et utilisation des résultats
			ResultSet rs = stmtClient.executeQuery();
			List<Client> listeClient = new ArrayList<Client>();
			while (rs.next()) {
				Client c = new Client();
				c.idClient = rs.getInt(1);
				c.nom = rs.getString(2);
				c.prenom = rs.getString(3);
				listeClient.add(c);
			}
			System.out.println(listeClient);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
