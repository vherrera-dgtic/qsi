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
 * Entitat de mapeig de la taula QSI_MATERIA
 * @author [u97091] Toni Juanico Soler
 * data: 27/08/2018
 */
@Entity
@Table(name="QSI_MATERIA")
public class Materia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_materia;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari; 
	
	@Column
	private Boolean activa;
	
	// Constructors
	public Materia() { 
		
	}
	
	public Materia(Integer id, String nom, Date data_creacio, String usuari, Boolean activa)
	{
		this.id_materia = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		this.activa = activa;
		
	}
	
	// Mètodes set i get
	public Integer getId() { return id_materia;	}
	public void setId(Integer id) {	this.id_materia = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; }
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiva() { return this.activa; }
	public void setActiva(Boolean value) { this.activa = value; }
	
}
