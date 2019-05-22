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

import es.caib.qssiEJB.entity.Motiu;
import es.caib.qssiEJB.interfaces.MotiuServiceInterface;

/**
 * Servei (EJB) per a l'entitat Materia
 * @author [u97091] Antoni Juanico soler
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.MotiuService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class MotiuService implements MotiuServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(MotiuService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a entityManager: "+this.em);
	}
	
	public boolean getResultat() { return this.resultat; }
	public String getError() { return this.strError; }
	
	@Override
	public void addMotiu(Motiu m) {
		
		LOGGER.info("in addMotiu, estat entity manager: " + em.toString());
		try
		{
			em.persist(m);
			LOGGER.info("Insert motiu");
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
	public ArrayList<Motiu> getLlista_Motius() {

		ArrayList<Motiu> l = new ArrayList<Motiu>();
		
		String queryString = new String("select m from Motiu m");
		try
		{
						
			LOGGER.info("in getLlista_Motius, estat entity manager: " + em.toString());
			
			l = (ArrayList<Motiu>) em.createQuery(queryString).getResultList();
			
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
	public void updateMotiu(Motiu m_update) {
		
		try 
		{
			LOGGER.info("in updateMotiu, estat entity manager: " + em.toString());
			
			Motiu m = em.find(Motiu.class, m_update.getId() );
			  
			m.setNom(m_update.getNom());
			m.setActiu(m_update.getActiu());
			m.setUsuari(m_update.getUsuari());
			m.setDatacreacio(m_update.getDatacreacio());
			  
			LOGGER.info("in updateMotiu, commit; ");
			
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
	public Motiu getMotiu(Integer id_motiu) {
		try
		{
			LOGGER.info("in getMotiu, estat entity manager: " + em.toString());
			Motiu m = em.find(Motiu.class, id_motiu);
			this.resultat = true;
			return m;
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
	public void removeMotiu(Integer id_motiu) {
		
		String queryString = new String("delete from Motiu where id_motiu = :id_motiu");
		
		try
		{
			LOGGER.info("in removeMotiu, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_motiu", id_motiu);
			query.executeUpdate();
			
			LOGGER.info("Removed motiu");
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
