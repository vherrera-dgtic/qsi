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
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;

/**
 * Servei (EJB) per a l'entitat Subcentre
 * @author [u97091] Antoni Juanico soler
 * data 07/09/2018
 */

@Stateless
//@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
//Toni Juanico, 25/01/2019. Llevam els RolesAllowed per permetre accedir des del WebService (Helium)
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
		
		try
		{
			em.persist(sc);
			LOGGER.info("Insert subcentre");
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
	public ArrayList<Subcentre> getLlista_SubcentresActiusWebperDir3(String dir3) {
		ArrayList<Subcentre> lsc = new ArrayList<Subcentre>();
		
		String queryString = new String("select sc from Subcentre sc where sc.visible_web=true and sc.centre.dir3 = :dir3");
		try
		{
						
			LOGGER.info("in getLlista_SubcentresActiusWebperDi3, amb paràmetre dir3: " + dir3 + " estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("dir3", dir3);
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

	@Override
	public void updateSubcentre(Subcentre sc_update) {
		try 
		{
			LOGGER.info("in updateSubcentre, estat entity manager: " + em.toString());
			
			Subcentre sc = em.find(Subcentre.class, sc_update.getId() );
						  
			sc.setNom(sc_update.getNom());
			sc.setDir3(sc_update.getDir3());
			sc.setActiu(sc_update.getActiu());
			sc.setVisible_web(sc_update.getVisible_web());
			sc.setUsuari(sc_update.getUsuari());
			sc.setDatacreacio(sc_update.getDatacreacio());
			
			Centre c = new Centre();
			c.setId(sc_update.getCentre().getId());
			sc.setCentre(c);
			  
			LOGGER.info("in updateSubcentre, commit; ");
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			
		}
	}

	@Override
	public Subcentre getSubcentre(Integer id_subcentre) {
		try
		{
			LOGGER.info("in getSubcentre, estat entity manager: " + em.toString());
			Subcentre sc = em.find(Subcentre.class, id_subcentre);
			this.resultat = true;
			return sc;
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
	public void removeSubcentre(Integer id_subcentre) {
		String queryString = new String("delete from Subcentre where id_subcentre = :id_subcentre");
		
		try
		{
			LOGGER.info("in removeSubcentre, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_subcentre", id_subcentre);
			query.executeUpdate();
			
			LOGGER.info("Delete subcentre");
			this.resultat = true;	
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
		}
		
	}
	
}
