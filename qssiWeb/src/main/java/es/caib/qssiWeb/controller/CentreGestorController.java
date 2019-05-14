package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

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
@ViewScoped
public class CentreGestorController {
	
	// Private properties
	private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(CentreGestorController.class);
	
	private DataTable taula_centres;
	private ArrayList<Centre> llista_centres;
		
	private String centreId;
	private String nom;
	private String dir3;
	private boolean actiu;
	private boolean visible_web;
	
	private String message = new String("");
	
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
		
	public DataTable getTaula_centres() { return taula_centres; }
	public void setTaula_centres(DataTable dataTable) { this.taula_centres = dataTable;	}
	
	public String getCentreId() { return this.centreId; }
	public void setCentreId(String cId) { this.centreId = cId;}
	
	public String getNom() { return this.nom; }
	public void setNom(String n) { this.nom = n; }
	
	public String getDir3() { return this.dir3; }
	public void setDir3(String d) { this.dir3 = d; }
	
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
	
	public boolean getVisibleweb() { return this.visible_web;}
	public void setVisibleweb(boolean v) { this.visible_web = v; }
	
	// Public methods
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a CentreGestorController ");
		
		this.centreId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("centreId_param");
		
		if (this.centreId!=null) {
			LOGGER.info("CentreGestorController amb centreId= "+this.centreId);
			this.getCentreGestorInfo(this.centreId);
		}
		
	}
		
	public void getCentreGestorInfo(String centreId) {
		
		CentreServiceInterface CentreServ;
		LOGGER.info("getCentreGestorInfo: " + centreId);
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");
			LOGGER.info("EJB lookup " + CentreServ);
			
			Centre c = new Centre();
			c = CentreServ.getCentre(Integer.parseInt(centreId));
			
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
				this.message = CentreServ.getError();
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_centres; 
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
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el centre", this.message));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el centre", this.message));
		}
		
	}
	
	// Toni Juanico, 14/05/2019. Mantenim el codi per conservar l'exemple per altres herbes
	
	//public void getUserRoles()
	//{
	//	try
	//	{
	//		ic = new InitialContext();
	//		/* Http Session */
	//		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

	//		LOGGER.info("Current user: " + origRequest.getRemoteUser());
			
			// origRequest.isUserInRole("QSI_ADMINISTRADOR");
			//HttpSession ses = origRequest.getSession();
		
	//		Subject subject = (Subject) ic.lookup("java:comp/env/security/subject");
			// To list the Principals contained in the Subject...
	//		Iterator<Group> principals;
			// To get the roles (the instance of java.security.acl.Group in the list of Principals)
	//		principals = subject.getPrincipals(java.security.acl.Group.class).iterator();
	//		if (principals.hasNext()) {
	//			Group roles = (Group)principals.next();
	//			Enumeration<?> roleEnum = roles.members();
	//			while (roleEnum.hasMoreElements()) {
	//				LOGGER.info("Role: " + roleEnum.nextElement());
	//			}
	//		}	
			
			/* Http Session end section */
			
	//	}
	//	catch (Exception ex)
	//	{
	//		LOGGER.info("getUserRoles exception: " + ex.toString());
	//	}
	//}*/

}
