package es.caib.qssiEJB.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entitat de mapeig de la taula QSI_ESCRIT
 * @author [u97091] Toni Juanico Soler
 * data: 28/08/2018
 */

@Entity
@Table(name="QSI_ESCRIT")
public class Escrit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_escrit;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Integer actiu;
	
	// Constructor
	public Escrit() { }
	public Escrit(Integer id, String nom, Date data_creacio, String usuari, Boolean actiu)
	{
		this.id_escrit = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		
		if (actiu) this.actiu =1;
		else this.actiu = 0;
	}
	
	// Mètodes get - set
	public Integer getId() { return this.id_escrit;	}
	public void setId(Integer id) {	this.id_escrit = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiu() { return (this.actiu==1); }
	public void setActiu(Boolean value) { if (value) this.actiu =1; else this.actiu = 0; }
	
}
