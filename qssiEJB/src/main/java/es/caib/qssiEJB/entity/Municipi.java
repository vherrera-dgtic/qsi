package es.caib.qssiEJB.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entitat de mapeig de la taula QSI_MUNICIPI
 * @author [u97091] Toni Juanico Soler
 * data: 11/09/2018
 */

@Entity
@Table(name="QSI_MUNICIPI")
public class Municipi {
	@Id
	private Integer id_municipi;
	
	@Column
	private String nom;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String usuari;
	
	@Column
	private Boolean actiu;
	
	@Column
	private Integer id_provincia;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_illa")
    private Illa illa;
	
	// Constructor
	public Municipi() { }
	public Municipi(Integer id, String nom, Date data_creacio, String usuari, Boolean activa, Illa i)
	{
		this.id_municipi = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		this.actiu = activa;
		this.illa = i;
		
	}
	
	// Mètodes get - set
	public Integer getId() { return this.id_municipi;	}
	public void setId(Integer id) {	this.id_municipi = id;	}
	
	public String getNom() { return this.nom; }
	public void setNom(String value) { this.nom = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public String getUsuari() { return this.usuari; }
	public void setUsuari(String value) { this.usuari = value; }
	
	public Boolean getActiu() { return this.actiu; }
	public void setActiu(Boolean value) { this.actiu = value; }
	
	public Illa getIlla() { return this.illa; }
	public void setIlla(Illa i) { this.illa = i; }
	
	public Integer getProvincia() { return this.id_provincia; }
	public void setProvincia(Integer id_provincia) { this.id_provincia = id_provincia; }
	
}
