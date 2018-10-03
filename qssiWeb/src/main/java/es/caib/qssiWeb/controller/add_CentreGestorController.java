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
import javax.naming.NamingException;
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
	private String nom;
	private String dir3;
	private boolean actiu;
	private boolean visible_web;
	private boolean ambErrors;
	private String message;
		
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a add_CentreGestorController ");
		
	}
	
	// Constructor
	public add_CentreGestorController() { }
	
	// Get - Set
	public String getNom() { return this.nom; }
	public void setNom(String n) { this.nom = n;}
	
	public String getDir3() { return this.dir3; }
	public void setDir3(String d) { this.dir3 = d; }
	
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
	
	public boolean getVisibleweb() { return this.visible_web;}
	public void setVisibleweb(boolean v) { this.visible_web = v; }
	
	public boolean getAmbErrors() { return this.ambErrors; }
	public void setAmbError(Boolean e) { this.ambErrors = e; }
	
	public String getMessage() { return this.message; }
	public void setMessage(String m) { this.message = m; }
	
	// Accions
	public void saveCentreGestor()
	{
		CentreServiceInterface CentreServ;
		
		LOGGER.info("add_CentreGestorController -> saveCentreGestor ");
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");	
			LOGGER.info("EJB lookup "+ CentreServ);	
			
			// Contruim el centre
			Centre e = new Centre();
			e.setNom(this.nom);
			e.setDir3(this.dir3);
			e.setDatacreacio(new Date());
			
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
			
			e.setUsuari(origRequest.getRemoteUser()); 
			e.setActiu(this.actiu);
			e.setVisible_web(this.visible_web);
				
			
			CentreServ.addCentre(e); // Cridem l'EJB
			
			 
			if (CentreServ.getResultat()==true)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Centre afegit correctament", "Centre afegit correctament"));				
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/centre_gestor/llistat_centregestor.xhtml");
			}
			else
			{
				
				LOGGER.info("error obtingut: "+ CentreServ.getError());
				this.ambErrors = true;
				this.message = CentreServ.getError();
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint centre",  CentreServ.getError()));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error NamingException: " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el centre", this.message));
		} catch (Exception e) {
			
			LOGGER.info("Error: " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el centre", this.message));
		}
	}
	
}
