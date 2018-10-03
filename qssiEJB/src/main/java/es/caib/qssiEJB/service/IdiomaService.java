package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Idioma;
import es.caib.qssiEJB.interfaces.IdiomaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Idioma
 * @author [u97091] Antoni Juanico soler
 * data 11/09/2018
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.IdiomaService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class IdiomaService implements IdiomaServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(EscritService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	
	
	@Override
	public void addIdioma(Idioma i) {
		try
		{
			LOGGER.info("in addIdioma, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(i);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserit idioma");
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
	public ArrayList<Idioma> getLlista_Idiomes() {

		ArrayList<Idioma> l = new ArrayList<Idioma>();
		
		String queryString = new String("select i from Idioma i");
		try
		{
						
			LOGGER.info("in getLlista_Idiomes, estat entity manager: " + em.toString());
			
			l = (ArrayList<Idioma>) em.createQuery(queryString).getResultList();
			
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
	public boolean getResultat() { return this.resultat;	}

	@Override
	public String getError() { return this.strError; }
	
}
