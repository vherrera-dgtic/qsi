package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.entity.Motiu;
import es.caib.qssiEJB.entity.Municipi;
import es.caib.qssiEJB.entity.Provincia;
import es.caib.qssiEJB.entity.Queixa;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Entrada;
import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.entity.Idioma;
import es.caib.qssiEJB.entity.Identificacio;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.EntradaServiceInterface;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;
import es.caib.qssiEJB.interfaces.IdentificacioServiceInterface;
import es.caib.qssiEJB.interfaces.IdiomaServiceInterface;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;
import es.caib.qssiEJB.interfaces.MotiuServiceInterface;
import es.caib.qssiEJB.interfaces.MunicipiServiceInterface;
import es.caib.qssiEJB.interfaces.ProvinciaServiceInterface;
import es.caib.qssiEJB.interfaces.QueixaServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Controlador de la vista Expedient
 * @author [u97091] Toni Juanico Soler
 * data: 28/08/2018
 */

@ManagedBean
@ViewScoped
public class ExpedientController {
	
	// EJB's
	@EJB
	MateriaServiceInterface MateriaServ;
	
	@EJB
	MotiuServiceInterface MotiuServ;
	
	@EJB
	EscritServiceInterface EscritServ;
	
	@EJB
	QueixaServiceInterface QueixaServ;
	
	@EJB
	SubcentreServiceInterface SubcentreServ;
	
	@EJB
	MunicipiServiceInterface MunicipiServ;
	
	@EJB
	ExpedientServiceInterface ExpedientServ;
	
	@EJB
	EntradaServiceInterface EntradaServ;
	
	@EJB
	IdentificacioServiceInterface IdentificacioServ;
	
	@EJB
	IdiomaServiceInterface IdiomaServ;
	
	@EJB
	ProvinciaServiceInterface ProvinciaServ;
	
	@EJB
	CentreServiceInterface CentreServ;
		
	// Private properties
	private final static Logger LOGGER = Logger.getLogger(ExpedientController.class);
	private static Integer CRIDADES = 0;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	private ArrayList<Subcentre> llista_subcentres;
	private ArrayList<Municipi> llista_municipis;
	
	private String expedientId = new String("");
	private String assumpte;
	private String unitatorganica;
	private Integer centre = 0;
	private Integer subcentre = 0;
	private Integer escrit = 0;
	private Integer materia = 0;
	private Integer motiu = 0;
	private Integer queixa = 0;
	private Integer entrada = 0;
	private String metoderesposta = new String("mail");
	private Integer provincia = 0;
	private Integer municipi = 0;
	private Integer identificacio = 0;
	private Integer idioma = 0;
	private String textpeticio = new String("");
	private String correu = new String("");
	private Date dataEntrada = new Date();
	private String numidentificacio = new String("");
	private String nom = new String("");
	private String llinatge1 = new String("");
	private String llinatge2 = new String("");
	private String telefon = new String("");
	private String direccio = new String("");
	private String numero = new String("");
	private String pis = new String("");
	private String codipostal = new String("");
	private Integer estat = 0;
	private String nom_estat = new String("");
	private String nom_centre_gestor = new String("");
	private String nom_subcentre = new String("");
	private String nom_tipus_escrit = new String("");
	private String nom_materia = new String("");
	private String nom_motiu = new String("");
	private String nom_tipus_queixa = new String("");
	private String nom_tipus_entrada = new String("");
	private String nom_tipus_identificacio = new String("");
	private String nom_llengua = new String("");
	private String nom_metoderesposta = new String("");
	private String nom_provincia = new String("");
	private String nom_municipi = new String("");
	private String dir3_centre_gestor = new String("");
	private String dir3_subcentre = new String("");
	private String mostrarAP = new String("");
	private String usuari_assignat = new String("");
	private String text_resposta = new String("");
	
	private String messages = new String("");
	
	private String actionSelected = new String("");
	
	private List<UploadedFile> llista_fitxers_adjunts = new ArrayList<UploadedFile>();
		 
	// Getters & Setters
	public void setExpedientId(String expedientId) { this.expedientId = expedientId; }
	public String getExpedientId() { return this.expedientId; }
	
	public void setAssumpte(String a) { this.assumpte = a; }
    public String getAssumpte() { return this.assumpte; }
    
