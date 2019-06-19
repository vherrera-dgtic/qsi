package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Illa;
import es.caib.qssiEJB.interfaces.IllaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Illa
 * @author [u97091] Antoni Juanico soler
 * data 11/09/2018
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class IllaService implements IllaServiceInterface {

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
	public void addIlla(Illa i) {
		try
		{
			LOGGER.info("in addIlla, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(i);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserida illa");
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
	public ArrayList<Illa> getLlista_Illes() {
		ArrayList<Illa> l = new ArrayList<Illa>();
		
		String queryString = new String("select i from Illa i");
		try
		{
						
			LOGGER.info("in getLlista_Illes, estat entity manager: " + em.toString());
			
			l = (ArrayList<Illa>) em.createQuery(queryString).getResultList();
			
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
