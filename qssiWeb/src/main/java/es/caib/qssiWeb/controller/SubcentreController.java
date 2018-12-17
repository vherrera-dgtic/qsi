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

/**
 * Controlador de la vista Subcentre
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="SubcentreController")
@ViewScoped
public class SubcentreController {

	// Propietats
	private final static Logger LOGGER = Logger.getLogger(SubcentreController.class);
	
	private InitialContext ic;
	
	private Integer centre = 0;
	private ArrayList<Centre> llista_centres;
	private ArrayList<Subcentre> llista_subcentres;
		
	// UI HTML
	private DataTable taula_subcentres;
	
		
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
		LOGGER.info("Proxy a SubcentreController ");
		refrescaLlista_centres();
	}
	
	private void refrescaLlista_centres() { 
			
		CentreServiceInterface CentreServ;
		
		// Obtenim la llista de centres
		LOGGER.info("Obtenim llista de centres");
		try {
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.CentreService");
				
			LOGGER.info("EJB lookup" + CentreServ);
				
			this.llista_centres = CentreServ.getLlista_CentresActiusWeb();
							
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: " + CentreServ.getError());
			}
				
				
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
		}
	}
	
	private void refrescaLlista_Subcentres(Integer c)
	{
		  
	   	SubcentreServiceInterface SubcentreServ;
	 		  	
	   	// Obtenim llista de subcentres
	   	LOGGER.info("Obtenim la llista de subcentres: " + c);
	   	this.llista_subcentres = new ArrayList<Subcentre>();
	   	
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
			LOGGER.info("EJB lookup "+ SubcentreServ);	
	 			
			this.llista_subcentres = SubcentreServ.getLlista_Subcentres(c); // Cridem l'EJB
	 			
			LOGGER.info("Obtinguda llista de subcentres "+ SubcentreServ);	
	 			
			if (!SubcentreServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
		}
	 			
	}
	// Crida AJAX per actualitzar els subcentres
	public void onCentre_change() {
		LOGGER.info("onCentre_change");
		refrescaLlista_Subcentres(this.centre);
	}
	
	public void remove()
	{
		
		Subcentre c = (Subcentre) this.taula_subcentres.getRowData();
		SubcentreServiceInterface SubcentreServ;
		
		LOGGER.info("Entram a remove amb paràmetre: " + c.getId());
		
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");
			LOGGER.info("EJB lookup " + SubcentreServ);
			
			SubcentreServ.removeSubcentre(c.getId());
		    			
			if (!SubcentreServ.getResultat())
			{
				LOGGER.info("Error a remove: " + SubcentreServ.getError());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", SubcentreServ.getError()));
			}
			else
			{
				refrescaLlista_Subcentres(this.centre);
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcentre eliminat correctament", "Subcentre eliminat correctament"));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", e.toString()));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", e.toString()));
		}
		
	}
	
}