	public ArrayList<Subcentre> getLlista_Subcentres() { return this.llista_subcentres; }
    public ArrayList<Municipi> getLlista_Municipis() { return this.llista_municipis; }
    
    public void setCentre(Integer centre) { this.centre = centre;  }
    public Integer getCentre() { return this.centre; }
    
    public void setSubcentre(Integer subcentre) { this.subcentre = subcentre; }
    public Integer getSubcentre() { return this.subcentre; }
    
    public void setUnitatorganica(String uo) { this.unitatorganica = uo; }
    public String getUnitatorganica() { return this.unitatorganica; }
    
    public void setEscrit(Integer escrit) { this.escrit = escrit;  }
    public Integer getEscrit() { return this.escrit; }
    
    public void setMateria(Integer materia) { this.materia = materia;  }
    public Integer getMateria() { return this.materia; }
    
    public void setMotiu(Integer motiu) { this.motiu = motiu;  }
    public Integer getMotiu() { return this.motiu; }
    
    public void setQueixa(Integer queixa) { this.queixa = queixa;  }
    public Integer getQueixa() { return this.queixa; }
    
    public void setEntrada(Integer entrada) { this.entrada = entrada;  }
    public Integer getEntrada() { return this.entrada; }
    
    public void setMetoderesposta(String mresp) { this.metoderesposta = mresp;  }
    public String getMetoderesposta() { return this.metoderesposta; }
    
    public void setProvincia(Integer provincia) { this.provincia = provincia; }
    public Integer getProvincia() { return this.provincia; }
    
    public void setMunicipi(Integer municipi) { this.municipi = municipi; }
    public Integer getMunicipi() { return this.municipi; }
    
    public void setIdentificacio(Integer identificacio) { this.identificacio = identificacio; }
    public Integer getIdentificacio() { return this.identificacio; }
    
    public void setIdioma(Integer idioma) { this.idioma = idioma; }
    public Integer getIdioma() { return this.idioma; }
    
    public void setTextpeticio(String text) { this.textpeticio = text; }
    public String getTextpeticio() { return this.textpeticio; }
    
    public void setCorreu(String correu) { this.correu = correu; }
    public String getCorreu() { return this.correu; }
    
    public void setDataEntrada(Date dataEntrada) { this.dataEntrada = dataEntrada; }
    public Date getDataEntrada() { return this.dataEntrada; }
    
    public void setNumidentificacio(String ni) { this.numidentificacio = ni; }
    public String getNumidentificacio() { return this.numidentificacio; }
    
    public void setNom(String n) { this.nom = n; }
    public String getNom() { return this.nom; }
    
    public void setLlinatge1(String ll) { this.llinatge1 = ll; }
    public String getLlinatge1() { return this.llinatge1; }
    
    public void setLlinatge2(String ll) { this.llinatge2 = ll; }
    public String getLlinatge2() { return this.llinatge2; }
    
    public void setTelefon(String t) { this.telefon = t; }
    public String getTelefon() { return this.telefon; }
    
    public void setNumero(String n) { this.numero = n; }
    public String getNumero() { return this.numero; }
    
    public void setDireccio(String d) { this.direccio = d; }
    public String getDireccio() { return this.direccio; }
    
    public void setPis(String p) { this.pis = p; }
    public String getPis() { return this.pis; }
    
    public void setCodipostal(String cp) { this.codipostal = cp; }
    public String getCodipostal() { return this.codipostal; }
    
    public void setEstat(Integer e) { this.estat = e; }
    public Integer getEstat() { return this.estat; }
    
    public void setNomEstat(String n) { this.nom_estat = n; }
    public String getNomEstat() { return this.nom_estat; }
    
    public void setNomCentreGestor(String n) { this.nom_centre_gestor = n; }
    public String getNomCentreGestor() { return this.nom_centre_gestor; }
    
    public void setDir3CentreGestor(String n) { this.dir3_centre_gestor = n; }
    public String getDir3CentreGestor() { return this.dir3_centre_gestor; }
    
    public void setNomsubcentre(String n) { this.nom_subcentre = n; }
    public String getNomsubcentre() { return this.nom_subcentre; }
    
    public void setDir3subcentre(String n) { this.dir3_subcentre = n; }
    public String getDir3subcentre() { return this.dir3_subcentre; }
    
    public void setNomtipusescrit(String n) { this.nom_tipus_escrit = n; }
    public String getNomtipusescrit() { return this.nom_tipus_escrit; }
    
