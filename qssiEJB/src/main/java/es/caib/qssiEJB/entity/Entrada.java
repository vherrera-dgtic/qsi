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
 * Entitat de mapeig de la taula QSI_ENTRADA
 * @author [u97091] Toni Juanico Soler
 * data: 06/09/2018
 */

@Entity
@Table(name="QSI_ENTRADA")
public class Entrada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_entrada;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Integer activa;
	
	// Constructor
	public Entrada() { }
	public Entrada(Integer id, String nom, Date data_creacio, String usuari, Boolean activa)
	{
		this.id_entrada = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		
		if (activa) this.activa=1; else this.activa=0;
	}
	
	// M�todes get - set
	public Integer getId() { return this.id_entrada;	}
	public void setId(Integer id) {	this.id_entrada = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiva() { return (this.activa==1); }
	public void setActiva(Boolean value) { if (value) this.activa = 1; else this.activa=0;}
	
}
