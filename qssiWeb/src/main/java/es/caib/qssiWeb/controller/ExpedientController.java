package es.caib.qssiWeb.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.entity.Motiu;
import es.caib.qssiEJB.entity.Municipi;
import es.caib.qssiEJB.entity.Queixa;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Entrada;
import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.entity.Idioma;
import es.caib.qssiEJB.entity.Illa;
import es.caib.qssiEJB.entity.Identificacio;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.EntradaServiceInterface;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;
import es.caib.qssiEJB.interfaces.IdentificacioServiceInterface;
import es.caib.qssiEJB.interfaces.IdiomaServiceInterface;
import es.caib.qssiEJB.interfaces.IllaServiceInterface;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;
import es.caib.qssiEJB.interfaces.MotiuServiceInterface;
import es.caib.qssiEJB.interfaces.MunicipiServiceInterface;
import es.caib.qssiEJB.interfaces.QueixaServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;


//import javax.ejb.EJB;

import org.apache.log4j.Logger;

/**
 * Controlador de la vista Expedient
 * @author [u97091] Toni Juanico Soler
 * data: 28/08/2018
 */

@ManagedBean(name="ExpedientController")
@ViewScoped
public class ExpedientController {
	
	// @EJB(name="es.caib.qssiEJB.service.MateriaService"), Toni Juanico, no sabem perquè el servidor JBoss 5.2 no injecta
		// obtenim l'error:
		//    SEVERE [application] JSF1029: The specified InjectionProvider implementation 'org.jboss.web.jsf.integration.injection.JBossInjectionProvider' does not implement the InjectionProvider interface.
		// que creiem que és el que fa que no funciona l'anotació @EJB.
		
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",  this.message));
	
	private final static Logger LOGGER = Logger.getLogger(ExpedientController.class);
	private  static Integer CRIDADES = 0;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	private InitialContext ic;
	
	private ArrayList<Subcentre> llista_subcentres;
	private ArrayList<Municipi> llista_municipis;
	private Integer centre = 0;
	private Integer subcentre = 0;
	private Integer illa = 0;
	private Integer municipi = 0;
	private String correu = new String("");
	private String data = new String("01/01/2018");
	
	private String messages = new String("");
	private String assumpte;
		
	@PostConstruct
	public void init() {
		CRIDADES = CRIDADES + 1;
		
		LOGGER.info("Proxy a ExpedientController " + CRIDADES);
		
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		this.data = simpleDateFormat.format(new Date());
	}
	
