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

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;

/**
 * Controlador de la vista Materia
 * @author [u97091] Toni Juanico Soler
 * data: 20/09/2018
 */

@ManagedBean(name="MateriaController")
@ViewScoped
public class MateriaController {
	
	// Private properties
	private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(MateriaController.class);
	
	private DataTable taula_materies;
	private ArrayList<Materia> llista_materies;
	
	private String materiaId;
	private String materiaNom;
	private boolean activa;
	private String message = new String("");
	
	// Getters & Setters
	public void setMessage(String m) { this.message = m; }
	public String getMessage() { return this.message; }
			
	public DataTable getTaula_materies() { return taula_materies;	}
	public void setTaula_materies(DataTable dataTable) { this.taula_materies = dataTable;	}

	public String getMateriaId() { return this.materiaId; }
	public void setMateriaId(String mId) { this.materiaId = mId;}
		
	public String getMateriaNom() { return this.materiaNom; }
	public void setMateriaNom(String n) { this.materiaNom = n; }
		
	public boolean getActiva() { return this.activa;}
	public void setActiva(boolean a) { this.activa = a; }
	
	// Methods
	@PostConstruct
	public void init() {
		
		LOGGER.info("Proxy a MateriaController ");
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("materiaId_param");
				
		if (param!=null) {
			LOGGER.info("MateriaController amb materiaId_param= "+param);
			this.materiaId = param;
			this.getMateriaInfo(this.materiaId);
		}
				
	}
	
	public ArrayList<Materia> getLlista_materies() 
	{ 
		MateriaServiceInterface MateriaServ;
		
		LOGGER.info("Obtenim llista de matèries ");
		
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");	
			LOGGER.info("EJB lookup "+ MateriaServ);	
			
			this.llista_materies = MateriaServ.getLlista_Materies(); // Cridem l'EJB
			
			if (!MateriaServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				this.message = MateriaServ.getError();
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_materies; 
	}
	
	public void getMateriaInfo(String materiaId) {
		
		MateriaServiceInterface MateriaServ;
		LOGGER.info("getMateriaInfo: " + materiaId);
			
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");
			LOGGER.info("EJB lookup " + MateriaServ);
				
			Materia m = new Materia();
			m = MateriaServ.getMateria(Integer.parseInt(materiaId));
				
			if (MateriaServ.getResultat())
			{
				this.materiaId = materiaId;
				this.materiaNom = m.getNom();
				this.activa = m.getActiva(); 
			}
			else
			{
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint la materia",  MateriaServ.getError()));
			}
				
		}
		catch(Exception ex)
		{
			LOGGER.info("error obtingut: "+ ex.toString());
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error obtenint la materia",  ex.toString()));
		}
	}
	
	public void updateMateria() {
		
		MateriaServiceInterface MateriaServ;
			
		LOGGER.info("updateMateria");
			
		try 
		{
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    ic = new InitialContext();
		    MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");
		    LOGGER.info("EJB lookup" + MateriaServ + "--> materiaId: " + this.materiaId);
		    	
		    // Construim la matèria
		    Materia m = new Materia();
		    m.setId(Integer.parseInt(this.materiaId));
			m.setNom(this.materiaNom);
			m.setDatacreacio(new Date());
			m.setUsuari(origRequest.getRemoteUser()); 
			m.setActiva(this.activa);
							
		    MateriaServ.updateMateria(m);
		    	
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Matèria actualitzada correctament", "Matèria actualitzada correctament"));				
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/materies/llistat_materia.xhtml");
		} 
		catch (Exception ex) {
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error actualitzant la matèria", ex.toString()));
		}
	}
	
	public void addMateria()
	{
		MateriaServiceInterface MateriaServ;
			
		LOGGER.info("addMateria ");
			
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");	
			LOGGER.info("EJB lookup "+ MateriaServ);	
				
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			// Contruim el centre
			Materia m = new Materia();
			m.setNom(this.materiaNom);
			m.setDatacreacio(new Date());
			m.setUsuari(origRequest.getRemoteUser()); 
			m.setActiva(this.activa);
				
			MateriaServ.addMateria(m); // Cridem l'EJB
					 
			if (MateriaServ.getResultat()==true)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Matèria afegida correctament", "Matèria afegida correctament"));				
				//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); -- Ojo, això no acaba de funcionar per un Bug a Mojarra 1.2_13
			    FacesContext.getCurrentInstance().getExternalContext().redirect(origRequest.getContextPath()  + "/manteniments/materies/llistat_materia.xhtml");
			}
			
			else
			{
				
				LOGGER.info("error obtingut: "+ MateriaServ.getError());
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error afegint la matèria",  MateriaServ.getError()));
			}
		} 
		catch (Exception ex) {
				
			LOGGER.info("Error: " + ex.toString());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desant la matèria", ex.toString()));
		}
	}
	
	public void removeMateria()
	{
		Materia m = (Materia) this.taula_materies.getRowData();
		MateriaServiceInterface MateriaServ;
				
		LOGGER.info("Entram a remove amb paràmetre: " + m.getId());
		
		try
		{
			ic = new InitialContext();
			MateriaServ = (MateriaServiceInterface) ic.lookup("es.caib.qssiEJB.service.MateriaService");
			LOGGER.info("EJB lookup " + MateriaServ);
			
			MateriaServ.removeMateria(m.getId());
		    			
			if (!MateriaServ.getResultat())
			{
				LOGGER.info("Error a remove: " + MateriaServ.getError());
				this.message = this.message + " -- " + MateriaServ.getError();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant la matèria", this.message));
			}
			else
			{
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Matèria eliminada correctament", "Matèria eliminada correctament"));
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant la matèria", this.message));
		}
		catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.message = this.message + " -- " + e.toString();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error eliminant la matèria", this.message));
		}
		
	}
	
}
