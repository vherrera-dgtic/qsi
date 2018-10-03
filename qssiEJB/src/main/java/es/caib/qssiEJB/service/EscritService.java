package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.interfaces.EscritServiceInterface;

/**
 * Servei (EJB) per a l'entitat Escrit
 * @author [u97091] Antoni Juanico soler
 * data 05/09/2018
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.EscritService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class EscritService implements EscritServiceInterface{

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
	public void addEscrit(Escrit e) {
		try
		{
			LOGGER.info("in addEscrit, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
			em.close();
			
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
	public boolean getResultat() { return this.resultat;	}

	@Override
	public String getError() { return this.strError;	}

}