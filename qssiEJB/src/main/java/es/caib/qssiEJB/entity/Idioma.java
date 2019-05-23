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
 * Entitat de mapeig de la taula QSI_IDIOMA
 * @author [u97091] Toni Juanico Soler
 * data: 11/09/2018
 */

@Entity
@Table(name="QSI_IDIOMA")
public class Idioma {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_idioma;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Boolean actiu;
	
	// Constructor
	public Idioma() { }
	public Idioma(Integer id, String nom, Date data_creacio, String usuari, Boolean actiu)
	{
		this.id_idioma = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		this.actiu = actiu;
	}
	
	// Mètodes get - set
	public Integer getId() { return this.id_idioma;	}
	public void setId(Integer id) {	this.id_idioma = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiu() { return this.actiu; }
	public void setActiu(Boolean value) { this.actiu = value; }
}
