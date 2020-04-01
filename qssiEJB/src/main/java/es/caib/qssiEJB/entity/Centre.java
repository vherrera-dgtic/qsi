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
@Table(name="QSI_CENTRE")
public class Centre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_centre;
	
	@Column
	private String nom;
	
	@Column
	private String dir3;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Integer actiu; 
	
	@Column
	private Integer visible_web;
	
	// Constructor
	public Centre() { }
	public Centre(Integer id, String nom, String dir3, Date data_creacio, String usuari, Boolean actiu, Boolean visible_web)
	{
		this.id_centre = id;
		this.nom = nom;
		this.dir3 = dir3;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		
		if (actiu) this.actiu =1; else this.actiu = 0;
		if (visible_web) this.visible_web=1; else this.visible_web=0;
		
		
	}
	
	// Mètodes get - set
	public Integer getId() { return this.id_centre;	}
	public void setId(Integer id) {	this.id_centre = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public String getDir3() { return this.dir3; }
	public void setDir3(String value) { this.dir3 = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiu() { return (this.actiu==1); }
	public void setActiu(Boolean value) { if (value) this.actiu =1; else this.actiu = 0; }
	
	public Boolean getVisible_web() { return (this.actiu==1); }
	public void setVisible_web(Boolean value) { if (value) this.visible_web = 1; else this.visible_web = 0;}
}
