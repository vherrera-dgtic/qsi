package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Materia
 * @author [u97091] Antoni Juanico soler
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN", "PBASE_ADMIN","JBOSSADMIN"}) // Si tothom -> sobren els altres rols
public class MateriaService implements MateriaServiceInterface {

	private final static Logger LOGGER = Logger.getLogger(MateriaService.class);
	
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
	public void addMateria(Materia m) {
		
		LOGGER.info("in addMateria, estat entity manager: " + em.toString());
		try
		{
			em.persist(m);
			LOGGER.info("Insert materia");
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
	public ArrayList<Materia> getLlista_Materies() {

		ArrayList<Materia> l = new ArrayList<Materia>();
		
		String queryString = new String("select m from Materia m");
		try
		{
						
			LOGGER.info("in getLlista_Materies, estat entity manager: " + em.toString());
			
			l = (ArrayList<Materia>) em.createQuery(queryString).getResultList();
			
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
	public void updateMateria(Materia m_update) {
		
		try 
		{
			LOGGER.info("in updateMateria, estat entity manager: " + em.toString());
			
			Materia m = em.find(Materia.class, m_update.getId() );
			  
			m.setNom(m_update.getNom());
			m.setActiva(m_update.getActiva());
			m.setUsuari(m_update.getUsuari());
			m.setDatacreacio(m_update.getDatacreacio());
			  
			LOGGER.info("in updateMateria, commit; ");
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			
		}
		
	}

	@Override
	public Materia getMateria(Integer id_materia) {
		try
		{
			LOGGER.info("in getMateria, estat entity manager: " + em.toString());
			Materia m = em.find(Materia.class, id_materia);
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
	public void removeMateria(Integer id_materia) {
		
		String queryString = new String("delete from Materia where id_materia = :id_materia");
		
		try
		{
			LOGGER.info("in removeMateria, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_materia", id_materia);
			query.executeUpdate();
			
			LOGGER.info("Removed materia");
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
