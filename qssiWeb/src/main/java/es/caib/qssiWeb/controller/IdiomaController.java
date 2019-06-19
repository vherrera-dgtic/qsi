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

import es.caib.qssiEJB.entity.Idioma;
import es.caib.qssiEJB.interfaces.IdiomaServiceInterface;

/**
 * Controlador de la vista Idioma
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean
@ViewScoped
public class IdiomaController {
	
	// Private properties
	private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(IdiomaController.class);
	
	private DataTable taula_idiomes;
	private ArrayList<Idioma> llista_idiomes;
	
	private String idiomaId;
	private String idiomaNom;
	private boolean actiu;
	private String message = new String("");
	
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
	
	public DataTable getTaula_idiomes() { return taula_idiomes;	}
	public void setTaula_idiomes(DataTable dataTable) { this.taula_idiomes = dataTable;	}

	public String getIdiomaId() { return this.idiomaId; }
	public void setIdiomaId(String iId) { this.idiomaId = iId;}
		
	public String getIdiomaNom() { return this.idiomaNom; }
	public void setIdiomaNom(String n) { this.idiomaNom = n; }
		
	public boolean getActiu() { return this.actiu;}
	public void setActiu(boolean a) { this.actiu = a; }
	
	// Methods
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a IdiomaController ");
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idiomaId_param");
		
		if (param!=null) {
			LOGGER.info("IdiomaController amb idiomaId_param= "+param);
			this.idiomaId = param;
			this.getIdiomaInfo(this.idiomaId);
		}
		
	}
	
	public ArrayList<Idioma> getLlista_idiomes() 
	{ 
		IdiomaServiceInterface IdiomaServ;
		
		LOGGER.info("Obtenim llista d'idiomes ");
		
		try
		{
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) ic.lookup("qssiEAR/IdiomaService/local");	
			LOGGER.info("EJB lookup "+ IdiomaServ);	
			
			this.llista_idiomes = IdiomaServ.getLlista_Idiomes(); // Cridem l'EJB
			
			if (!IdiomaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ IdiomaServ.getError());
				this.message = IdiomaServ.getError();
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_idiomes; 
	}
	
	public void getIdiomaInfo(String idiomaId) {
		
		IdiomaServiceInterface IdiomaServ;
		LOGGER.info("getIdiomaInfo: " + idiomaId);
			
		try
		{
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) ic.lookup("qssiEAR/IdiomaService/local");
			LOGGER.info("EJB lookup " + IdiomaServ);
				
			Idioma i = new Idioma();
			i = IdiomaServ.getIdioma(Integer.parseInt(idiomaId));
				
			if (IdiomaServ.getResultat())
			{
				this.idiomaId = idiomaId;
				this.idiomaNom = i.getNom();
				this.actiu = i.getActiu(); 
			}
			else
			{
				LOGGER.info("error obtingut: "+ IdiomaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint l'idioma",  IdiomaServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint l'idioma",  ex.toString()));
		}
	}

	public void updateIdioma() {
		
		IdiomaServiceInterface IdiomaServ;
			
		LOGGER.info("updateIdioma");
			
		try 
		{
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    ic = new InitialContext();
		    IdiomaServ = (IdiomaServiceInterface) ic.lookup("qssiEAR/IdiomaService/local");
		    LOGGER.info("EJB lookup" + IdiomaServ + "--> idiomaId: " + this.idiomaId);
		    	
		    // Construim l'idioma
		    Idioma i = new Idioma();
		    i.setId(Integer.parseInt(this.idiomaId));
			i.setNom(this.idiomaNom);
			i.setDatacreacio(new Date());
			i.setUsuari(origRequest.getRemoteUser()); 
			i.setActiu(this.actiu);
							
		    IdiomaServ.updateIdioma(i);
		    //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Idioma actualitzat correctament", "Idioma actualitzat correctament"));				
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/idiomes/llistat_idioma.xhtml");
		} 
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant l'idioma", ex.toString()));
		}
	}

	public void addIdioma()
	{
		IdiomaServiceInterface IdiomaServ;
			
		LOGGER.info("addIdioma ");
			
		try
		{
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) ic.lookup("qssiEAR/IdiomaService/local");	
			LOGGER.info("EJB lookup "+ IdiomaServ);	
				
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			// Contruim l'idiome
			Idioma i = new Idioma();
			i.setNom(this.idiomaNom);
			i.setDatacreacio(new Date());
			i.setUsuari(origRequest.getRemoteUser()); 
			i.setActiu(this.actiu);
				
			IdiomaServ.addIdioma(i); // Cridem l'EJB
					 
			if (IdiomaServ.getResultat()==true)
			{
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Idioma afegit correctament", "Idioma afegit correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/idiomes/llistat_idioma.xhtml");
			}
			
			else
			{				
				LOGGER.info("error obtingut: "+ IdiomaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint l'idioma",  IdiomaServ.getError()));
			}
		} 
		catch (Exception ex) {	
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant l'idioma", ex.toString()));
		}
	}
	
	public void removeIdioma()
	{
		Idioma i = (Idioma) this.taula_idiomes.getRowData();
		IdiomaServiceInterface IdiomaServ;
				
		LOGGER.info("Entram a remove amb paràmetre: " + i.getId());
		
		try
		{
			ic = new InitialContext();
			IdiomaServ = (IdiomaServiceInterface) ic.lookup("qssiEAR/IdiomaService/local");
			LOGGER.info("EJB lookup " + IdiomaServ);
			
			IdiomaServ.removeIdioma(i.getId());
		    			
			if (!IdiomaServ.getResultat())
			{
				LOGGER.info("Error a remove: " + IdiomaServ.getError());
				this.message = this.message + " -- " + IdiomaServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant l'idioma", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Idioma eliminat correctament", "Idioma eliminat correctament"));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant l'idioma", this.message));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant l'idioma", this.message));
		}
		
	}
	
}
