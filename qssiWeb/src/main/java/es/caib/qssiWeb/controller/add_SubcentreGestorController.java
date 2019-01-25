package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

/**
 * Controlador de la vista add_SubcentreGestor
 * @author [u97091] Toni Juanico Soler
 * data: 13/12/2018
 */

@ManagedBean(name="add_SubcentreGestorController")
public class add_SubcentreGestorController {

	
	private final static Logger LOGGER = Logger.getLogger(add_SubcentreGestorController.class);
	
	private InitialContext ic;
	
	private String message = new String("");
	private boolean ambErrors = false;
	
	// Propietats privades
	private String subcentreId;
	private Integer centre = 0;
	private ArrayList<Centre> llista_centres;
	private String nom;
	private String dir3;
	private boolean actiu;
	private boolean visible_web;
	
	private String btnUpdate;
	private String btnAdd;
	private String strTitol;
	
	@PostConstruct
	private void init() {
				
		LOGGER.info("Proxy a add_SubcentreGestorController ");
		
				
		String subcentreId_param = new String("");
		String subcentreId_param_update = new String("");
		
		this.btnUpdate = new String("");
		this.btnAdd = new String("");
		this.strTitol = new String("");
		
		try
		{
						
			subcentreId_param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subcentreId_param");
			subcentreId_param_update = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subcentreId_param_update");
			
			
			if (subcentreId_param!=null && !subcentreId_param.isEmpty()) {
				LOGGER.info("add_SubcentreGestorController amb subcentreId= "+ subcentreId_param);
				this.btnUpdate="true";
				this.btnAdd="false";
				this.strTitol = "Modificació subcentre gestor";
				this.subcentreId = subcentreId_param;
				this.getSubcentreGestorInfo(Integer.parseInt(subcentreId_param));
			}
			else
			{
				if (subcentreId_param_update != null)
				{
					this.btnUpdate="true";
					this.btnAdd="false";
					this.strTitol = "Modificació subcentre gestor";
					this.subcentreId = subcentreId_param_update;
					LOGGER.info("update callback " + subcentreId_param_update);
				}
				else
				{
					this.btnUpdate="false";
					this.btnAdd="true";
					this.strTitol = "Nou subcentre gestor";
					LOGGER.info("add_SubcentreGestorController sense paramètre");	
				}
			}	
		}
		catch (Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el subcentre",  ex.toString()));
		}
		
		
	}
	
	// Constructor
	public add_SubcentreGestorController() { }
	
	// Get - Set
	public String getSubcentreId() { return this.subcentreId; }
	public void setSubcentreId(String scId) { this.subcentreId = scId;}
		
	public String getNom() { return this.nom; }
	public void setNom(String n) { this.nom = n;}
		
	public String getDir3() { return this.dir3; }
	public void setDir3(String d) { this.dir3 = d; }
		
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
		
	public boolean getVisibleweb() { return this.visible_web;}
	public void setVisibleweb(boolean v) { this.visible_web = v; }
		
	public String getBtnUpdate() { return this.btnUpdate;}
	public void setBtnUpdate(String s) { this.btnUpdate = s; }
		
	public String getBtnAdd() { return this.btnAdd;}
	public void setBtnAdd(String s) { this.btnAdd = s; }
		
	public String getStrTitol() { return this.strTitol;}
	public void setStrTitol(String s) { this.strTitol = s; }
	
	public String getMessage() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public void setCentre(Integer centre) { this.centre = centre;  }
	public Integer getCentre() { return this.centre; }
	
	public void setLlista_centres(ArrayList <Centre> lc) { this.llista_centres = lc; }
	public ArrayList<Centre> getLlista_centres() { return this.llista_centres; }

	// Accions
	public void getSubcentreGestorInfo(Integer subcentreId) {
			
		SubcentreServiceInterface SubcentreServ;
		LOGGER.info("getSubcentreGestorInfo: " + subcentreId);
			
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");
			LOGGER.info("EJB lookup " + SubcentreServ);
				
			Subcentre c = new Subcentre();
			c = SubcentreServ.getSubcentre(subcentreId);
				
			if (SubcentreServ.getResultat())
			{
				this.nom = c.getNom();
				this.dir3 = c.getDir3();
				this.actiu = c.getActiu();
				this.visible_web = c.getVisible_web();
				this.centre = c.getCentre().getId();
				
			}
			else
			{
				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el subcentre",  SubcentreServ.getError()));
			}
			
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el subcentre",  ex.toString()));
		}
	}
	
	public void updateSubcentreGestor()
	{
		SubcentreServiceInterface SubcentreServ;
		
		LOGGER.info("updateSubcentreGestor");
		
	    try 
	    {
	    	HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	
	    	ic = new InitialContext();
	    	SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");
	    	LOGGER.info("EJB lookup" + SubcentreServ);
	    	
	    	// Construim el centre
	    	Subcentre sc = new Subcentre();
	    	sc.setId(Integer.parseInt(this.subcentreId));
			sc.setNom(this.nom);
			sc.setDir3(this.dir3);
			sc.setDatacreacio(new Date());
			sc.setUsuari(origRequest.getRemoteUser()); 
			sc.setActiu(this.actiu);
			sc.setVisible_web(this.visible_web);
			
			Centre c = new Centre();
			c.setId(this.centre);
			sc.setCentre(c);
			
	    	SubcentreServ.updateSubcentre(sc);
	    	
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcentre actualitzat correctament", "Subcentre actualitzat correctament"));				
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/centre_gestor/llistat_subcentre.xhtml?centreId_param=" + this.centre);
		} catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant el subcentre", ex.toString()));
		}
	}
	
	public void addSubcentreGestor()
	{
		SubcentreServiceInterface SubcentreServ;
		
		LOGGER.info("addSubcentreGestor ");
		
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
			LOGGER.info("EJB lookup "+ SubcentreServ);	
			
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			// Contruim el subcentre
			Subcentre sc = new Subcentre();
			sc.setNom(this.nom);
			sc.setDir3(this.dir3);
			sc.setDatacreacio(new Date());
			sc.setUsuari(origRequest.getRemoteUser()); 
			sc.setActiu(this.actiu);
			sc.setVisible_web(this.visible_web);
			
			// Indiquem el centre (pare)
			Centre c = new Centre();
			c.setId(this.centre);
			sc.setCentre(c);
		
			SubcentreServ.addSubcentre(sc); // Cridem l'EJB
				 
			if (SubcentreServ.getResultat()==true)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcentre afegit correctament", "Subcentre afegit correctament"));				
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/centre_gestor/llistat_subcentre.xhtml?centreId_param=" + this.centre);
			}
			else
			{
				
				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint subcentre",  SubcentreServ.getError()));
			}
		} catch (Exception ex) {
			
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el subcentre", ex.toString()));
		}
	}
	
	public ArrayList<Centre> getLlista_Centres() { 
		
		CentreServiceInterface CentreServ;
		ArrayList<Centre> llista_Centres = null;
			
		// Obtenim la llista d'escrits
		LOGGER.info("Obtenim llista de centres");
		try {
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.CentreService");
				
			LOGGER.info("EJB lookup" + CentreServ);
				
			llista_Centres = CentreServ.getLlista_CentresActiusWeb();
							
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: " + CentreServ.getError());
				this.ambErrors = true;
				this.message = CentreServ.getError();
			}
				
				
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
			
		return llista_Centres; 
			
	}
	
}
