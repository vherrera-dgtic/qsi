package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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

@ManagedBean(name="MunicipiController")
public class MunicipiController {

	private final static Logger LOGGER = Logger.getLogger(MunicipiController.class);
	private  static Integer CRIDADES = 0;
	
	private InitialContext ic;
	
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
	
	public ArrayList<Municipi> getLlista_Municipis() { return this.llista_municipis; }
	public void setIlla(Integer illa) { this.illa = illa;  }
	public Integer getIlla() { return this.illa; }
	public void setMunicipi(Integer municipi) { this.municipi = municipi; }
	public Integer getMunicipi() { return this.municipi; }
	
	// Crida AJAX per actualitzar els subcentres
	public void onCentre_change() {
	    	
	    	LOGGER.info("Proxy a MunicipiController --> onCentre_change");
	     	this.llista_municipis = new ArrayList<Municipi>();  
	     	MunicipiServiceInterface MunicipiServ;
	 		  	
	     	// Obtenim llista de matèries
	 		LOGGER.info("Obtenim llista de municipis a partir del canvi d'illa ");
	 		
	 		try
	 		{
	 			ic = new InitialContext();
	 			MunicipiServ = (MunicipiServiceInterface) ic.lookup("es.caib.qssiEJB.service.MunicipiService");	
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
