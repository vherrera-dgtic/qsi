package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;

/**
 * Servei (EJB) per a l'entitat Escrit
 * @author [u97091] Antoni Juanico soler
 * data 05/09/2018
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class EscritService implements EscritServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(EscritService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	public boolean getResultat() { return this.resultat;	}
	public String getError() { return this.strError;	}
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a entityManager: "+this.em);
	}
	
	@Override
	public void addEscrit(Escrit e) {
		
		LOGGER.info("in addEscrit, estat entity manager: " + em.toString());
		
		try
		{
			em.persist(e);
			LOGGER.info("Inserit escrit");
			this.resultat = true;
		}
		catch(Exception ex) {
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Escrit> getLlista_Escrits() {

		ArrayList<Escrit> l = new ArrayList<Escrit>();
		
		String queryString = new String("select e from Escrit e");
		try
		{
						
			LOGGER.info("in getLlista_Escrits, estat entity manager: " + em.toString());
			
			l = (ArrayList<Escrit>) em.createQuery(queryString).getResultList();
			
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
	public void updateEscrit(Escrit e_update) {
		
		try 
		{
			LOGGER.info("in updateEscrit, estat entity manager: " + em.toString());
			
			Escrit e = em.find(Escrit.class, e_update.getId() );
			  
			e.setNom(e_update.getNom());
			e.setActiu(e_update.getActiu());
			e.setUsuari(e_update.getUsuari());
			e.setDatacreacio(e_update.getDatacreacio());
			  
			LOGGER.info("in updateEscrit, commit; ");
			
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
	public Escrit getEscrit(Integer id_escrit) {
		try
		{
			LOGGER.info("in getEscrit, estat entity manager: " + em.toString());
			Escrit e = em.find(Escrit.class, id_escrit);
			this.resultat = true;
			return e;
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
	public void removeEscrit(Integer id_escrit) {
		
		String queryString = new String("delete from Escrit where id_escrit = :id_escrit");
		
		try
		{
			LOGGER.info("in removeEscrit, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_escrit", id_escrit);
			query.executeUpdate();
			
			LOGGER.info("Removed escrit");
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
