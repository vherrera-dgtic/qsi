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
@Table(name="QSI_MOTIU")
public class Motiu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="qsi_sequencia")
	@SequenceGenerator(name="qsi_sequencia", sequenceName = "qsi_sequencia", allocationSize=1)
	private Integer id_motiu;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari; 
	
	@Column
	private Integer actiu;
	
	// Constructors
	public Motiu() {	}
	public Motiu(Integer id, String nom, Date data_creacio, String usuari, Boolean actiu)
	{
		this.id_motiu = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		
		if (actiu) this.actiu =1;
		else this.actiu = 0;
		
	}
	
	// Mètodes set i get
	public Integer getId() { return id_motiu;	}
	public void setId(Integer id) {	this.id_motiu = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiu() { return (this.actiu==1); }
	public void setActiu(Boolean value) { if (value) this.actiu =1; else this.actiu = 0; }
	
}
