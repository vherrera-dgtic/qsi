package es.caib.qssiEJB.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="QSI_QUEIXA")
public class Queixa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_queixa;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari; 
	
	@Column
	private Integer activa;
	
	// Constructors
	public Queixa() {	}
	public Queixa(Integer id, String nom, Date data_creacio, String usuari, Boolean activa)
	{
		this.id_queixa = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		
		if (activa) this.activa=1; else this.activa=0;
			
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
		
	public Boolean getActiva() { return (this.activa==1); }
	public void setActiva(Boolean value) { if (value) this.activa = 1; else this.activa=0;}
		
}
