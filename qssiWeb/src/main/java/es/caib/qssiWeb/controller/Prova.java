package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

@ManagedBean
@ViewScoped
public class Prova {
	
	// Propietats
	private final static Logger LOGGER = Logger.getLogger(Prova.class);
	
	private InitialContext ic;
	
	private Integer centre = 0;
	private ArrayList<Centre> llista_centres;
	private ArrayList<Subcentre> llista_subcentres;
	
	// UI HTML
	private DataTable taula_subcentres;
	
	// Setters i Getters de les propietats
	public void setCentre(Integer centre) { this.centre = centre;  }
	public Integer getCentre() { return this.centre; }
	
	public ArrayList<Centre> getLlista_Centres() { return this.llista_centres; }
	public void setLlista_Centres(ArrayList<Centre> lc) { this.llista_centres = lc; }
	
	public ArrayList<Subcentre> getLlista_Subcentres() { return this.llista_subcentres; }
	public void setLlista_Subcentres(ArrayList<Subcentre> lsc) { this.llista_subcentres = lsc; }
	
	public DataTable getTaula_subcentres() { return this.taula_subcentres; }
	public void setTaula_subcentres(DataTable dataTable) { this.taula_subcentres = dataTable; }
	
	
	// Mètodes
	
	@PostConstruct
	public void init() {
		LOGGER.info("init...");
		refreshLlista_Centres();
	}
	
	private void refreshLlista_Centres()
	{
		LOGGER.info("Obtenint centres...");
		CentreServiceInterface CentreServ;
					
		// Obtenim la llista d'escrits
		try {
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.CentreService");
			this.llista_centres = new ArrayList<Centre>();
			this.llista_centres = CentreServ.getLlista_CentresActiusWeb();
							
		} catch (NamingException e) {
			// NOPE
		} catch (Exception e) {
			// NOPE
		}
	}
	
	private void refreshLlista_SubCentres(Integer c)
	{
		LOGGER.info("Obtenint subcentres..." + c);
	   	this.llista_subcentres = new ArrayList<Subcentre>();  
	   	SubcentreServiceInterface SubcentreServ;
	 		  	
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
			LOGGER.info("EJB lookup "+ SubcentreServ);	
	 			
			this.llista_subcentres = SubcentreServ.getLlista_Subcentres(c); // Cridem l'EJB
	 			
			LOGGER.info("Obtinguda llista de subcentres "+ SubcentreServ);	
	 			
		}
		catch (NamingException e) {
			// NOPE 
		} catch (Exception e) {
			// NOPE
		}
		
	}
	
	// Cridades AJAX
	public void onCentre_change() {
		LOGGER.info("onCentre_change");
		refreshLlista_SubCentres(this.centre);
		addMessage("Centre canviat!");
	}
		
	public void buttonAction() {
		LOGGER.info("buttonAction");
		addMessage("Paperera apretada!");
	}
	 
	public void buttonAction2() {
		LOGGER.info("buttonAction2");
		addMessage("Paperera de la TAULA apretada!");
	}
	
	private void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
	    FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
