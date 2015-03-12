package my.first.step.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * Cette classe est la symbolisation sous forme d'objet de la table stock. L'ORM
 * (Object Relationnal Mapping) est réalisé grâce à la configuration se trouvant
 * dans le fichier Client.hbm.xml
 */
public class Client implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1515372912011034166L;

	private Integer idClient;
	private String nom;
	private String prenom;
	private Set<Commande> commandes;

	public Client() {
		commandes = new HashSet<Commande>();
	}

	public Client(String nom, String prenom, Set<Commande> commandes) {
		this.nom = nom;
		this.prenom = prenom;
		this.commandes = commandes;
	}

	public Integer getIdClient() {
		return idClient;
	}

	public void setIdClient(Integer idClient) {
		this.idClient = idClient;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Set<Commande> getCommandes() {
		return commandes;
	}

	public void setCommandes(Set<Commande> commandes) {
		this.commandes = commandes;
	}

	public String toString() {
		return idClient + " " + nom + " " + prenom;
	}
}
