package es.caib.qssiEJB.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="QSI_QUEIXA")
public class Queixa {
	@Id
	private Integer id_queixa;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari; 
	
	@Column
	private Boolean activa;
	
	// Constructors
	public Queixa() {	}
	public Queixa(Integer id, String nom, Date data_creacio, String usuari, Boolean activa)
	{
		this.id_queixa = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		this.activa = activa;
			
	}
		
	// Mètodes set i get
	public Integer getId() { return id_queixa;	}
	public void setId(Integer id) {	this.id_queixa = id;	}
		
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
		
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
		
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
		
	public Boolean getActiva() { return this.activa; }
	public void setActiva(Boolean value) { this.activa = value; }
		
}
