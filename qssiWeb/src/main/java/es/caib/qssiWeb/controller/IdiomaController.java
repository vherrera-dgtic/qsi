package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Idioma;
import es.caib.qssiEJB.interfaces.IdiomaServiceInterface;

/**
 * Controlador de la vista Idioma
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="IdiomaController")
@ViewScoped
public class IdiomaController {
	
	private final static Logger LOGGER = Logger.getLogger(IdiomaController.class);
	
	private ArrayList<Idioma> llista_idiomes;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a IdiomaController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Idioma> getLlista_idiomes() 
	{ 
		IdiomaServiceInterface IdiomaServ;
		
		LOGGER.info("Obtenim llista d'idiomes ");
		
		try
		{
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) ic.lookup("es.caib.qssiEJB.service.IdiomaService");	
			LOGGER.info("EJB lookup "+ IdiomaServ);	
			
			this.llista_idiomes = IdiomaServ.getLlista_Idiomes(); // Cridem l'EJB
			
			if (!IdiomaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ IdiomaServ.getError());
				this.ambErrors = true;
				this.message = IdiomaServ.getError();
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
		
		return this.llista_idiomes; 
	}
}
