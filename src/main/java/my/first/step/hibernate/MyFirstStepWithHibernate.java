package my.first.step.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author JBD
 *
 *         Cette classe permet d'effectuer quelques traitements simples sur une
 *         classe Client qui sera le reflet de la table Client située en base de
 *         données et d'une classe Commande, reflet de la table Commande.
 * 
 *         Pour que l'exemple fonctionne, on vérifiera plusieurs choses :
 * 
 *         -Il existe une base de donnée nommée "persistance".
 * 
 *         -Il existe un utilisateur nommé "root" et dont le mot de passe est
 *         "root". Si cela ne correspond pas, il est possible de modifier les
 *         valeurs prédéfinies dans le fichier hibernate.cfg.xml
 * 
 *         -Il existe une table Client et une table Commande dans la base de
 *         données (celle-ci doivent être créées grâce au fichier Client.sql et
 *         Commande.sql)
 *
 */
@SuppressWarnings("unchecked")
public class MyFirstStepWithHibernate {

	private Session session = null;

	public MyFirstStepWithHibernate() {

	}

	/**
	 * Cette méthode permet de réaliser l'enregistrement d'un Client et d'une
	 * commande pour ce client en base de données en utilisant hibernate
	 */
	public void insert() {
		/*
		 * Dans un premier lieu, on crée une transaction. Tant que celle-ci
		 * n'est pas validée grâce à la méthode commit, si une exception
		 * intervient, toutes les modifications effectuées dans cette
		 * transaction seront annulées
		 */
		session.beginTransaction();

		/*
		 * Comme on veux créer un stock en base de données, on crée un nouvel
		 * objet stock, auquel on affecte les données désirées.
		 */
		Client client = new Client();

		client.setNom("Tristan");
		client.setPrenom("Test");

		/*
		 * Puis, grâce à la méthode save, on indique à hibernate qu'il doit
		 * rendre persistant cette données. Après avoir utilisé cette méthode,
		 * l'objet stock aura son équivalent en base de données.
		 * 
		 * Cette méthode va donc générer une requête sql d'insertion qui sera
		 * affichée dans la console lors de son execution.
		 */

		Commande commande = new Commande();

		commande.setDateCommande(new Date());
		commande.setClient(client);

		session.save(client);

		/*
		 * Une fois les traitement effectués, on valide la transaction afin que
		 * les requêtes executées soient validées.
		 */
		session.getTransaction().commit();
	}

	/**
	 * Cette méthode permet de récupérer sous la forme d'une liste d'objet
	 * Client les enregistrements présents en base de données.
	 */
	public List<Client> listeClient() {
		System.out.println("Liste des clients : ");
		session.beginTransaction();
		/*
		 * Cette méthode permet de renvoyer toutes les lignes de la table
		 * Client, puis de les mapper afin d'obtenir des objets Client
		 */
		List<Client> list = session.createCriteria(Client.class).list();
		for (Client s : list) {
			/*
			 * Arrivé à ce point, Hibernate à chargé les Client, néanmoins,
			 * comme le lien one-to-many a été configuré comme lazy (cf:
			 * Client.hbm.xml et Commande.hbm.xml), les commandes n'ont pas été
			 * chargés. Cela est bien visible lorsqu'on essaie d'y accéder, car
			 * une nouvelle requête est exectuée (voir console).
			 * 
			 * Ainsi, on effectue une première requête pour obtenir les clients,
			 * mais une requête de plus est effectuée lorsqu'on accède pour la
			 * première fois à un objet auquel est lié l'objet de base.
			 */
			System.out.println(s);
			System.out.println(s.getCommandes());
		}
		session.getTransaction().commit();
		return list;
	}

	/**
	 * Cette méthode retourne une liste de client dont le nom est "test".
	 * 
	 * @return
	 */
	public List<Client> getTestClient() {
		session.beginTransaction();
		/*
		 * Jusqu'à maintenant, nous avons vu comment obtenir toutes les
		 * instances d'un type. Dans le cas où on rechercherais des données
		 * particulière, on utilise des requêtes hql. Le hql ressemble fortement
		 * à du sql à ceci près qu'il utilise les objets au lieu des tables.
		 * Comme on ne peut récuperer que des objets, il est inutile d'indiquer
		 * "select *" car l'objet entier doit être retourné. Puis, dans le
		 * where, on peut effectuer des tests sur les attributs de cet objet :
		 * s.stockName appelle en fait s.getStockName(), il est donc important
		 * de respecter la casse des getters et setters.
		 */
		String requete = "select c from Client c where c.prenom = :prenom";
		Query query = session.createQuery(requete);
		/*
		 * Comme vous l'aurez remarqué, la requête contient :prenom, qui
		 * correspond à un paramètre. Il est possible de le remplacer grâce à
		 * set<Data> (et ainsi d'éviter l'injection, comme en jdbc).
		 */
		query.setString("prenom", "Test");
		/*
		 * La requête retourne ensuite les données directement sous forme d'une
		 * liste d'objet.
		 */
		List<Client> list = query.list();
		session.getTransaction().commit();
		return list;
	}

	/**
	 * Cette methode permet de lister les commandes existant
	 * en base de données et de les restituer sous forme d'objet
	 * 
	 * @return
	 */
	public List<Commande> listeCommande() {
		System.out.println("Liste des commandes : ");
		session.beginTransaction();
		/*
		 * Cette méthode permet de renvoyer toutes les lignes de la table
		 * Commande, puis de les mapper afin d'obtenir des objets Commande
		 */
		List<Commande> list = session.createCriteria(Commande.class).list();
		for (Commande d : list) {
			System.out.println(d);
		}
		session.getTransaction().commit();
		return list;
	}

	/**
	 * Cette methode permet de renvoyer une liste de stock pour lesquels il
	 * existe un enregistrement journalier correspondant à aujourd'hui
	 * 
	 * @return
	 */
	public List<Client> listeClientAvecCommande() {
		session.beginTransaction();
		String requete = "select c from Client c join c.commandes as com where com is not empty and com.dateCommande = :today";
		Query query = session.createQuery(requete);

		query.setDate("today", new Date());

		List<Client> list = query.list();
		for (Client s : list) {
			System.out.println(s);
			System.out.println(s.getCommandes());
		}
		session.getTransaction().commit();
		return list;
	}

	/**
	 * Cette méthode permet, s'il n'en existe pas encore, d'ouvrir une session
	 * hibernate
	 */
	public void openSession() {
		if (session == null) {
			session = HibernateUtil.getSessionFactory().openSession();
		}
	}

	/**
	 * Cette méthode permet, s'il en existe une, de terminer la session
	 * hibernate
	 */
	public void closeSession() {
		session.close();
		session = null;
	}

	public static void main(String[] args) {
		MyFirstStepWithHibernate hibernate = new MyFirstStepWithHibernate();
		hibernate.openSession();
		hibernate.insert();
		hibernate.listeClient();
		hibernate.listeCommande();
		hibernate.listeClientAvecCommande();
		hibernate.closeSession();
		System.exit(0);
	}
}
