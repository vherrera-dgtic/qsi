package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Illa;
import es.caib.qssiEJB.interfaces.IllaServiceInterface;


/**
 * Controlador de la vista Illa
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean
@ViewScoped
public class IllaController {

	// EJB's
	@EJB
	IllaServiceInterface IllaServ;
	
	private final static Logger LOGGER = Logger.getLogger(IllaController.class);
	
	private ArrayList<Illa> llista_illes;
		
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a IllaController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Illa> getLlista_illes() 
	{ 
		LOGGER.info("Obtenim llista d'illes ");
		
		try
		{
			LOGGER.info("EJB lookup "+ IllaServ);	
			
			this.llista_illes = IllaServ.getLlista_Illes(); // Cridem l'EJB
			
			if (!IllaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ IllaServ.getError());
				this.ambErrors = true;
				this.message = IllaServ.getError();
			}
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_illes; 
	}
}
