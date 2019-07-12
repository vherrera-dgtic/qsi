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
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;
	
	// Constructor
	public Municipi() { }
	public Municipi(Integer id, String nom, Date data_creacio, String usuari, Boolean activa, Provincia p)
	{
		this.id_municipi = id;
		this.nom = nom;
		this.data_creacio = data_creacio;
		this.usuari = usuari;
		this.actiu = activa;
		this.provincia = p;
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
	
	public Provincia getProvincia() { return this.provincia; }
	public void setProvincia(Provincia p) { this.provincia = p; }
	
}
