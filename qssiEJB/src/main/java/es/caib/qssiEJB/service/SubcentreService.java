package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

/**
 * Servei (EJB) per a l'entitat Subcentre
 * @author [u97091] Antoni Juanico soler
 * data 07/09/2018
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.SubcentreService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class SubcentreService implements SubcentreServiceInterface {
	
	private final static Logger LOGGER = Logger.getLogger(SubcentreService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	@Override
	public void addSubcentre(Subcentre sc) {
		
		LOGGER.info("in addSubcentre, estat entity manager: " + em.toString());
		
		em.getTransaction().begin();
		em.persist(sc);
		em.getTransaction().commit();
		em.close();
		
		LOGGER.info("Inserit subcentre");
		this.resultat = true;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Subcentre> getLlista_SubcentresActiusWeb(Integer id_centre) {
		ArrayList<Subcentre> lsc = new ArrayList<Subcentre>();
		
		String queryString = new String("select sc from Subcentre sc where sc.visible_web=true and sc.centre.id_centre = :id_centre");
		try
		{
						
			LOGGER.info("in getLlista_SubcentresActiusWeb, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_centre", id_centre);
		    lsc = (ArrayList<Subcentre>) query.getResultList();
		    
			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return lsc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Subcentre> getLlista_Subcentres(Integer id_centre) {
		ArrayList<Subcentre> lsc = new ArrayList<Subcentre>();
		
		String queryString = new String("select sc from Subcentre sc where sc.centre.id_centre = :id_centre");
		try
		{
						
			LOGGER.info("in getLlista_Subcentres, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_centre", id_centre);
		    lsc = (ArrayList<Subcentre>) query.getResultList();
		    			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return lsc;
	}
	
	@Override
	public boolean getResultat() { return this.resultat; }

	@Override
	public String getError() { return this.strError; }
	
}
