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

import es.caib.qssiEJB.entity.Entrada;
import es.caib.qssiEJB.interfaces.EntradaServiceInterface;

/**
 * Controlador de la vista Entrada
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean
@ViewScoped
public class TipusEntradaController {
	
	// EJB'
	@EJB
	EntradaServiceInterface EntradaServ;
	
	// Private properties
	private final static Logger LOGGER = Logger.getLogger(TipusEntradaController.class);
	
	private DataTable taula_entrades;
	private ArrayList<Entrada> llista_entrades;
	
	private String entradaId;
	private String entradaNom;
	private boolean activa;
	
	private String message = new String("");
	
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
		
	public DataTable getTaula_entrades() { return taula_entrades;	}
	public void setTaula_entrades(DataTable dataTable) { this.taula_entrades = dataTable;	}

	public String getEntradaId() { return this.entradaId; }
	public void setEntradaId(String eId) { this.entradaId = eId;}
	
	public String getEntradaNom() { return this.entradaNom; }
	public void setEntradaNom(String n) { this.entradaNom = n; }
	
	public boolean getActiva() { return this.activa;}
	public void setActiva(boolean a) { this.activa = a; }
	
	// Methods
	@PostConstruct
	public void init() {
				
		LOGGER.info("Proxy a TipusEntradaController ");
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("entradaId_param");
		
		if (param!=null) {
			LOGGER.info("TipusEntradaController amb entradaId_param= "+param);
			this.entradaId = param;
			this.getTipusEntradaInfo(this.entradaId);
		}
		
	}
	
	public ArrayList<Entrada> getLlista_entrades() 
	{ 
				
		LOGGER.info("Obtenim llista de tipus d'entrades ");
		
		try
		{
			LOGGER.info("EJB lookup "+ EntradaServ);	
			
			this.llista_entrades = EntradaServ.getLlista_Entrades(); // Cridem l'EJB
			
			if (!EntradaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ EntradaServ.getError());
				this.message = EntradaServ.getError();
			}
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_entrades; 
	}
	
	public void getTipusEntradaInfo(String entradaId) {
		
		LOGGER.info("getTipusEntradaInfo: " + entradaId);

		try
		{
			
			LOGGER.info("EJB lookup " + EntradaServ);
				
			Entrada e = new Entrada();
			e = EntradaServ.getEntrada(Integer.parseInt(entradaId));
				
			if (EntradaServ.getResultat())
			{
				this.entradaId = entradaId;
				this.entradaNom = e.getNom();
				this.activa = e.getActiva(); 
			}
			else
			{
				LOGGER.info("error obtingut: "+ EntradaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus d'entrada",  EntradaServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint el tipus d'entrada",  ex.toString()));
		}
	}
	
	public void updateTipusEntrada() {
		
		LOGGER.info("updateTipusEntrada");
			
		try 
		{
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    
		    LOGGER.info("EJB lookup" + EntradaServ + "--> entradaId: " + this.entradaId);
		    	
		    // Construim el centre
		    Entrada e = new Entrada();
		    e.setId(Integer.parseInt(this.entradaId));
			e.setNom(this.entradaNom);
			e.setDatacreacio(new Date());
			e.setUsuari(origRequest.getRemoteUser()); 
			e.setActiva(this.activa);
							
		    EntradaServ.updateEntrada(e);
		    	
		    //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus d'entrada actualitzada correctament", "Tipus d'entrada actualitzada correctament"));				
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_entrada/llistat_tipusentrada.xhtml");
		} 
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant el tipus d'entrada", ex.toString()));
		}
	}
	
	public void addTipusEntrada()
	{
			
		LOGGER.info("addTipusEntrada ");
			
		try
		{
				
			LOGGER.info("EJB lookup "+ EntradaServ);	
				
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			// Contruim el centre
			Entrada e = new Entrada();
			e.setNom(this.entradaNom);
			e.setDatacreacio(new Date());
			e.setUsuari(origRequest.getRemoteUser()); 
			e.setActiva(this.activa);
				
			EntradaServ.addEntrada(e); // Cridem l'EJB
					 
			if (EntradaServ.getResultat()==true)
			{
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus d'entrada afegida correctament", "Tipus d'entrada afegida correctament"));				
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/tipus_entrada/llistat_tipusentrada.xhtml");
			}
			
			else
			{
				
				LOGGER.info("error obtingut: "+ EntradaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint el tipus d'entrada",  EntradaServ.getError()));
			}
		} 
		catch (Exception ex) {
				
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant el tipus d'entrada", ex.toString()));
		}
	}
	
	public void removeTipusEntrada()
	{
		Entrada e = (Entrada) this.taula_entrades.getRowData();
						
		LOGGER.info("Entram a remove amb paràmetre: " + e.getId());
		
		try
		{
			LOGGER.info("EJB lookup " + EntradaServ);
			
			EntradaServ.removeEntrada(e.getId());
		    			
			if (!EntradaServ.getResultat())
			{
				LOGGER.info("Error a remove: " + EntradaServ.getError());
				this.message = this.message + " -- " + EntradaServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el tipus d'entrada", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipus d'entrada eliminada correctament", "Tipus d'entrada eliminada correctament"));
			}
		} catch (Exception except) {
			LOGGER.info("Error_+ " + except.toString());
			this.message = this.message + " -- " + except.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant el tipus d'entrada", this.message));
		}
		
	}
		
}
