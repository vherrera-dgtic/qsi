package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Materia;
import es.caib.qssiEJB.interfaces.MateriaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Materia
 * @author [u97091] Antoni Juanico soler
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.MateriaService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
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
	
	public void addMateria(Materia m) {
		
		try
		{
			LOGGER.info("in addMateria, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(m);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserida matèria");
			this.resultat = true;
		}
		catch(Exception ex) {
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
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
	
	

}
