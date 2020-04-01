package es.caib.qssiEJB.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entitat de mapeig de la taula QSI_PROVINCIA
 * @author [u97091] Toni Juanico Soler
 * data: 28/01/2019
 */

@Entity
@Table(name="QSI_PROVINCIA")
public class Provincia {
	@Id
	private Integer id_provincia;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Integer activa;
	
	// Constructor
	public Provincia() { }
	public Provincia(Integer id, String nom, Date data_creacio, String usuari, Boolean activa)
	{
			this.id_provincia = id;
			this.nom = nom;
			this.data_creacio = data_creacio;
			this.usuari = usuari;

			if (activa) this.activa=1; else this.activa=0;
	}
		
	// Mètodes get - set
	public Integer getId() { return this.id_provincia;	}
	public void setId(Integer id) {	this.id_provincia = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiva() { return (this.activa==1); }
	public void setActiva(Boolean value) { if (value) this.activa = 1; else this.activa=0;}
}
