package my.first.step.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Cette classe permet de récuperer l'objet SessionFactory d'hibernate. On
 * implémentera ici le design pattern SINGLETON afin de s'assurer qu'il n'y ait
 * qu'une seule instance de SessionFactory.
 */
public class HibernateUtil {

	private static SessionFactory INSTANCE;

	private static SessionFactory buildSessionFactory() {
		try {
			/*
			 * Creation de la sessionFactory à partir des informations contenues
			 * dans le fichier hibernate.cfg.xml
			 */
			Configuration config = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
					.applySettings(config.getProperties());
			return config.buildSessionFactory(builder.build());
		} catch (Exception ex) {
			System.err.println("La création du sessionFactory a échouée." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (INSTANCE == null) {
			INSTANCE = buildSessionFactory();
		}
		return INSTANCE;
	}
}