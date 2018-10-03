package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

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
public class SubcentreController {

	private final static Logger LOGGER = Logger.getLogger(ExpedientController.class);
	private  static Integer CRIDADES = 0;
	
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	private ArrayList<Subcentre> llista_subcentres;
	private Integer centre = 0;
	private Integer subcentre = 0;
	
	@PostConstruct
	public void init() {
		CRIDADES = CRIDADES + 1;
		
		LOGGER.info("Proxy a SubcentreController " + CRIDADES);
	}
	public boolean getAmbErrors() { return this.ambErrors; }
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
	public ArrayList<Subcentre> getLlista_Subcentres() { return this.llista_subcentres; }
	public void setCentre(Integer centre) { this.centre = centre;  }
	public Integer getCentre() { return this.centre; }
	public void setSubcentre(Integer subcentre) { this.subcentre = subcentre; }
	public Integer getSubcentre() { return this.subcentre; }
	
	// Crida AJAX per actualitzar els subcentres
	public void onCentre_change() {
	    	
	    	LOGGER.info("Proxy a SubcentreController --> onCentre_change");
	     	this.llista_subcentres = new ArrayList<Subcentre>();  
	     	SubcentreServiceInterface SubcentreServ;
	 		  	
	     	// Obtenim llista de matèries
	 		LOGGER.info("Obtenim llista de subcentres a partir del canvi de centre ");
	 		
	 		try
	 		{
	 			ic = new InitialContext();
	 			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
	 			LOGGER.info("EJB lookup "+ SubcentreServ);	
	 			
	 			this.llista_subcentres = SubcentreServ.getLlista_Subcentres(this.centre); // Cridem l'EJB
	 			
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
}
