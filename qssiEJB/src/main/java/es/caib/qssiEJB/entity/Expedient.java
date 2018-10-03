package es.caib.qssiEJB.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entitat de mapeig de la taula QSI_EXPEDIENT - Aquesta entitat és el core de l'aplicació
 * @author [u97091] Toni Juanico Soler
 * data: 18/09/2018
 */

@Entity
@Table(name="QSI_EXPEDIENT")
public class Expedient {
	@Id
	private Long id_expedient;
	
	@Column
	private String assumpte;
	
	@Column
	private String unitat_organica;
	
	@Column
	private Date data_entrada;
	
	@Column
	private Date data_creacio;
	
	@Column
	private String via_contestacio;
	
	@Column
	private String usuari_assignat;
	
	@Column
	private String text_peticio;
	
	@Column
	private String text_resposta;
	
	@Column
	private Date data_resposta;
	
	@Column
	private Integer id_estat;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_escrit")
    private Escrit escrit;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subcentre")
    private Subcentre subcentre;
	
	// Constructor
	public Expedient() { }
	
	// Mètodes get - set
	public Long getId() { return this.id_expedient;	}
	public void setId(Long id) {	this.id_expedient = id;	}
	
	public String getAssumpte() { return this.assumpte; }
	public void setAssumpte(String value) { this.assumpte = value; }
	
	public String getUnitatOrganica() { return this.unitat_organica; }
	public void setUnitatOrganica(String value) { this.unitat_organica = value; }
	
	public String getViaContestacio() { return this.via_contestacio; }
	public void setViaContestacio(String value) { this.via_contestacio = value; }
	
	public String getUsuariassignat() { return this.usuari_assignat; }
	public void setUsuariassignat(String value) { this.usuari_assignat = value; }
	
	public String getTextPeticio() { return this.text_peticio; }
	public void setTextPeticio(String value) { this.text_peticio = value; }
	
	public String getTextResposta() { return this.text_resposta; }
	public void setTextRespota(String value) { this.text_resposta = value; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public Date getDataResposta() { return this.data_resposta; }
	public void setDataResposta(Date data) { this.data_resposta = data; } 
	
	public Integer getEstat() { return this.id_estat; }
	public void setEstat(Integer value) { this.id_estat = value; }
	
	public Escrit getEscrit() { return this.escrit; }
	public void setEscrit(Escrit e) { this.escrit = e; }
	
	public Subcentre getSubcentre() { return this.subcentre; }
	public void setSubcentre(Subcentre s) { this.subcentre = s; }
	
	public Date getDatavenciment()
	{
		// convert date to calendar
	    Calendar c = Calendar.getInstance();
	    c.setTime(this.data_entrada);
	    c.add(Calendar.DATE, 20); // TODO: La norma dia 15 dies hàbils. S'ha de crear una taula amb els festius i afinar l'algorisme, Toni Juaico
	    return c.getTime();

	}
	
}
