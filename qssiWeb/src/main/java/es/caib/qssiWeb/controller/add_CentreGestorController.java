package es.caib.qssiWeb.controller;

import java.security.acl.Group;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;


/**
 * Controlador de la vista add_CentreGestor
 * @author [u97091] Toni Juanico Soler
 * data: 24/09/2018
 */

@ManagedBean(name="add_CentreGestorController")
public class add_CentreGestorController {

	private final static Logger LOGGER = Logger.getLogger(CentreGestorController.class);
	
	private InitialContext ic;
	
	// Propietats privades
	private String centreId;
	private String nom;
	private String dir3;
	private boolean actiu;
	private boolean visible_web;
		
	private String btnUpdate;
	private String btnAdd;
	private String strTitol;
	
	@PostConstruct
	private void init() {
				
		LOGGER.info("Proxy a add_CentreGestorController ");
		
		String centreId_param = new String("");
		String centreId_param_update = new String("");
		
		this.btnUpdate = new String("");
		this.btnAdd = new String("");
		this.strTitol = new String("");
		
		try
		{
			centreId_param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("centreId_param");
			centreId_param_update = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("centreId_param_update");
			
			if (centreId_param!=null && !centreId_param.isEmpty()) {
				LOGGER.info("add_CentreGestorController amb centreId= "+centreId_param);
				this.btnUpdate="true";
				this.btnAdd="false";
				this.strTitol = "Modificació centre gestor";
				this.centreId = centreId_param;
				getCentreGestorInfo(Integer.parseInt(centreId_param));
			}
			else
			{
				if (centreId_param_update != null)
				{
					this.btnUpdate="true";
					this.btnAdd="false";
					this.strTitol = "Modificació centre gestor";
					this.centreId = centreId_param_update;
					LOGGER.info("update callback " + centreId_param_update);
				}
				else
				{
					this.btnUpdate="false";
					this.btnAdd="true";
					this.strTitol = "Nou centre gestor";
					LOGGER.info("add_CentreGestorController sense paramètre");	
				}
			}	
		}
		catch (Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el centre",  ex.toString()));
		}
		
		
	}
	
	// Constructor
	public add_CentreGestorController() { }
	
	// Get - Set
	public String getCentreId() { return this.centreId; }
	public void setCentreId(String cId) { this.centreId = cId;}
	
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
	
	// Accions
	public void getCentreGestorInfo(Integer centreId) {
		
		CentreServiceInterface CentreServ;
		LOGGER.info("getCentreGestorInfo: " + centreId);
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");
			LOGGER.info("EJB lookup " + CentreServ);
			
			Centre c = new Centre();
			c = CentreServ.getCentre(centreId);
			
			if (CentreServ.getResultat())
			{
				this.nom = c.getNom();
				this.dir3 = c.getDir3();
				this.actiu = c.getActiu();
				this.visible_web = c.getVisible_web();	
			}
			else
			{
				LOGGER.info("error obtingut: "+ CentreServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el centre",  CentreServ.getError()));
			}
			
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el centre",  ex.toString()));
		}
	}
	
	public void updateCentreGestor()
	{
		CentreServiceInterface CentreServ;
		
		LOGGER.info("updateCentreGestor");
		
	    try 
	    {
	    	HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	
	    	ic = new InitialContext();
	    	CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");
	    	LOGGER.info("EJB lookup" + CentreServ);
	    	
	    	// Construim el centre
	    	Centre c = new Centre();
	    	c.setId(Integer.parseInt(this.centreId));
			c.setNom(this.nom);
			c.setDir3(this.dir3);
			c.setDatacreacio(new Date());
			c.setUsuari(origRequest.getRemoteUser()); 
			c.setActiu(this.actiu);
			c.setVisible_web(this.visible_web);
			
	    	CentreServ.updateCentre(c);
	    	
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Centre actualitzat correctament", "Centre actualitzat correctament"));				
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/centre_gestor/llistat_centregestor.xhtml");
		} catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant el centre", ex.toString()));
		}
	}
	
	public void addCentreGestor()
	{
		CentreServiceInterface CentreServ;
		
		LOGGER.info("addCentreGestor ");
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");	
			LOGGER.info("EJB lookup "+ CentreServ);	
			
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			// Contruim el centre
			Centre c = new Centre();
			c.setNom(this.nom);
			c.setDir3(this.dir3);
			c.setDatacreacio(new Date());
			c.setUsuari(origRequest.getRemoteUser()); 
			c.setActiu(this.actiu);
			c.setVisible_web(this.visible_web);
			
			CentreServ.addCentre(c); // Cridem l'EJB
				 
			if (CentreServ.getResultat()==true)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Centre afegit correctament", "Centre afegit correctament"));				
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/centre_gestor/llistat_centregestor.xhtml");
			}
			else
			{
				
				LOGGER.info("error obtingut: "+ CentreServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint centre",  CentreServ.getError()));
			}
		} catch (Exception ex) {
			
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el centre", ex.toString()));
		}
	}
	
	public void getUserRoles()
	{
		try
		{
			ic = new InitialContext();
			/* Http Session */
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

			LOGGER.info("Current user: " + origRequest.getRemoteUser());
			
			// origRequest.isUserInRole("QSI_ADMINISTRADOR");
			//HttpSession ses = origRequest.getSession();
		
			Subject subject = (Subject) ic.lookup("java:comp/env/security/subject");
			// To list the Principals contained in the Subject...
			Iterator<Group> principals;
			// To get the roles (the instance of java.security.acl.Group in the list of Principals)
			principals = subject.getPrincipals(java.security.acl.Group.class).iterator();
			if (principals.hasNext()) {
				Group roles = (Group)principals.next();
				Enumeration<?> roleEnum = roles.members();
				while (roleEnum.hasMoreElements()) {
					LOGGER.info("Role: " + roleEnum.nextElement());
				}
			}	
			
			/* Http Session end section */
			
		}
		catch (Exception ex)
		{
			LOGGER.info("getUserRoles exception: " + ex.toString());
		}
	}
	
}