	public boolean getAmbErrors() { return this.ambErrors; }
	// Mètodes get - set
	public ArrayList<Materia> getLlista_Materies() { 

		MateriaServiceInterface MateriaServ;
		ArrayList<Materia> llista_Materies = null;
		
		// Obtenim llista de matèries
		LOGGER.info("Obtenim llista de matèries ");
		
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");	
			LOGGER.info("EJB lookup "+ MateriaServ);	
			
			llista_Materies = MateriaServ.getLlista_Materies(); // Cridem l'EJB
			
			if (!MateriaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				this.ambErrors = true;
				this.message = MateriaServ.getError();
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Materies;
		
	}
	public ArrayList<Motiu> getLlista_Motius() { 
		
		MotiuServiceInterface MotiuServ;
		ArrayList<Motiu> llista_Motius = null;
		
		try {
			ic = new InitialContext();
			
			// Obtenim llista de motius
			LOGGER.info("Obtenim llista de motius");
			MotiuServ = (MotiuServiceInterface) ic.lookup("es.caib.qssiEJB.service.MotiuService");
			llista_Motius = MotiuServ.getLlista_Motius(); // Cridem l'EJB
			
			if (!MotiuServ.getResultat())
			{
				LOGGER.info("error obtingut+-: " + MotiuServ.getError());
				this.ambErrors = true;
				this.message = this.message + " -- " + MotiuServ.getError();
			}
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Motius;
		
	}
	public ArrayList<Escrit> getLlista_Escrits() { 
		
		EscritServiceInterface EscritServ;
		ArrayList<Escrit> llista_Escrits = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'escrits");
		try {
			ic = new InitialContext();
			EscritServ = (EscritServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.EscritService");
			
			LOGGER.info("EJB lookup" + EscritServ);
			
			llista_Escrits = EscritServ.getLlista_Escrits();
						
			if (!EscritServ.getResultat())
			{
				LOGGER.info("error obtingut: " + EscritServ.getError());
				this.ambErrors = true;
				this.message = EscritServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Escrits; 
		
	}
    public ArrayList<Queixa> getLlista_Queixes() { 
		
		QueixaServiceInterface QueixaServ;
		ArrayList<Queixa> llista_Queixes = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista de queixes");
		try {
			ic = new InitialContext();
			QueixaServ = (QueixaServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.QueixaService");
			
			LOGGER.info("EJB lookup" + QueixaServ);
			
			llista_Queixes = QueixaServ.getLLista_queixes();
						
			if (!QueixaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + QueixaServ.getError());
				this.ambErrors = true;
				this.message = QueixaServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Queixes; 
		
	}
    public ArrayList<Entrada> getLlista_Entrades() { 
		
		EntradaServiceInterface EntradaServ;
		ArrayList<Entrada> llista_Entrades = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'entrades");
		try {
			ic = new InitialContext();
			EntradaServ = (EntradaServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.EntradaService");
			
			LOGGER.info("EJB lookup" + EntradaServ);
			
			llista_Entrades = EntradaServ.getLlista_Entrades();
						
			if (!EntradaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + EntradaServ.getError());
				this.ambErrors = true;
				this.message = EntradaServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Entrades; 
		
	}
    public ArrayList<Idioma> getLlista_Idiomes() {
    	IdiomaServiceInterface IdiomaServ;
		ArrayList<Idioma> llista_Idiomes = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'idiomes");
		try {
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.IdiomaService");
			
			LOGGER.info("EJB lookup" + IdiomaServ);
			
			llista_Idiomes = IdiomaServ.getLlista_Idiomes();
						
			if (!IdiomaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IdiomaServ.getError());
				this.ambErrors = true;
				this.message = IdiomaServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Idiomes; 
    }
    public ArrayList<Illa> getLlista_Illes() {
    	IllaServiceInterface IllaServ;
		ArrayList<Illa> llista_Illes = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'illes");
		try {
			ic = new InitialContext();
			IllaServ = (IllaServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.IllaService");
			
			LOGGER.info("EJB lookup" + IllaServ);
			
			llista_Illes = IllaServ.getLlista_Illes();
						
			if (!IllaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IllaServ.getError());
				this.ambErrors = true;
				this.message = IllaServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Illes; 
    }
    public ArrayList<Centre> getLlista_Centres() { 
		
		CentreServiceInterface CentreServ;
		ArrayList<Centre> llista_Centres = null;
		
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista de centres");
		try {
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.CentreService");
			
			LOGGER.info("EJB lookup" + CentreServ);
			
			llista_Centres = CentreServ.getLlista_CentresActiusWeb();
						
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: " + CentreServ.getError());
				this.ambErrors = true;
				this.message = CentreServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Centres; 
		
	}
    public ArrayList<Identificacio> getLlista_Identificacions() {
    	IdentificacioServiceInterface IdentificacioServ;
		ArrayList<Identificacio> llista_Identificacions = null;
		
		// Obtenim la llista d'identificacions
		LOGGER.info("Obtenim llista d'identificacions");
		try {
			ic = new InitialContext();
			IdentificacioServ = (IdentificacioServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.IdentificacioService");
			
			LOGGER.info("EJB lookup" + IdentificacioServ);
			
			llista_Identificacions = IdentificacioServ.getLlista_Identificacions();
						
			if (!IdentificacioServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IdentificacioServ.getError());
				this.ambErrors = true;
				this.message = IdentificacioServ.getError();
			}
			
			
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return llista_Identificacions; 
			
    }
    public ArrayList<Subcentre> getLlista_Subcentres() { return this.llista_subcentres; }
    public ArrayList<Municipi> getLlista_Municipis() { return this.llista_municipis; }
    
    public void setCentre(Integer centre) { this.centre = centre;  }
    public Integer getCentre() { return this.centre; }
    public void setSubcentre(Integer subcentre) { this.subcentre = subcentre; }
    public Integer getSubcentre() { return this.subcentre; }
    public void setIlla(Integer illa) { this.illa = illa; }
    public Integer getIlla() { return this.illa; }
    public void setMunicipi(Integer municipi) { this.municipi = municipi; }
    public Integer getMunicipi() { return this.municipi; }
    public void setCorreu(String correu) { this.correu = correu; }
    public String getCorreu() { return this.correu; }
    public void setData(String data) { this.data = data; }
    public String getData() { return this.data; }
    public void setMessages(String m) { this.messages = m; }
    public String getMessages() { return this.messages; }
    public void setAssumpte(String a) { this.assumpte = a; }
    public String getAssumpte() { return this.assumpte; }
    
    // Crida AJAX per actualitzar els subcentres
    public void onCentre_change() {
    	
    	LOGGER.info("Proxy a ExpedientController --> onCentre_change");
     	this.llista_subcentres = new ArrayList<Subcentre>();  
     	SubcentreServiceInterface SubcentreServ;
 		  	
     	// Obtenim llista de matèries
 		LOGGER.info("Obtenim llista de subcentres a partir del canvi de centre ");
 		
 		try
 		{
 			ic = new InitialContext();
 			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
 			LOGGER.info("EJB lookup "+ SubcentreServ);	
 			
 			this.llista_subcentres = SubcentreServ.getLlista_SubcentresActiusWeb(this.centre); // Cridem l'EJB
 			
 			LOGGER.info("Obtinguda llista de subcentres "+ SubcentreServ);	
 			
 			if (!SubcentreServ.getResultat())
 			{
 				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
 				this.ambErrors = true;
 				this.message = SubcentreServ.getError();
 			}
 		}
 		catch (NamingException e) {
 			LOGGER.info("Error___ "+e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		} catch (Exception e) {
 			LOGGER.info("Error_+ " + e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		}
 		
    }
    
    // Crida AJAX per actualitzar els municipis
    public void onIlla_change() {
    	LOGGER.info("Proxy a ExpedientController --> onIlla_change");
     	this.llista_municipis = new ArrayList<Municipi>();  
     	MunicipiServiceInterface MunicipiServ;
 		  	
     	// Obtenim llista de matèries
 		LOGGER.info("Obtenim llista de municipis a partir de l'illa ");
 		
 		try
 		{
 			ic = new InitialContext();
 			MunicipiServ = (MunicipiServiceInterface) ic.lookup("es.caib.qssiEJB.service.MunicipiService");	
 			LOGGER.info("EJB lookup "+ MunicipiServ);	
 			
 			this.llista_municipis = MunicipiServ.getLlista_MunicipisActius(this.illa); // Cridem l'EJB
 			
 			LOGGER.info("Obtinguda llista de municipis "+ MunicipiServ);	
 			
 			if (!MunicipiServ.getResultat())
 			{
 				LOGGER.info("error obtingut: "+ MunicipiServ.getError());
 				this.ambErrors = true;
 				this.message = MunicipiServ.getError();
 			}
 		}
 		catch (NamingException e) {
 			LOGGER.info("Error___ "+e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		} catch (Exception e) {
 			LOGGER.info("Error_+ " + e.toString());
 			this.ambErrors = true;
 			this.message = this.message + " -- " + e.toString();
 		}
    }
    
    public void saveExpedient() {
        FacesContext context = FacesContext.getCurrentInstance();
         
        context.addMessage(null, new FacesMessage("Successful",  "Your message: " + message) );
        context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
    }
}
