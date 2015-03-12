package my.first.step.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * Cette classe est la symbolisation sous forme d'objet de la table
 * daily_record. L'ORM (Object Relationnal Mapping) est réalisé grâce à la
 * configuration se trouvant dans le fichier Commande.hbm.xml
 */
public class Commande implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 130224046705238482L;

	private Integer idCommande;
	private Date dateCommande;
	private Client client;

	public Commande() {

	}

	public Commande(Date dateCommande, Client client) {
		this.client = client;
		this.dateCommande = dateCommande;
	}

	public Integer getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Integer idCommande) {
		this.idCommande = idCommande;
	}

	public Date getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String toString() {
		return idCommande + " " + dateCommande;
	}
}
