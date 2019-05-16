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

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

/**
 * Controlador de la vista Subcentre
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="SubcentreController")
@ViewScoped
public class SubcentreController {

	// Private properties
	private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(SubcentreController.class);
	
	private DataTable taula_subcentres;
	private Integer centre = 0;
	private ArrayList<Centre> llista_centres;
	private ArrayList<Subcentre> llista_subcentres;
	
	private String subcentreId;
	private String nom;
	private String dir3;
	private boolean actiu;
	private boolean visible_web;
	
	private String message = new String("");
	
	// Getters & Setters		
	public Integer getCentre() { return this.centre; }
	public void setCentre(Integer centre) { this.centre = centre;  }
	
	public ArrayList<Subcentre> getLlista_Subcentres() { return this.llista_subcentres; }
	public void setLlista_Subcentres(ArrayList<Subcentre> lsc) { this.llista_subcentres = lsc; }
	
	public ArrayList<Centre> getLlista_centres() { return this.llista_centres; }
	public void setLlista_centres(ArrayList <Centre> lc) { this.llista_centres = lc; }
		
	public DataTable getTaula_subcentres() { return this.taula_subcentres; }
	public void setTaula_subcentres(DataTable dataTable) { this.taula_subcentres = dataTable; }
	
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
	
	public String getMessage() { return this.message; }
	
	// Public methods
	@PostConstruct
	public void init() {
		
		LOGGER.info("Proxy a SubcentreController");
		refrescaLlista_centres();
	
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subcentreId_param");
		
		if (param!=null) {
			LOGGER.info("SubcentreController amb subcentreId_param= " + param);
			this.subcentreId = param;
			this.getSubcentreGestorInfo(this.subcentreId);
		}
		
	}
	
	public void getSubcentreGestorInfo(String subcentreId) {
		
		SubcentreServiceInterface SubcentreServ;
		LOGGER.info("getSubcentreGestorInfo: " + subcentreId);
			
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");
			LOGGER.info("EJB lookup " + SubcentreServ);
				
			Subcentre c = new Subcentre();
			c = SubcentreServ.getSubcentre(Integer.parseInt(subcentreId));
				
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
				this.message = CentreServ.getError();
			}
				
				
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
			
		return llista_Centres; 
	}
			
	public void removeSubcentreGestor()
	{
			
		Subcentre c = (Subcentre) this.taula_subcentres.getRowData();
		SubcentreServiceInterface SubcentreServ;
			
		LOGGER.info("Entram a remove amb paràmetre: " + c.getId());
			
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");
			LOGGER.info("EJB lookup " + SubcentreServ);
				
			SubcentreServ.removeSubcentre(c.getId());
			    			
			if (!SubcentreServ.getResultat())
			{
				LOGGER.info("Error a remove: " + SubcentreServ.getError());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", SubcentreServ.getError()));
			}
			else
			{
				refrescaLlista_Subcentres(this.centre);
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcentre eliminat correctament", "Subcentre eliminat correctament"));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", e.toString()));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el subcentre", e.toString()));
		}
			
	}
	
	// Crida AJAX per actualitzar els subcentres
	public void onCentre_change() {
		LOGGER.info("onCentre_change");
		refrescaLlista_Subcentres(this.centre);
	}
	
	private void refrescaLlista_centres() { 
			
		CentreServiceInterface CentreServ;
		
		// Obtenim la llista de centres
		LOGGER.info("Obtenim llista de centres");
		try {
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) this.ic.lookup("es.caib.qssiEJB.service.CentreService");
				
			LOGGER.info("EJB lookup" + CentreServ);
				
			this.llista_centres = CentreServ.getLlista_CentresActiusWeb();
			
			/* si tenim paràmetre, posem l'índex */
			String centreId_param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("centreId_param");
						
			if (centreId_param!=null && !centreId_param.isEmpty()) {
				this.centre = Integer.parseInt(centreId_param);
				this.refrescaLlista_Subcentres(this.centre);
			}
							
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: " + CentreServ.getError());
			}
				
				
		} catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
		}
	}
	
	private void refrescaLlista_Subcentres(Integer c)
	{
		  
	   	SubcentreServiceInterface SubcentreServ;
	 		  	
	   	// Obtenim llista de subcentres
	   	LOGGER.info("Obtenim la llista de subcentres: " + c);
	   	this.llista_subcentres = new ArrayList<Subcentre>();
	   	
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
			LOGGER.info("EJB lookup "+ SubcentreServ);	
	 			
			this.llista_subcentres = SubcentreServ.getLlista_Subcentres(c); // Cridem l'EJB
	 			
			LOGGER.info("Obtinguda llista de subcentres "+ SubcentreServ);	
	 			
			if (!SubcentreServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
		}
	 			
	}
	
	
}