    public void setNommateria(String n) {this.nom_materia = n; }
    public String getNommateria() { return this.nom_materia; }
    
    public void setNomtipusqueixa(String n) {this.nom_tipus_queixa = n; }
    public String getNomtipusqueixa() { return this.nom_tipus_queixa; }
    
    public void setNommotiu(String n) {this.nom_motiu = n; }
    public String getNommotiu() { return this.nom_motiu; }
    
    public void setNomtipusentrada(String n) {this.nom_tipus_entrada = n; }
    public String getNomtipusentrada() { return this.nom_tipus_entrada; }
    
    public void setNomtipusidentificacio(String n) {this.nom_tipus_identificacio = n; }
    public String getNomtipusidentificacio() { return this.nom_tipus_identificacio; }
    
    public void setNomllengua(String n) { this.nom_llengua = n; }
    public String getNomllengua() { return this.nom_llengua; }
    
    public void setNommetoderesposta(String n) { this.nom_metoderesposta = n; }
    public String getNommetoderesposta() { return this.nom_metoderesposta; }
    
    public void setNomprovincia(String n) { this.nom_provincia = n; }
    public String getNomprovincia() { return this.nom_provincia; }
    
    public void setNommunicipi(String n) { this.nom_municipi = n; }
    public String getNommunicipi() { return this.nom_municipi; }
    
    public void setMostrarAP(String v) { this.mostrarAP = v; }
    public String getMostrarAP() { return this.mostrarAP; }
    
    public void setTextresposta(String t) { this.text_resposta = t; }
    public String getTextresposta() { return this.text_resposta; }
    
    public void setMessages(String m) { this.messages = m; }
    public String getMessages() { return this.messages; }
        
    public void setActionSelected(String a) { this.actionSelected = a; }
    public String getActionSelected() { return this.actionSelected; }
    
