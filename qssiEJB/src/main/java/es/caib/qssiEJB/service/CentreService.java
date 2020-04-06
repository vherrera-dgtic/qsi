package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
//import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;

/**
 * Servei (EJB) per a l'entitat Entrada
 * @author [u97091] Antoni Juanico soler
 * data 06/09/2018
 */

@Stateless
//@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
// Toni Juanico, 25/01/2019. Llevam els RolesAllowed per permetre accedir des del WebService (Helium)
public class CentreService implements CentreServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(CentreService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	@Override
	public void addCentre(Centre e) {
		LOGGER.info("in addCentre, estat entity manager: " + em.toString());
		
		try
		{
			em.persist(e);
			LOGGER.info("Insert centre");
			this.resultat = true;	
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.strError = ex.toString();
			this.resultat = false;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Centre> getLlista_CentresActiusWeb() {
		
		ArrayList<Centre> l = new ArrayList<Centre>();
		String queryString = new String("select e from Centre e where e.visible_web=1");
		
		try
		{
						
			LOGGER.info("in getLlista_CentresActiusWeb, estat entity manager: " + em.toString());
			
			l = (ArrayList<Centre>) em.createQuery(queryString).getResultList();
			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return l;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Centre> getLlista_Centres() {

		ArrayList<Centre> l = new ArrayList<Centre>();
		String queryString = new String("select e from Centre e order by e.nom");
		
		try
		{
						
			LOGGER.info("in getLlista_Centres, estat entity manager: " + em.toString());
			
			l = (ArrayList<Centre>) em.createQuery(queryString).getResultList();
			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return l;
	}
	
	@Override
	public boolean getResultat() { return this.resultat; }

	@Override
	public String getError() { return this.strError; }

	@Override
	public void removeCentre(Integer id_centre) {
		
		String queryString = new String("delete from Centre where id_centre = :id_centre");
		
		try
		{
			LOGGER.info("in removeCentre, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_centre", id_centre);
			query.executeUpdate();
			
			LOGGER.info("Removed centre");
			this.resultat = true;	
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
		}
	}

	@Override
	public Centre getCentre(Integer id_centre) {
		try
		{
			LOGGER.info("in getCentre, estat entity manager: " + em.toString());
			Centre c = em.find(Centre.class, id_centre);
			this.resultat = true;
			return c;
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			return null;
		}
		
	}

	@Override
	public void updateCentre(Centre c_update) {
		
		try 
		{
			LOGGER.info("in updateCentre, estat entity manager: " + em.toString());
			
			Centre c = em.find(Centre.class, c_update.getId() );

			//em.getTransaction().begin();
			  
			c.setNom(c_update.getNom());
			c.setDir3(c_update.getDir3());
			c.setActiu(c_update.getActiu());
			c.setVisible_web(c_update.getVisible_web());
			c.setUsuari(c_update.getUsuari());
			c.setDatacreacio(c_update.getDatacreacio());
			  
			//em.getTransaction().commit();
			
			LOGGER.info("in updateCentre, commit; ");
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			
		}
		
	}

}
