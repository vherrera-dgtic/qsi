package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Illa;
import es.caib.qssiEJB.entity.Municipi;
import es.caib.qssiEJB.interfaces.IllaServiceInterface;
import es.caib.qssiEJB.interfaces.MunicipiServiceInterface;


/**
 * Controlador de la vista Municipi
 * @author [u97091] Toni Juanico Soler
 * data: 21/09/2018
 */

@ManagedBean
@ViewScoped
public class MunicipiController {

	// EJB's
	@EJB
	IllaServiceInterface IllaServ;
	
	
	@EJB
	MunicipiServiceInterface MunicipiServ;
	
	// Private
	private final static Logger LOGGER = Logger.getLogger(MunicipiController.class);
	private  static Integer CRIDADES = 0;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	private ArrayList<Municipi> llista_municipis;
	private Integer illa = 0;
	private Integer municipi = 0;
	
	@PostConstruct
	public void init() {
		CRIDADES = CRIDADES + 1;
		
		LOGGER.info("Proxy a MunicipiController " + CRIDADES);
	}
	public boolean getAmbErrors() { return this.ambErrors; }
	public ArrayList<Illa> getLlista_Illes() { 
			
		
		ArrayList<Illa> llista_Illes = null;
			
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista d'illes");
		try {
			LOGGER.info("EJB lookup" + IllaServ);
				
			llista_Illes = IllaServ.getLlista_Illes();
							
			if (!IllaServ.getResultat())
			{
				LOGGER.info("error obtingut: " + IllaServ.getError());
				this.ambErrors = true;
				this.message = IllaServ.getError();
			}
				
				
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
			
		return llista_Illes; 
			
	}
	
	public ArrayList<Municipi> getLlista_Municipis() { return this.llista_municipis; }
	public void setIlla(Integer illa) { this.illa = illa;  }
	public Integer getIlla() { return this.illa; }
	public void setMunicipi(Integer municipi) { this.municipi = municipi; }
	public Integer getMunicipi() { return this.municipi; }
	
	// Crida AJAX per actualitzar els subcentres
	public void onCentre_change() {
	    	
	    	LOGGER.info("Proxy a MunicipiController --> onCentre_change");
	     	this.llista_municipis = new ArrayList<Municipi>();  
	     		 		  	
	     	// Obtenim llista de matèries
	 		LOGGER.info("Obtenim llista de municipis a partir del canvi d'illa ");
	 		
	 		try
	 		{
	 			LOGGER.info("EJB lookup "+ MunicipiServ);	
	 			
	 			this.llista_municipis = MunicipiServ.getLlista_Municipis(this.illa); // Cridem l'EJB
	 			
	 			LOGGER.info("Obtinguda llista de municipis "+ MunicipiServ);	
	 			
	 			if (!MunicipiServ.getResultat())
	 			{
	 				LOGGER.info("error obtingut: "+ MunicipiServ.getError());
	 				this.ambErrors = true;
	 				this.message = MunicipiServ.getError();
	 			}
	 		}
	 		catch (Exception e) {
	 			LOGGER.info("Error_+ " + e.toString());
	 			this.ambErrors = true;
	 			this.message = this.message + " -- " + e.toString();
	 		}
	 		
	    }
}
