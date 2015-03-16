package persistance.example;

import java.util.List;

import my.first.step.hibernate.Client;
import my.first.step.hibernate.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateExample {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("select c from Client c where c.prenom = :prenom");
		query.setString("prenom", "test");

		List<Client> listeClient = query.list();
		System.out.println(listeClient);
		
		Client client = listeClient.get(0);

		client.setNom("Tristan 2");
		client.setPrenom("test2");
		session.saveOrUpdate(client);
		
		Query query2 = session.createQuery("select c from Client c where c.prenom = :prenom");
		query2.setString("prenom", "test2");
		System.out.println(query2.list());
		
		transaction.commit();
		session.close();
		System.exit(0);
	}
}
