package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Queixa;
import es.caib.qssiEJB.interfaces.QueixaServiceInterface;

/**
 * Controlador de la vista TipusQueixa
 * @author [u97091] Toni Juanico Soler
 * data: 19/09/2018
 */

@ManagedBean(name="TipusQueixaController")
public class TipusQueixaController {

	private final static Logger LOGGER = Logger.getLogger(TipusQueixaController.class);
	
	private ArrayList<Queixa> llista_queixes;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a TipusQueixaController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Queixa> getLlista_queixes() 
	{ 
		QueixaServiceInterface QueixaServ;
		
		LOGGER.info("Obtenim llista de tipus de queixes ");
		
		try
		{
			ic = new InitialContext();
			QueixaServ = (QueixaServiceInterface) ic.lookup("es.caib.qssiEJB.service.QueixaService");	
			LOGGER.info("EJB lookup "+ QueixaServ);	
			
			this.llista_queixes = QueixaServ.getLLista_queixes(); // Cridem l'EJB
			
			if (!QueixaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ QueixaServ.getError());
				this.ambErrors = true;
				this.message = QueixaServ.getError();
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
		
		return this.llista_queixes; 
	}
}
