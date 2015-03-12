package persistance.example;

import java.util.List;

import my.first.step.hibernate.HibernateUtil;

import my.first.step.hibernate.Client;
import org.hibernate.Query;
import org.hibernate.Session;

public class HibernateExample {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery("select c from Client c where c.prenom = :prenom");
		query.setString("prenom", "test");

		List<Client> listeClient = query.list();
		System.out.println(listeClient);

		session.close();
	}
}
