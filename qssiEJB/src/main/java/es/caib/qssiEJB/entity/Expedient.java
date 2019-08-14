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

import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;

/**
 * Entitat de mapeig de la taula QSI_EXPEDIENT - Aquesta entitat és el core de l'aplicació
 * @author [u97091] Toni Juanico Soler
 * data: 18/09/2018
 */

@Entity
@Table(name="QSI_EXPEDIENT")
public class Expedient {
	@Id
	private Integer id_expedient;
	
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
	private Integer id_gestor;
	
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
	
	@Column
	private String num_identificacio;
	
	@Column 
	private String nom;
	
	@Column 
	private String llinatge1;
	
	@Column 
	private String llinatge2;
	
	@Column 
	private String telefon;
	
	@Column 
	private String email;
	
	@Column 
	private String direccio;
	
	@Column 
	private String numero;
	
	@Column 
	private String pis;
	
	@Column 
	private String codi_postal;
	
	@Column
	private String id_expedient_arxiu_caib;
	
	@Column
	private String url_versio_imprimible_expedient_arxiu_caib;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_escrit")
    private Escrit escrit;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_centre")
    private Centre centre;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subcentre")
    private Subcentre subcentre;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_materia")
	private Materia materia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_motiu")
	private Motiu motiu;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_queixa")
	private Queixa queixa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_entrada")
	private Entrada entrada;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_idioma")
	private Idioma idioma;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_municipi")
	private Municipi municipi;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_identificacio")
	private Identificacio identificacio;
	
	// Constructor
	public Expedient() { }
	
	// Mètodes get - set
	public Integer getId() { return this.id_expedient;	}
	public void setId(Integer id) {	this.id_expedient = id;	}
	
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
	
	public Date getDataentrada() { return this.data_entrada; }
	public void setDataentrada(Date data) { this.data_entrada = data; }
	
	public Date getDatacreacio() { return this.data_creacio; }
	public void setDatacreacio(Date data) { this.data_creacio = data; } 
	
	public Date getDataresposta() { return this.data_resposta; }
	public void setDataresposta(Date data) { this.data_resposta = data; } 
	
	public Integer getEstat() { return this.id_estat; }
	public void setEstat(ExpedientServiceInterface.EstatExpedient value) { this.id_estat = value.getValue(); }
	
	public Escrit getEscrit() { return this.escrit; }
	public void setEscrit(Escrit e) { this.escrit = e; }
	
	public Centre getCentre() { return this.centre; }
	public void setCentre(Centre s) { this.centre = s; }
	
	public Subcentre getSubcentre() { return this.subcentre; }
	public void setSubcentre(Subcentre s) { this.subcentre = s; }
	
	public Materia getMateria() { return this.materia; }
	public void setMateria(Materia m) { this.materia = m; }
	
	public Motiu getMotiu() { return this.motiu; }
	public void setMotiu(Motiu m) { this.motiu = m; }
	
	public Queixa getQueixa() { return this.queixa; }
	public void setQueixa(Queixa q) { this.queixa = q; }
	
	public Entrada getEntrada() { return this.entrada; }
	public void setEntrada(Entrada e) { this.entrada = e; }
	
	public Idioma getIdioma() { return this.idioma; }
	public void setIdioma(Idioma i) { this.idioma = i; }
	
	public Municipi getMunicipi() { return this.municipi; }
	public void setMunicipi(Municipi m) { this.municipi = m; }
	
	public Identificacio getIdentificacio() { return this.identificacio; }
	public void setIdentificacio(Identificacio i) { this.identificacio = i; }
	
	public String getNumidentificacio() { return this.num_identificacio; }
	public void setNumidentificacio(String value) { this.num_identificacio = value; }
	
	public String getNom() { return this.nom; }
	public void setNom(String n) { this.nom = n; }
	
	public String getLlinatge1() { return this.llinatge1; }
	public void setLlinatge1(String l) { this.llinatge1 = l; }
			
	public String getLlinatge2() { return this.llinatge2; }
	public void setLlinatge2(String l) { this.llinatge2 = l; }
	
	public String getTelefon() { return this.telefon; }
	public void setTelefon(String t) { this.telefon = t; }
	
	public String getEmail() { return this.email; }
	public void setEmail(String e) { this.email = e; }
	
	public String getDireccio() { return this.direccio; }
	public void setDireccio(String d) { this.direccio = d; }
	
	public String getNumero() { return this.numero; }
	public void setNumero(String n) { this.numero = n; }
	
	public String getPis() { return this.pis; }
	public void setPis(String p) { this.pis = p; }
	
	public String getCodipostal() { return this.codi_postal; }
	public void setCodipostal(String cp) { this.codi_postal = cp; }
	
	public String getIdExpedientArxiuCAIB() { return this.id_expedient_arxiu_caib; }
	public void setIdExpedientArxiuCAIB(String id) { this.id_expedient_arxiu_caib =id; }
	
	public String getUrlVersioImprimibleArxiuCAIB() { return this.url_versio_imprimible_expedient_arxiu_caib; }
	public void setUrlVersioImprimibleArxiuCAIB(String url) { this.url_versio_imprimible_expedient_arxiu_caib = url; }
	
	// TODO: Tal volta aquesta funció no ha d'estar aquí
	public Date getDatavenciment()
	{
		// convert date to calendar
	    Calendar c = Calendar.getInstance();
	    c.setTime(this.data_entrada);
	    c.add(Calendar.DATE, 20); // TODO: La norma dia 15 dies hàbils. S'ha de crear una taula amb els festius i afinar l'algorisme, Toni Juanico
	    
	    return c.getTime();
	}
	
}
