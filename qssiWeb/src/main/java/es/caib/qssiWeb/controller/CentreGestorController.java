package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.primefaces.component.datatable.DataTable;
 
import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;

/**
 * Controlador de la vista CentreGestor
 * @author [u97091] Toni Juanico Soler
 * data: 19/09/2018
 */

@ManagedBean(name="CentreGestorController")
public class CentreGestorController {
	
	private final static Logger LOGGER = Logger.getLogger(CentreGestorController.class);
	
	private ArrayList<Centre> llista_centres;
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	private DataTable taula_centres;
	
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a CentreGestorController ");
		
	}
	
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	
	public DataTable getTaula_centres() {
	    return taula_centres;
	}

	public void setTaula_centres(DataTable dataTable) {
	    this.taula_centres = dataTable;
	}
	
	public void remove()
	{
		Centre c = (Centre) this.taula_centres.getRowData();
		CentreServiceInterface CentreServ;
		
		LOGGER.info("Entram a remove amb paràmetre: " + c.getId());
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");
			LOGGER.info("EJB lookup " + CentreServ);
			
			CentreServ.removeCentre(c.getId());
		    			
			if (!CentreServ.getResultat())
			{
				LOGGER.info("Error a remove: " + CentreServ.getError());
				this.ambErrors = true;
				this.message = this.message + " -- " +CentreServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el centre", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Centre eliminat correctament", "Centre eliminat correctament"));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el centre", this.message));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el centre", this.message));
		}
		
	}
	
	public ArrayList<Centre> getLlista_centres() 
	{ 
		CentreServiceInterface CentreServ;
		
		LOGGER.info("Obtenim llista de centres gestors ");
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");	
			LOGGER.info("EJB lookup "+ CentreServ);	
			
			this.llista_centres = CentreServ.getLlista_Centres(); // Cridem l'EJB
			
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ CentreServ.getError());
				this.ambErrors = true;
				this.message = CentreServ.getError();
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
		
		return this.llista_centres; 
	}
}
