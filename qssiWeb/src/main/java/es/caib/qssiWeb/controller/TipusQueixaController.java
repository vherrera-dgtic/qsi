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

import es.caib.qssiEJB.entity.Queixa;
import es.caib.qssiEJB.interfaces.QueixaServiceInterface;

/**
 * Controlador de la vista TipusQueixa
 * @author [u97091] Toni Juanico Soler
 * data: 19/09/2018
 */

@ManagedBean
@ViewScoped
public class TipusQueixaController {

	// EJB's
	@EJB
	QueixaServiceInterface QueixaServ;
		
	// Private properties
	
	private final static Logger LOGGER = Logger.getLogger(TipusQueixaController.class);
	
	private DataTable taula_queixes;
	private ArrayList<Queixa> llista_queixes;
	
	private String queixaId;
	private String queixaNom;
	private boolean actiu;
	
	private String message = new String("");
			
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
		
	public DataTable getTaula_queixes() { return taula_queixes;	}
	public void setTaula_queixes(DataTable dataTable) { this.taula_queixes = dataTable;	}

	public String getQueixaId() { return this.queixaId; }
	public void setQueixaId(String cId) { this.queixaId = cId;}
	
	public String getQueixaNom() { return this.queixaNom; }
	public void setQueixaNom(String n) { this.queixaNom = n; }
	
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
	
	// Methods
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a TipusQueixaController");
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("queixaId_param");
		
		if (param!=null) {
			LOGGER.info("TipusQueixaController amb queixaId_param= "+param);
			this.queixaId = param;
			this.getTipusQueixaInfo(this.queixaId);
		}
		
	}
	
	public ArrayList<Queixa> getLlista_queixes() 
	{ 
				
		LOGGER.info("Obtenim llista de tipus de queixes ");
		
		try
		{
			
				
			LOGGER.info("EJB lookup "+ QueixaServ);	
			
			this.llista_queixes = QueixaServ.getLLista_queixes(); // Cridem l'EJB
			
			if (!QueixaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ QueixaServ.getError());
				this.message = QueixaServ.getError();
			}
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_queixes; 
	}

	public void getTipusQueixaInfo(String queixaId) {
			
		
		LOGGER.info("getTipusQueixaInfo: " + queixaId);
			
		try
		{
			LOGGER.info("EJB lookup " + QueixaServ);
				
			Queixa q = new Queixa();
			q = QueixaServ.getQueixa(Integer.parseInt(queixaId));
				
			if (QueixaServ.getResultat())
			{
				this.queixaId = queixaId;
				this.queixaNom = q.getNom();
				this.actiu = q.getActiva(); 
			}
			else
			{
				LOGGER.info("error obtingut: "+ QueixaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus de queixa",  QueixaServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus de queixa",  ex.toString()));
		}
	}
		
	public void updateTipusQueixa() {
			
					
		LOGGER.info("updateTipusQueixa");
			
		try 
		{
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    
		    LOGGER.info("EJB lookup" + QueixaServ + "-->queixaId: " + this.queixaId);
		    	
		    // Construim la queixa
		    Queixa q = new Queixa();
		    q.setId(Integer.parseInt(this.queixaId));
			q.setNom(this.queixaNom);
			q.setDatacreacio(new Date());
			q.setUsuari(origRequest.getRemoteUser()); 
			q.setActiva(this.actiu);
							
		    QueixaServ.updateQueixa(q);
		    	
		    // Ojo, aix� no acaba de funcionar per un Bug a Mojarra 1.2_13
		    // Despr�s d'actualitzar jsg-api.jar i jsf-imppl.jar (v. 2.1.2) al JBoss ubicats a ...\jboss-eap-5.2\jboss-as\server\default\deploy\jbossweb.sar\jsf-libs
		    // hem aconseguit actualitzar a Mojarra 2.1.2, per� tampoc funciona correctament
		    // ja que el missatge es conserva indefinidament
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus de queixa actualitzat correctament", "Tipus de queixa actualitzat correctament"));
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_queixa/llistat_tipusqueixa.xhtml?faces-redirect=true");
		} 
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant el tipus queixa", ex.toString()));
		}
	}
		
	public void addTipusQueixa()
	{
					
		LOGGER.info("addTipusQueixa ");
			
		try
		{
			
			LOGGER.info("EJB lookup "+ QueixaServ);	
				
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			// Contruim la queixa
			Queixa q = new Queixa();
			q.setNom(this.queixaNom);
			q.setDatacreacio(new Date());
			q.setUsuari(origRequest.getRemoteUser()); 
			q.setActiva(this.actiu);
				
			QueixaServ.addQueixa(q); // Cridem l'EJB
					 
			if (QueixaServ.getResultat()==true)
			{			
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus queixa afegida correctament", "Tipus queixa afegida correctament"));
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_queixa/llistat_tipusqueixa.xhtml?faces-redirect=true");
			}
			
			else
			{
				
				LOGGER.info("error obtingut: "+ QueixaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint queixa",  QueixaServ.getError()));
			}
		} 
		catch (Exception ex) {
				
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el tipus queixa", ex.toString()));
		}
	}
	
	public void removeTipusQueixa()
	{
		Queixa q = (Queixa) this.taula_queixes.getRowData();
						
		LOGGER.info("Entram a remove amb par�metre: " + q.getId());
		
		try
		{
			LOGGER.info("EJB lookup " + QueixaServ);
			
			QueixaServ.removeQueixa(q.getId());
		    			
			if (!QueixaServ.getResultat())
			{
				LOGGER.info("Error a remove: " + QueixaServ.getError());
				this.message = this.message + " -- " + QueixaServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant la queixa", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Queixa eliminada correctament", "Queixa eliminada correctament"));
			}
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant la queixa", this.message));
		}
		
	}
	
}
