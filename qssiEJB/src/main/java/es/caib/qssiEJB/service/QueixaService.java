package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Queixa;
import es.caib.qssiEJB.interfaces.QueixaServiceInterface;

/**
 * Servei (EJB) per a l'entitat Queixa
 * @author [u97091] Antoni Juanico soler
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.QueixaService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class QueixaService implements QueixaServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(QueixaService.class);
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	@Override
	public void addQueixa(Queixa q) {
		
		LOGGER.info("in addQueixa, estat entity manager: " + em.toString());
			
		try
		{
			em.persist(q);
			LOGGER.info("Insert queixa");
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
	public ArrayList<Queixa> getLLista_queixes() {
		
		ArrayList<Queixa> l = new ArrayList<Queixa>();
		
		String queryString = new String("select q from Queixa q");
		try
		{
						
			LOGGER.info("in getLlista_Queixes, estat entity manager: " + em.toString());
			
			l = (ArrayList<Queixa>) em.createQuery(queryString).getResultList();
			
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

	@Override
	public Queixa getQueixa(Integer id_queixa) {
		try
		{
			LOGGER.info("in getQueixa, estat entity manager: " + em.toString());
			Queixa c = em.find(Queixa.class, id_queixa);
			return c;
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
	public void updateQueixa(Queixa q_update) {
		try 
		{
			LOGGER.info("in updateQueixa, estat entity manager: " + em.toString());
			
			Queixa q = em.find(Queixa.class, q_update.getId() );
			  
			q.setNom(q_update.getNom());
			q.setActiva(q_update.getActiva());
			q.setUsuari(q_update.getUsuari());
			q.setDatacreacio(q_update.getDatacreacio());
			  
			LOGGER.info("in updateQueixa, commit; ");
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			
		}
		
	}

}
