package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;

/**
 * Controlador de la vista Escrit
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="EscritController")
@ViewScoped
public class EscritController {
	
	private final static Logger LOGGER = Logger.getLogger(EscritController.class);
	
	private ArrayList<Escrit> llista_escrits;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a EscritController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Escrit> getLlista_escrits() 
	{ 
		EscritServiceInterface EscritServ;
		
		LOGGER.info("Obtenim llista d'escrits ");
		
		try
		{
			ic = new InitialContext();
			EscritServ = (EscritServiceInterface) ic.lookup("es.caib.qssiEJB.service.EscritService");	
			LOGGER.info("EJB lookup "+ EscritServ);	
			
			this.llista_escrits = EscritServ.getLlista_Escrits(); // Cridem l'EJB
			
			if (!EscritServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ EscritServ.getError());
				this.ambErrors = true;
				this.message = EscritServ.getError();
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
		
		return this.llista_escrits; 
	}
}
