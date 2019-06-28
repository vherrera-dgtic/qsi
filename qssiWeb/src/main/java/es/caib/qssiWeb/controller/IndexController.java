package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.entity.Subcentre;
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
	private TreeNode arbre_expedients;
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
	
	// Obtenim els expedients assignats a l'usuari
	public ArrayList<Expedient> getLlista_expedients_assignats_usuari()
	{
		ExpedientServiceInterface ExpedientServ;
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String usuari = origRequest.getRemoteUser(); 
		
		LOGGER.info("Current user: " + usuari);
		
		try
		{
			ic = new InitialContext();
			ExpedientServ = (ExpedientServiceInterface) ic.lookup("qssiEAR/ExpedientService/local");
			LOGGER.info("EJB lookup2 "+ ExpedientServ);	
			this.llista_expedients = ExpedientServ.getLlista_Expedients_assignats_usuari(usuari); // Cridem l'EJB
			
			if (!ExpedientServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ ExpedientServ.getError());
				this.ambErrors = true;
				this.message = ExpedientServ.getError();
			}	
		}
		catch (Exception ex) {
			LOGGER.info("Error_+ " + ex.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + ex.toString();	
		}
		
		return this.llista_expedients; 
		
	}
	// Obtenim tots els expedients
	public TreeNode getLlista_expedients() 
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
			
			// Un cop obtinguda la llista d'expedients la convertim a format arbre
			Iterator<Expedient> i = this.llista_expedients.iterator();
			String conselleria_actual = new String("");
			String conselleria_anterior = new String("");
			ArrayList<DefaultTreeNode> nodes_conselleries = new ArrayList<DefaultTreeNode>();
			Expedient expedient_dummy = new Expedient();
			expedient_dummy.setDataentrada(new Date());
			expedient_dummy.setDatacreacio(new Date());
			Subcentre subcentre_node = new Subcentre();
			Centre centre_node = new Centre();
			centre_node.setNom("Hola");
			subcentre_node.setCentre(centre_node);
			expedient_dummy.setSubcentre(subcentre_node);
			
			this.arbre_expedients = new DefaultTreeNode(expedient_dummy, null);
			
			LOGGER.info("Recorrem els expedients per construir arbre");
			while (i.hasNext()) {
				
				Expedient e = i.next();
				conselleria_actual = e.getSubcentre().getCentre().getNom();
				LOGGER.info("Conselleria actual: " + conselleria_actual + "  conselleria_anterior: " + conselleria_anterior);
				
				if (conselleria_actual.equals(conselleria_anterior))
				{
					LOGGER.info("seguim igual");
					TreeNode pare = nodes_conselleries.get(nodes_conselleries.size()-1);
					TreeNode node_fulla = new DefaultTreeNode(conselleria_actual, e,pare);
					
				}
				else
				{
					LOGGER.info("construim nou node");
					// Canvi de conselleria
					expedient_dummy = new Expedient();
					expedient_dummy.setDataentrada(new Date());
					expedient_dummy.setDatacreacio(new Date()); 
					
					subcentre_node = new Subcentre();
					centre_node = new Centre();
					centre_node.setNom(conselleria_actual);
					subcentre_node.setCentre(centre_node);
					expedient_dummy.setSubcentre(subcentre_node);
					
					nodes_conselleries.add(new DefaultTreeNode(expedient_dummy,this.arbre_expedients));
					conselleria_anterior = centre_node.getNom();
					LOGGER.info("node construit " + centre_node.getNom());
					
				}
			}
			
		} catch (Exception ex) {
			LOGGER.info("Error_+ " + ex.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + ex.toString();
		}
		
		return this.arbre_expedients; 
	}
}
