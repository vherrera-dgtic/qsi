package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Entrada;
import es.caib.qssiEJB.interfaces.EntradaServiceInterface;

/**
 * Controlador de la vista Entrada
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="EntradaController")
@ViewScoped
public class EntradaController {
	private final static Logger LOGGER = Logger.getLogger(EntradaController.class);
	
	private ArrayList<Entrada> llista_entrades;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a EntradaController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Entrada> getLlista_entrades() 
	{ 
		EntradaServiceInterface EntradaServ;
		
		LOGGER.info("Obtenim llista de motius ");
		
		try
		{
			ic = new InitialContext();
			EntradaServ = (EntradaServiceInterface) ic.lookup("es.caib.qssiEJB.service.EntradaService");	
			LOGGER.info("EJB lookup "+ EntradaServ);	
			
			this.llista_entrades = EntradaServ.getLlista_Entrades(); // Cridem l'EJB
			
			if (!EntradaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ EntradaServ.getError());
				this.ambErrors = true;
				this.message = EntradaServ.getError();
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
		
		return this.llista_entrades; 
	}
}
