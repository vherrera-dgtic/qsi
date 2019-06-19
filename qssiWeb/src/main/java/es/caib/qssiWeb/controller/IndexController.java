package es.caib.qssiWeb.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Expedient;
//import es.caib.qssiEJB.interfaces.ControladorInterface;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;

@ManagedBean
@ViewScoped
public class IndexController {
	
	/* Això no ens va bé. Hem aconseguit injectar EJB a un Servlet, mirar el Servlet de qssiWeb ProvaEJB
	 * però obtenim l'error següent quan fem la crida dins un @ManagedBean
	 * ERROR [org.jboss.web.jsf.integration.injection.JBossInjectionProvider] (http-127.0.0.1-8080-2) Injection failed on managed bean.
                                javax.naming.NameNotFoundException: xxx_xxxx not bound */
	//@EJB(mappedName="qssiEAR/ExpedientService/local")
	//ExpedientServiceInterface ExpedientServ2;
	//@EJB
	//ControladorInterface controllerBean;
	
	private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(IndexController.class);
			
	private ArrayList<Expedient> llista_expedients;
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a IndexController: ");
		//LOGGER.info("Prova injecció EJB: " + ExpedientServ2);
		//LOGGER.info("Prova injecció EJB: " + controllerBean);
	}
	
	public void setMissatge(String m) { this.message = m; }
	public String getMissatge() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	public ArrayList<Expedient> getLlista_expedients() 
	{ 
		ExpedientServiceInterface ExpedientServ;
		
		// Obtenim llista de matèries
		LOGGER.info("Obtenim llista d'expedients ");
		
		try
		{
			ic = new InitialContext();
			ExpedientServ = (ExpedientServiceInterface) ic.lookup("qssiEAR/ExpedientService/local");
			LOGGER.info("EJB lookup2 "+ ExpedientServ);	
			this.llista_expedients = ExpedientServ.getLlista_Expedients(); // Cridem l'EJB
			
			if (!ExpedientServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ ExpedientServ.getError());
				this.ambErrors = true;
				this.message = ExpedientServ.getError();
			}
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + e.toString();
		}
		
		return this.llista_expedients; 
	}
}
