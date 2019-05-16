package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;


@ManagedBean(name="IndexController")
@ViewScoped
public class IndexController {
	
	private final static Logger LOGGER = Logger.getLogger(ExpedientController.class);
			
	private ArrayList<Expedient> llista_expedients;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a IndexController ");
		
	}
	
	public void setMissatge(String m) { this.message = m; }
	public String getMissatge() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	public ArrayList<Expedient> getLlista_expedients() 
	{ 
		ExpedientServiceInterface ExpedientServ;
		
		// Obtenim llista de matèries
		LOGGER.info("Obtenim llista d'expedients ");
		
		try
		{
			ic = new InitialContext();
			ExpedientServ = (ExpedientServiceInterface) ic.lookup("es.caib.qssiEJB.service.ExpedientService");	
			LOGGER.info("EJB lookup "+ ExpedientServ);	
			
			this.llista_expedients = ExpedientServ.getLlista_Expedients(); // Cridem l'EJB
			
			if (!ExpedientServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ ExpedientServ.getError());
				this.ambErrors = true;
				this.message = ExpedientServ.getError();
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
		
		return this.llista_expedients; 
	}
}
