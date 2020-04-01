package es.caib.qssiWeb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
	
	@EJB
	ExpedientServiceInterface ExpedientServ;
	
	//private InitialContext ic;
	private final static Logger LOGGER = Logger.getLogger(IndexController.class);
			
	private ArrayList<Expedient> llista_expedients;
	private TreeNode arbre_expedients;
	private String message = new String("");
	private boolean ambErrors = false;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a IndexController: ");
		LOGGER.info("Prova injecció EJB: " + ExpedientServ);
		//LOGGER.info("Prova injecció EJB: " + controllerBean);
	}
	
	public void setMissatge(String m) { this.message = m; }
	public String getMissatge() { return this.message; }
	public boolean getAmbErrors() { return this.ambErrors; }
	
	// Obtenim els expedients assignats a l'usuari
	public ArrayList<Expedient> getLlista_expedients_assignats_usuari()
	{
		//ExpedientServiceInterface ExpedientServ;
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String usuari = origRequest.getRemoteUser(); 
		
		LOGGER.info("Current user: " + usuari);
		
		try
		{
			//ic = new InitialContext();
			//ExpedientServ = (ExpedientServiceInterface) ic.lookup("qssiEAR/ExpedientService/local");
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
		//ExpedientServiceInterface ExpedientServ;
		ExpedientServiceInterface.TipusCerca tipuscerca;
		
		String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("f");
		
		// Obtenim llista de matèries
		LOGGER.info("Obtenim llista d'expedients amb paràmetre: " + param);
		
		try
		{
			//ic = new InitialContext();
			//ExpedientServ = (ExpedientServiceInterface) ic.lookup("qssiEAR/ExpedientService/local");
			LOGGER.info("EJB lookup2 "+ ExpedientServ);
			
			switch (param)
			{
				case "centre": 			tipuscerca = ExpedientServiceInterface.TipusCerca.PENDENTS_ASSIGNAR_PER_CENTRE; break;
				case "estat": 		tipuscerca = ExpedientServiceInterface.TipusCerca.PENDENTS_ASSIGNAR_PER_ESTAT; break;
				case "rebutjades": 		tipuscerca = ExpedientServiceInterface.TipusCerca.REBUTJADES; break;
				case "finalitzades": 	tipuscerca = ExpedientServiceInterface.TipusCerca.FINALITZADES; break;
				case "pendents_resposta": 	tipuscerca = ExpedientServiceInterface.TipusCerca.PENDENTS_RESPOSTA; break;
				case "totes_centre": 			tipuscerca  = ExpedientServiceInterface.TipusCerca.TOTES_PER_CENTRE; break;
				case "totes_estat": 			tipuscerca  = ExpedientServiceInterface.TipusCerca.TOTES_PER_ESTAT; break;
				default: tipuscerca = ExpedientServiceInterface.TipusCerca.TOTES_PER_CENTRE;
			}
			
			this.llista_expedients = ExpedientServ.getLlista_Expedients(tipuscerca);
			
			if (ExpedientServ.getResultat())
			{
				agrupaElements(tipuscerca);
			}
			else
			{
				LOGGER.info("error obtingut: "+ ExpedientServ.getError());
				this.ambErrors = true;
				this.message = ExpedientServ.getError();	
			}
			
		} catch (Exception ex) {
			LOGGER.info("Error_+ " + ex.toString());
			this.ambErrors = true;
			this.message = this.message + " -- " + ex.toString();
		}
		
		return this.arbre_expedients; 
	}
	
	private void agrupaElements(ExpedientServiceInterface.TipusCerca tc)
	{
		// Un cop obtinguda la llista d'expedients la convertim a format arbre
		Iterator<Expedient> i = this.llista_expedients.iterator();
		String element_actual = new String("");
		String element_anterior = new String("");
		ArrayList<DefaultTreeNode> nodes = new ArrayList<DefaultTreeNode>();
		Expedient expedient_dummy = new Expedient();
		expedient_dummy.setId(0);
		expedient_dummy.setDataentrada(new Date());
		expedient_dummy.setDatacreacio(new Date());
		Centre centre_node = new Centre();
		centre_node.setNom("Centre dummy");
		
		expedient_dummy.setCentre(centre_node);
		
		Integer num_nodes = 0;
		this.arbre_expedients = new DefaultTreeNode(expedient_dummy, null);
		
		LOGGER.info("Recorrem els expedients per construir arbre");
		while (i.hasNext()) {
			
			Expedient e = i.next();
			
			if (tc == ExpedientServiceInterface.TipusCerca.PENDENTS_ASSIGNAR_PER_ESTAT || tc == ExpedientServiceInterface.TipusCerca.TOTES_PER_ESTAT)
			{
				ExpedientServiceInterface.EstatExpedient estat_expedient = ExpedientServiceInterface.EstatExpedient.valueOf(e.getEstat());
				element_actual = estat_expedient.getTag();
			}
			else
			{
				element_actual = e.getCentre().getNom();
			}
			
			if (element_actual.equals(element_anterior))
			{
				TreeNode pare = nodes.get(nodes.size()-1);
				TreeNode node_fulla = new DefaultTreeNode(element_actual, e,pare);
				num_nodes = num_nodes + 1;
			}
			else
			{
				// Actualitzam els elements
				TreeNode pare;
				if (nodes.size()>0)
				{
					pare = nodes.get(nodes.size()-1);
					expedient_dummy = (Expedient) pare.getData();
					expedient_dummy.getCentre().setNom("(" + num_nodes + ") " + expedient_dummy.getCentre().getNom());
						
				}
				
				// Canvi d'element
				expedient_dummy = new Expedient();
				expedient_dummy.setId(0);
				expedient_dummy.setDataentrada(new Date());
				expedient_dummy.setDatacreacio(new Date()); 
				
				centre_node = new Centre();
				centre_node.setNom(element_actual);
				expedient_dummy.setCentre(centre_node);
				
				nodes.add(new DefaultTreeNode(expedient_dummy,this.arbre_expedients));
				element_anterior = centre_node.getNom();
								
				pare = nodes.get(nodes.size()-1);
				TreeNode node_fulla = new DefaultTreeNode(element_actual, e, pare);
				num_nodes = 1;
			}
		}
		// Al finalitzar agefim l'etiqueta al darrer element
		TreeNode pare;
		if (nodes.size()>0)
		{
			pare = nodes.get(nodes.size()-1);
			expedient_dummy = (Expedient) pare.getData();
			expedient_dummy.getCentre().setNom("(" + num_nodes + ") " + expedient_dummy.getCentre().getNom());
		}
	}
	
}