    public void setUsuariassignat(String u) { this.usuari_assignat = u; }
    public String getUsuariassignat() { return this.usuari_assignat; }
        
    
    public String getFilesNames() 
    { 
    	String resultat = new String("");
    	for (UploadedFile e : this.llista_fitxers_adjunts) {
			resultat = resultat + " " + e.getFileName();
		}
    	return resultat;
    }
	
	
	// Methods
	@PostConstruct
	public void init() {
		CRIDADES = CRIDADES + 1;
		
		LOGGER.info("Proxy a ExpedientController " + CRIDADES);
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("expedientId_param");
				
		if (param!=null) {
			LOGGER.info("ExpedientController amb expedientId_param= "+param);
			this.expedientId = param;
			this.getExpedientInfo(this.expedientId);
		}
	}
	
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Materia> getLlista_Materies() { 

		
		ArrayList<Materia> llista_Materies = null;
		
		// Obtenim llista de mat�ries
		LOGGER.info("Obtenim llista de mat�ries ");
		
		try
		{
				
			LOGGER.info("EJB lookup "+ MateriaServ);	
			
			llista_Materies = MateriaServ.getLlista_Materies(); // Cridem l'EJB
			
			if (!MateriaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				this.ambErrors = true;
				this.message = MateriaServ.getError();
			}
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Materies;
		
	}
	public ArrayList<Motiu> getLlista_Motius() { 
		
		
		ArrayList<Motiu> llista_Motius = null;
		
		try {
			
			
			// Obtenim llista de motius
			LOGGER.info("Obtenim llista de motius");
			
			llista_Motius = MotiuServ.getLlista_Motius(); // Cridem l'EJB
			
			if (!MotiuServ.getResultat())
			{
				LOGGER.info("error obtingut+-: " + MotiuServ.getError());
				this.ambErrors = true;
				this.message = this.message + " -- " + MotiuServ.getError();
			}
			
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Motius;
		
	}
	public ArrayList<Escrit> getLlista_Escrits() { 
		
		ArrayList<Escrit> llista_Escrits = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'escrits");
		try {
			LOGGER.info("EJB lookup" + EscritServ);
			
			llista_Escrits = EscritServ.getLlista_Escrits();
						
			if (!EscritServ.getResultat())
			{
				LOGGER.info("error obtingut: " + EscritServ.getError());
				this.ambErrors = true;
				this.message = EscritServ.getError();
			}
			
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Escrits; 
		
	}
    public ArrayList<Queixa> getLlista_Queixes() { 
		
		
		ArrayList<Queixa> llista_Queixes = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista de queixes");
		try {
						
			LOGGER.info("EJB lookup" + QueixaServ);
			
			llista_Queixes = QueixaServ.getLLista_queixes();
						
			if (!QueixaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + QueixaServ.getError());
				this.ambErrors = true;
				this.message = QueixaServ.getError();
			}
			
			
		
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Queixes; 
		
	}
    public ArrayList<Entrada> getLlista_Entrades() { 
		
		
		ArrayList<Entrada> llista_Entrades = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'entrades");
		try {
						
			LOGGER.info("EJB lookup" + EntradaServ);
			
			llista_Entrades = EntradaServ.getLlista_Entrades();
						
			if (!EntradaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + EntradaServ.getError());
				this.ambErrors = true;
				this.message = EntradaServ.getError();
			}
			
			
		}  catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Entrades; 
		
	}
    public ArrayList<Idioma> getLlista_Idiomes() {
    	
		ArrayList<Idioma> llista_Idiomes = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'idiomes");
		try {
						
			LOGGER.info("EJB lookup" + IdiomaServ);
			
			llista_Idiomes = IdiomaServ.getLlista_Idiomes();
						
			if (!IdiomaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IdiomaServ.getError());
				this.ambErrors = true;
				this.message = IdiomaServ.getError();
			}
			
			
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Idiomes; 
    }  
    public ArrayList<Provincia> getLlista_Provincies() {
    	
		ArrayList<Provincia> llista_Provincies = null;
		
		LOGGER.info("Obtenim llista de provincies");
		try {
						
			LOGGER.info("EJB lookup" + ProvinciaServ);
			
			llista_Provincies = ProvinciaServ.getLlista_Provincies();
						
			if (!ProvinciaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + ProvinciaServ.getError());
				this.ambErrors = true;
				this.message = ProvinciaServ.getError();
			}
			
			
		}catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Provincies; 
    }
    
    public ArrayList<Centre> getLlista_Centres() { 
		
		ArrayList<Centre> llista_Centres = null;
		
		LOGGER.info("Obtenim llista de centres");
		try {
						
			LOGGER.info("EJB lookup" + CentreServ);
			
			llista_Centres = CentreServ.getLlista_CentresActiusWeb();
						
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: " + CentreServ.getError());
				this.ambErrors = true;
				this.message = CentreServ.getError();
			}
			
			
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Centres; 
		
	}
    public ArrayList<Identificacio> getLlista_Identificacions() {
    	
		ArrayList<Identificacio> llista_Identificacions = null;
		
		LOGGER.info("Obtenim llista d'identificacions");
		try {
						
			LOGGER.info("EJB lookup" + IdentificacioServ);
			
			llista_Identificacions = IdentificacioServ.getLlista_Identificacions();
						
			if (!IdentificacioServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IdentificacioServ.getError());
				this.ambErrors = true;
				this.message = IdentificacioServ.getError();
			}
			
			
		}  catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Identificacions; 
			
    }
    
