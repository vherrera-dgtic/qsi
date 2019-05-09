package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
//import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Provincia;
import es.caib.qssiEJB.interfaces.ProvinciaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Provincia
 * @author [u97091] Antoni Juanico soler
 * data 28/01/2019
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.ProvinciaService")
//@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
//Toni Juanico, 28/01/2019. Llevam els RolesAllowed per permetre accedir des del WebService (Helium)
public class ProvinciaService implements ProvinciaServiceInterface {
	
	private final static Logger LOGGER = Logger.getLogger(ProvinciaService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	@Override
	public void addProvincia(Provincia p) {
		try
		{
			LOGGER.info("in addProvincia, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(p);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserida provincia");
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
	public ArrayList<Provincia> getLlista_Provincies() {
		ArrayList<Provincia> l = new ArrayList<Provincia>();
		
		String queryString = new String("select p from Provincia p");
		try
		{
						
			LOGGER.info("in getLlista_provincies, estat entity manager: " + em.toString());
			
			l = (ArrayList<Provincia>) em.createQuery(queryString).getResultList();
			
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
}
