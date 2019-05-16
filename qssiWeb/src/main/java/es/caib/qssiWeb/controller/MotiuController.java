package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Motiu;
import es.caib.qssiEJB.interfaces.MotiuServiceInterface;

/**
 * Controlador de la vista Motiu
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="MotiuController")
@ViewScoped
public class MotiuController {
	
	private final static Logger LOGGER = Logger.getLogger(MotiuController.class);
	
	private ArrayList<Motiu> llista_motius;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a MotiuController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Motiu> getLlista_motius() 
	{ 
		MotiuServiceInterface MotiuServ;
		
		LOGGER.info("Obtenim llista de motius ");
		
		try
		{
			ic = new InitialContext();
			MotiuServ = (MotiuServiceInterface) ic.lookup("es.caib.qssiEJB.service.MotiuService");	
			LOGGER.info("EJB lookup "+ MotiuServ);	
			
			this.llista_motius = MotiuServ.getLlista_Motius(); // Cridem l'EJB
			
			if (!MotiuServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ MotiuServ.getError());
				this.ambErrors = true;
				this.message = MotiuServ.getError();
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
		
		return this.llista_motius; 
	}
}
