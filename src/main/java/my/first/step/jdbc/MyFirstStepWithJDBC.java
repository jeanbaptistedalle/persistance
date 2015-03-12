package my.first.step.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author JBD
 * 
 *         Cette classe vous aidera a faire vos premiers pas grâce à JDBC.
 *
 */
public class MyFirstStepWithJDBC {

	private Connection connection;
	private ResultSet resultSet;

	/**
	 * Cette méthode charge le pilote nécessaire à la connexion avec la BDD.
	 * Celui-ci prend la forme d'un jar qui ici est téléchargé grâce à maven.
	 */
	public MyFirstStepWithJDBC() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de charger le pilote");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Cette méthode permet de lancer une connection avec une BDD. Dans le cas
	 * où la BDD n'aurait pas besoin d'identifiant et/ou de mot de passe, il
	 * suffit de laisser ces champs à null.
	 * 
	 * Dans le cadre de ce tutoriel, on utilisera root pour se connecter à la
	 * BDD néanmoins, dans le cadre d'un véritable projet, il est fortement
	 * conseillé d'utiliser un utilisateur créé pour l'occasion afin qu'il
	 * bénéficie de droit dédiés (et donc pas des droits root). Après tout, il
	 * est peu probable qu'un utilisateur ait le droit de supprimer des tables !
	 * 
	 * @param bddAdress
	 * @param login
	 * @param password
	 */
	public void startConnection(final String bddAdress, final String login, final String password) {
		try {
			connection = DriverManager.getConnection(bddAdress, login, password);
		} catch (SQLException e) {
			System.out.println("Problème de connexion !");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Cette méthode permet d'inserer en base de données quelques
	 * enregistrements dans le but de tester nos futures requêtes.
	 */
	public void insertData() {
		try {
			String nomClient1 = "nomClient1";
			String prenomClient1 = "prenomClient1";
			/*
			 * Pour créer une requête paramétrée, il existe deux manières de
			 * faire avec JDBC. La première, la mauvaise, consiste simplement à
			 * concatener les différentes données. On pourra executer la requête
			 * grâce à un Statement comme vu précédemment.
			 */
			String requeteClient1 = "INSERT INTO Client(nom, prenom) value ('" + nomClient1
					+ "', '" + prenomClient1 + "');";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(requeteClient1);

			String nomClient2 = "nomClient2";
			String prenomClient2 = "prenomClient2";

			/*
			 * La seconde, bien plus propre consiste à indiquer où devront être
			 * placées les différents paramètres puis à les insérer selon leur
			 * position. Cette manière de faire permet notamment de se prémunir
			 * de l'injection de sql.
			 * 
			 * !! Attention cependant car les index commencent à 1 !!
			 */
			String requeteClient2 = "INSERT INTO Client(nom, prenom) value (?, ?);";
			PreparedStatement pstmt = connection.prepareStatement(requeteClient2);
			pstmt.setString(1, nomClient2);
			pstmt.setString(2, prenomClient2);
			pstmt.executeUpdate();
			
			List<Integer> listId = getClient();
			int id = listId.get(new Random().nextInt(listId.size()));

			String requeteCommande = "INSERT INTO Commande(dateCommande, idClient) value (?, ?);";
			PreparedStatement pstmt2 = connection.prepareStatement(requeteCommande);
			pstmt2.setDate(1, new Date(new java.util.Date().getTime()));
			pstmt2.setInt(2, id);
			pstmt2.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette methode permet d'afficher toutes les tables présentes en base de
	 * données ainsi que, pour chacune, les colonnes qui la compose
	 */
	public void printDatabase() {
		try {
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet rs = dmd.getTables(null, null, null, null);
			System.out.println("Liste des tables existantes : ");
			while (rs.next()) {
				/*
				 * Dans le DatabaseMetaData, l'indice 3 correspond au nom des
				 * tables
				 */
				System.out.println("Table " + rs.getString(3) + " : ");
				/*
				 * A partir du nom de la table, on effectue une requête select
				 * afin de récuperer le ResultSetMetaData. Ici, on utilisera
				 * tout de même un simple Statement car il n'est pas possible
				 * d'inserer dynamiquement le nom de la table dans un preparedStatement.
				 */
				Statement s = connection.createStatement();
				ResultSet rs2 = s.executeQuery("select * from " + rs.getString(3) + ";");
				ResultSetMetaData rsmd = rs2.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					System.out.println(rsmd.getColumnLabel(i));
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Anomalie lors de l'execution de la requête");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Cette méthode permet d'afficher tous les clients existant en BDD.
	 */
	public List<Integer> getClient() {
		String requete = "SELECT * FROM Client";
		/*
		 * Ici, le statement doit renvoyer des données, on utilise donc la
		 * méthode executeQuery() et on stockera les données renvoyées dans un
		 * ResultSet
		 */
		try {
			Statement stmt = connection.createStatement();
			resultSet = stmt.executeQuery(requete);

			System.out.println("parcours des données retournées");
			/*
			 * ResultSetMetaData contient toutes les données liés au ResultSet
			 * notamment : le nombre de colonne retournée, le nom d'une colonne
			 * en particulier, le type de donnée (au sens SQL), etc.
			 */
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int nbCols = rsmd.getColumnCount();
			/*
			 * la méthode next() permet d'avancer le curseur se trouvant sur le
			 * résultat de la requête d'une ligne. Le premier appel de next()
			 * place le curseur sur la première ligne. Si la dernière ligne est
			 * atteinte, next() renvoie false.
			 */
			List<Integer> listId = new ArrayList<Integer>();
			while (resultSet.next()) {
				for (int i = 1; i <= nbCols; i++) {
					System.out.print(rsmd.getColumnLabel(i) + " : " + resultSet.getString(i) + " ");
				}
				listId.add(resultSet.getInt(1));
				System.out.println();
			}
			resultSet.close();
			return listId;
		} catch (SQLException e) {
			System.out.println("Anomalie lors de l'execution de la requête");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Grâce à cette méthode, on tente de fermer la connection, peu importe
	 * comment le programme se déroule. Trop de connexion non fermées peuvent
	 * encombrer la BDD, de plus, fermer une connexion permet de s'assurer que
	 * la transaction est bien terminée.
	 */
	public void disconnect() {

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		MyFirstStepWithJDBC JDBC = new MyFirstStepWithJDBC();
		JDBC.startConnection("jdbc:mysql://localhost/persistance", "root", "root");

		JDBC.insertData();
		JDBC.printDatabase();
		JDBC.getClient();

		JDBC.disconnect();
	}
}