    // Crida AJAX per actualitzar els subcentres
    public void onCentre_change() {
    	
    	LOGGER.info("Proxy a ExpedientController --> onCentre_change");
     	this.llista_subcentres = new ArrayList<Subcentre>();  
     	
 		LOGGER.info("Obtenim llista de subcentres a partir del canvi de centre ");
 		
 		try
 		{
 			LOGGER.info("EJB lookup "+ SubcentreServ);	
 			
 			this.llista_subcentres = SubcentreServ.getLlista_SubcentresActiusWeb(this.centre); // Cridem l'EJB
 			 			 			
 			LOGGER.info("Obtinguda llista de subcentres "+ SubcentreServ);	
 			
 			if (!SubcentreServ.getResultat())
 			{
 				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
 				this.ambErrors = true;
 				this.message = SubcentreServ.getError();
 			}
 		} catch (Exception e) {
 			LOGGER.info("Error_+ " + e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		}
 		
    }
    
    // Crida AJAX per actualitzar els municipis
    public void onProvincia_change() {
    	LOGGER.info("Proxy a ExpedientController --> onProvincia_change");
     	this.llista_municipis = new ArrayList<Municipi>();  
     	 		  	
     	// Obtenim llista de mat�ries
 		LOGGER.info("Obtenim llista de municipis a partir de la provincia ");
 		
 		try
 		{
 				
 			LOGGER.info("EJB lookup "+ MunicipiServ);	
 			
 			this.llista_municipis = MunicipiServ.getLlista_MunicipisActius(this.provincia); // Cridem l'EJB
 			
 			LOGGER.info("Obtinguda llista de municipis "+ MunicipiServ);	
 			
 			if (!MunicipiServ.getResultat())
 			{
 				LOGGER.info("error obtingut: "+ MunicipiServ.getError());
 				this.ambErrors = true;
 				this.message = MunicipiServ.getError();
 			}
 		
 		} catch (Exception e) {
 			LOGGER.info("Error_+ " + e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		}
    }
    
    public void addExpedient() {
    	
		LOGGER.info("addExpedient ");
		
		try
		{
			LOGGER.info("EJB lookup "+ ExpedientServ);	
			
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			// Contruim l'expedient a donar d'alta
			Expedient exp = new Expedient();
			
			exp.setAssumpte(this.assumpte);
			exp.setUnitatOrganica(this.unitatorganica);
			exp.setDataentrada(this.dataEntrada); // Data d'entrada -> l'establert per l'usuari
			exp.setDatacreacio(new Date()); // Data creaci� -> avui
			exp.setViaContestacio(this.metoderesposta);
			exp.setTextPeticio(this.textpeticio);
			exp.setEstat(ExpedientServiceInterface.EstatExpedient.ASSIGNAT_EQUIP_FILTRATGE); // Estat inicial -> Assignat a Equip de Filtratge, Toni Juanico, 04/07/2019
			exp.setNumidentificacio(this.numidentificacio);
			exp.setNom(this.nom);
			exp.setLlinatge1(this.llinatge1);
			exp.setLlinatge2(this.llinatge2);
			exp.setTelefon(this.telefon);
			exp.setEmail(this.correu);
			
			Centre c = new Centre();
			c.setId(this.centre);
			exp.setCentre(c);
			
			if (this.subcentre != null && this.subcentre != 0)
			{
				Subcentre sc = new Subcentre();
				sc.setId(this.subcentre);
				exp.setSubcentre(sc);	
			}
					
			Escrit e = new Escrit();
			e.setId(this.escrit);
			exp.setEscrit(e);
			
			Materia m = new Materia();
			m.setId(this.materia); 
			exp.setMateria(m);
		
			Motiu mo = new Motiu();
			mo.setId(this.motiu);
			exp.setMotiu(mo);
			
			Queixa q = new Queixa();
			q.setId(this.queixa);
			exp.setQueixa(q);
			
			Entrada en = new Entrada();
			en.setId(this.entrada);
			exp.setEntrada(en);
			
			Idioma i = new Idioma();
			i.setId(this.idioma);
			exp.setIdioma(i);
			
			Identificacio id = new Identificacio();
			id.setId(this.identificacio);
			exp.setIdentificacio(id);
			
			// Si ens han eligit el m�tode resposta per correu postal afegim la informaci� postal
			
			LOGGER.info("Aix� �s un m�tode de resposta: " + this.metoderesposta);
			if (this.metoderesposta.equals("postal")) {
				LOGGER.info("in postal: " + this.metoderesposta);
				Municipi mu = new Municipi();
				mu.setId(this.municipi);
				exp.setMunicipi(mu);
				
				exp.setDireccio(this.direccio);
				exp.setNumero(this.numero);
				exp.setPis(this.pis);
				exp.setCodipostal(this.codipostal);		
			}
		
			ExpedientServ.addExpedient(exp); // Cridem l'EJB
			
			// Aqu� hem de validar els fitxers pujats
			for (UploadedFile file : this.llista_fitxers_adjunts) {
				LOGGER.info("Enviar fitxer a Arxiu CAIB i desar identificaci�" + file.getFileName() + " - " + file.getSize());
				
				DocumentEstat estat_document = DocumentEstat.ESBORRANY;
				DocumentFormat format_document = getDocumentFormat(file.getContentType());
				DocumentExtensio extensio_document = getDocumentExtensio(file.getContentType());
				String tipus_mime = file.getContentType();
				String nom_document = file.getFileName();
				boolean resultat_enviar_fitxer_ArxiuCAIB = ExpedientServ.enviar_fitxer_ArxiuCAIB(exp.getId(),exp.getNumidentificacio(),null,null,
						                              											estat_document,
						                              											format_document,
						                              											extensio_document,
						                              											tipus_mime,
						                              											nom_document,
						                              											file.getContents()); 
				if (!resultat_enviar_fitxer_ArxiuCAIB)
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error afegint fitxer a ArxiuCAIB", "Error afegint fitxer a ArxiuCAIB"));				
				}
			}
			
			if (ExpedientServ.getResultat()==true)
			{
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Expedient afegit correctament", "Expedient afegit correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/index.xhtml");
			}
			else
			{
				
				LOGGER.info("Error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint expedient",  ExpedientServ.getError()));
			}
		} catch (Exception ex) {
			
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant l'expedient", ex.toString()));
		}
    }

