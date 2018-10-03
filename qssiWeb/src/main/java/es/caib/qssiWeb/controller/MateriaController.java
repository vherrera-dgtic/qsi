package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;

/**
 * Controlador de la vista Materia
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="MateriaController")
public class MateriaController {
	
	private final static Logger LOGGER = Logger.getLogger(MateriaController.class);
	
	private ArrayList<Materia> llista_materies;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a MateriaController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Materia> getLlista_materies() 
	{ 
		MateriaServiceInterface MateriaServ;
		
		LOGGER.info("Obtenim llista de tipus de queixes ");
		
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");	
			LOGGER.info("EJB lookup "+ MateriaServ);	
			
			this.llista_materies = MateriaServ.getLlista_Materies(); // Cridem l'EJB
			
			if (!MateriaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				this.ambErrors = true;
				this.message = MateriaServ.getError();
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
		
		return this.llista_materies; 
	}
}
