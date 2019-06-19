package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Identificacio;
import es.caib.qssiEJB.interfaces.IdentificacioServiceInterface;

/**
 * Servei (EJB) per a l'entitat Identificacio
 * @author [u97091] Antoni Juanico soler
 * data 14/09/2018
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class IdentificacioService implements IdentificacioServiceInterface {

	private final static Logger LOGGER = Logger.getLogger(IdentificacioService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	@Override
	public void addIdentificacio(Identificacio i) {
		
		try
		{
			LOGGER.info("in addIdentificacio, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(i);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserida identificacio");
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
	public ArrayList<Identificacio> getLlista_Identificacions() {

		ArrayList<Identificacio> l = new ArrayList<Identificacio>();
		
		String queryString = new String("select i from Identificacio i where i.activa=true");
		try
		{
						
			LOGGER.info("in getLlista_Identificacions, estat entity manager: " + em.toString());
			
			l = (ArrayList<Identificacio>) em.createQuery(queryString).getResultList();
			
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
