package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;


/**
 * Controlador de la vista Escrit
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean
@ViewScoped
public class TipusEscritController {
	
	// EJB's
	@EJB
	EscritServiceInterface EscritServ;
	
	// Private properties
	private final static Logger LOGGER = Logger.getLogger(TipusEscritController.class);
	
	private DataTable taula_escrits;
	private ArrayList<Escrit> llista_escrits;
	
	private String escritId;
	private String escritNom;
	private boolean actiu;
	
	private String message = new String("");
	
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
		
	public DataTable getTaula_escrits() { return taula_escrits;	}
	public void setTaula_escrits(DataTable dataTable) { this.taula_escrits = dataTable;	}

	public String getEscritId() { return this.escritId; }
	public void setEscritId(String cId) { this.escritId = cId;}
	
	public String getEscritNom() { return this.escritNom; }
	public void setEscritNom(String n) { this.escritNom = n; }
	
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
		
	// Methods
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a EscritController ");
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("escritId_param");
		
		if (param!=null) {
			LOGGER.info("TipusEscritController amb escritId_param= "+param);
			this.escritId = param;
			this.getTipusEscritInfo(this.escritId);
		}
	}
	
	public ArrayList<Escrit> getLlista_escrits() 
	{ 
		LOGGER.info("Obtenim llista d'escrits ");
		
		try
		{
			
			LOGGER.info("EJB lookup "+ EscritServ);	
			
			this.llista_escrits = EscritServ.getLlista_Escrits(); // Cridem l'EJB
			
			if (!EscritServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ EscritServ.getError());
				this.message = EscritServ.getError();
			}
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_escrits; 
	}
	
	public void getTipusEscritInfo(String escritId) {
		
		
		LOGGER.info("getTipusEscritInfo: " + escritId);
			
		try
		{
			LOGGER.info("EJB lookup " + EscritServ);
				
			Escrit e = new Escrit();
			e = EscritServ.getEscrit(Integer.parseInt(escritId));
				
			if (EscritServ.getResultat())
			{
				this.escritId = escritId;
				this.escritNom = e.getNom();
				this.actiu = e.getActiu(); 
			}
			else
			{
				LOGGER.info("error obtingut: "+ EscritServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus d'escrit",  EscritServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus d'escrit",  ex.toString()));
		}
	}
		
	public void updateTipusEscrit() {
		
		LOGGER.info("updateTipusEscrit");
			
		try 
		{
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    LOGGER.info("EJB lookup" + EscritServ + "--> escritId: " + this.escritId);
		    	
		    // Construim l'escrit
		    Escrit e = new Escrit();
		    e.setId(Integer.parseInt(this.escritId));
			e.setNom(this.escritNom);
			e.setDatacreacio(new Date());
			e.setUsuari(origRequest.getRemoteUser()); 
			e.setActiu(this.actiu);
							
		    EscritServ.updateEscrit(e);
		    	
		    //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus d'escrit actualitzat correctament", "Tipus d'escrit actualitzat correctament"));				
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_escrit/llistat_tipusescrit.xhtml");
		} 
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant el tipus d'escrit", ex.toString()));
		}
	}
		
	public void addTipusEscrit()
	{
		LOGGER.info("addTipusEscrit ");
			
		try
		{
			LOGGER.info("EJB lookup "+ EscritServ);	
				
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			// Contruim l'escrit
			Escrit e = new Escrit();
			e.setNom(this.escritNom);
			e.setDatacreacio(new Date());
			e.setUsuari(origRequest.getRemoteUser()); 
			e.setActiu(this.actiu);
				
			EscritServ.addEscrit(e); // Cridem l'EJB
					 
			if (EscritServ.getResultat()==true)
			{
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus escrit afegit correctament", "Tipus escrit afegit correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_escrit/llistat_tipusescrit.xhtml");
			}
			
			else
			{
				
				LOGGER.info("error obtingut: "+ EscritServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint escrit",  EscritServ.getError()));
			}
		} 
		catch (Exception ex) {
				
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el tipus d'escrit", ex.toString()));
		}
	}
	
	public void removeTipusEscrit()
	{
		Escrit e = (Escrit) this.taula_escrits.getRowData();
						
		LOGGER.info("Entram a remove amb paràmetre: " + e.getId());
		
		try
		{
			LOGGER.info("EJB lookup " + EscritServ);
			
			EscritServ.removeEscrit(e.getId());
		    			
			if (!EscritServ.getResultat())
			{
				LOGGER.info("Error a remove: " + EscritServ.getError());
				this.message = this.message + " -- " + EscritServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el tipus d'escrit", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus d'escrit eliminat correctament", "Tipus d'escrit eliminat correctament"));
			}
		} catch (Exception ex) {
			LOGGER.info("Error_+ " + ex.toString());
			this.message = this.message + " -- " + ex.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el tipus d'escrit", this.message));
		}
		
	}
}