    private DocumentFormat getDocumentFormat(String contentType)
    {
    	switch (contentType)
    	{
    	case "image/jpeg": return DocumentFormat.JPEG; 
    	case "image/png": return DocumentFormat.PNG;
    	case "application/vnd.oasis.opendocument.text": return DocumentFormat.OASIS12; // Per odt
    	case "application/octet-stream" : return DocumentFormat.OASIS12; // Per docx
    	case "application/pdf": return DocumentFormat.PDF;
    	default: return DocumentFormat.PDF; // Ojo, hauriem de retornar error, Toni Juanico
    	}
    }
    
    private DocumentExtensio getDocumentExtensio(String contentType)
    {
    	switch (contentType)
    	{
    	case "image/jpeg": return DocumentExtensio.JPEG; 
    	case "image/png": return DocumentExtensio.PNG;
    	case "application/vnd.oasis.opendocument.text": return DocumentExtensio.ODT; // Per odt
    	case "application/octet-stream" : return DocumentExtensio.DOCX; // Per docx
    	case "application/pdf": return DocumentExtensio.PDF;
    	default: return DocumentExtensio.PDF; // Ojo, hauriem de retornar error, Toni Juanico
    	}
    }
    private void getExpedientInfo(String expedientId) {
    	
    	
		LOGGER.info("getExpedientInfo: " + expedientId);
			
		try
		{
			Expedient e = new Expedient();
			e = ExpedientServ.getExpedient(Integer.parseInt(expedientId));
				
			if (ExpedientServ.getResultat())
			{
				this.expedientId = expedientId;
				this.assumpte = e.getAssumpte();
				this.unitatorganica = e.getUnitatOrganica();
				this.metoderesposta = e.getViaContestacio();
				this.textpeticio = e.getTextPeticio();
				this.correu = e.getEmail();
				this.dataEntrada = e.getDataentrada();
				this.numidentificacio = e.getNumidentificacio();
				this.nom = e.getNom();
				this.llinatge1 = e.getLlinatge1();
				this.llinatge2 = e.getLlinatge2();
				this.telefon = e.getTelefon();
				this.direccio = e.getDireccio();
				this.numero = e.getNumero();
				this.pis = e.getPis();
				this.codipostal = e.getCodipostal();
				this.estat = e.getEstat();
				this.centre = e.getCentre().getId();
				this.text_resposta = e.getTextResposta();
				this.onCentre_change();
								
				// Etiquetes
				this.nom_estat = ExpedientServiceInterface.EstatExpedient.valueOf(this.estat).getTag();
				this.nom_centre_gestor = e.getCentre().getNom();
				
				if (e.getSubcentre() != null)
				{
					this.nom_subcentre = e.getSubcentre().getNom();
					this.dir3_subcentre = e.getSubcentre().getDir3();
					this.subcentre = e.getSubcentre().getId();
				}
					
				
				this.nom_tipus_escrit = e.getEscrit().getNom();
				this.nom_materia = e.getMateria().getNom();
				this.nom_motiu = e.getMotiu().getNom();
				this.nom_tipus_queixa = e.getQueixa().getNom();
				this.nom_tipus_entrada = e.getEntrada().getNom();
				this.nom_tipus_identificacio = e.getIdentificacio().getNom();
				this.nom_llengua = e.getIdioma().getNom();
				this.dir3_centre_gestor = e.getCentre().getDir3();
				this.nom_metoderesposta = e.getViaContestacio();
				
				if (this.nom_metoderesposta.equals("postal")) {
					this.mostrarAP = "";
					this.nom_municipi= e.getMunicipi().getNom();
					this.nom_provincia = e.getMunicipi().getProvincia().getNom();
				}
				else
					this.mostrarAP = "display:none";
			}
			else
			{
				LOGGER.info("error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint l'expedient",  ExpedientServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint l'expedient",  ex.toString()));
		}	
    }
    
    public ExpedientServiceInterface.AccioExpedient[] getAccionsDisponibles()
    {
    	ExpedientServiceInterface.AccioExpedient[] resultat = null;
		LOGGER.info("obtenirAccionsDisponibles: ");
		
		try
		{
			resultat = ExpedientServ.getAccionsDisponiblesExpedient(ExpedientServiceInterface.EstatExpedient.valueOf(this.estat));
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint accions disponibles",  ex.toString()));
		}
        return resultat;
    } 
    
    // Funci� per canviar l'estat d'un expedient. Assignat equip de filtratge --> Assignat Responsable Conselleria
    public void assignarCentre() {
    	
		LOGGER.info("assignarCentre, param: expedient: " + expedientId + "  centre: " + this.getCentre());
			
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		try
		{
			ExpedientServ.assignarCentreExpedient(Integer.parseInt(expedientId),this.getCentre());
				
			if (ExpedientServ.getResultat())
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Expedient actualitzat correctament", "Expedient actualitzat correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/index_admin.xhtml?f=estat");
			}
			else
			{
				LOGGER.info("Error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error actualitzant expedient",  ExpedientServ.getError()));
			}
		}
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant l'expedient", ex.toString()));
		}
    	
    }
    
    public void retornarequipfiltratge() {
    	FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "TODO: S'ha d'implementar la funcionalitat", "TODO: S'ha d'implementar la funcionalitat"));
    }
    
    public void assignarTramitador() {
    	
		LOGGER.info("assignarTramitador, param: expedient: " + expedientId + "  centre: " + this.getCentre());
			
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		try
		{
			ExpedientServ.assignarTramitador(Integer.parseInt(expedientId),this.getSubcentre(), this.getUnitatorganica(), this.getUsuariassignat());
				
			if (ExpedientServ.getResultat())
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Expedient actualitzat correctament", "Expedient actualitzat correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/index_admin.xhtml?f=estat");
			}
			else
			{
				LOGGER.info("Error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error actualitzant expedient",  ExpedientServ.getError()));
			}
		}
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant l'expedient", ex.toString()));
		}
    	   	
    }
    
    public void rebutjarExpedient() {
    	FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "TODO: expedient a cancel�lar", "cancel�lant..."));
    }
   
    public void tancarExpedient() {
		LOGGER.info("tancarExpedient, param: expedient: " + expedientId);
		
		try
		{
			
			ExpedientServ.tancarExpedient(Integer.parseInt(expedientId));
				
			if (ExpedientServ.getResultat())
			{
				LOGGER.info("Expedient tancat correctament");
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Expedient tancat correctament", "Expedient tancat correctament"));
			}
			else
			{
				LOGGER.info("Error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error tancant expedient",  ExpedientServ.getError()));
			}
		}
		catch(Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error tancant l'expedient", ex.toString()));			
		}	
    }
    
    public void desarRespostaExpedient() {
    	
		LOGGER.info("desarRespostaExpedient, param: expedient: " + expedientId);
		
		try
		{
			
			ExpedientServ.desarRespostaExpedient(Integer.parseInt(expedientId),this.text_resposta);
				
			if (ExpedientServ.getResultat())
			{
				LOGGER.info("Resposta desada correctament");
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Resposta desada correctament", "Resposta desada correctament"));
			}
			else
			{
				LOGGER.info("Error obtingut: "+ ExpedientServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant expedient",  ExpedientServ.getError()));
			}
		}
		catch(Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant l'expedient", ex.toString()));			
		}
    }
    
    public void handleFileUpload(FileUploadEvent event) {
    	LOGGER.info("handleFileUpload");
       
    	this.llista_fitxers_adjunts.add(event.getFile());
		// Print out the information of the file
		System.out.println("Uploaded File Name Is :: "+event.getFile().getFileName()+" :: Uploaded File Size :: "+event.getFile().getSize() + " :: Uploaded File Type :: " + event.getFile().getContentType());
		
		FacesMessage msg = new FacesMessage("Adjuntat fitxer correctament: ", event.getFile().getFileName());
	    FacesContext.getCurrentInstance().addMessage("growl", msg);
    }
    
}
