package es.caib.qssiEJB.service;

//import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;

/**
 * Servei (EJB) per a l'entitat Expedient
 * @author [u97091] Antoni Juanico soler
 * data 18/09/2018
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.ExpedientService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class ExpedientService implements ExpedientServiceInterface {

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
	public void addExpedient(Expedient e) {
		
		LOGGER.info("in addExpedient, estat entity manager: " + em.toString());
		
		try
		{
			// La idea és fer una select per construir la clau primària any + 00000 + núm.
			// TODO: aquesta operació hauria d'esser atòmica
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e.getDataentrada());
			long any = calendar.get(Calendar.YEAR);
			String queryString = new String("select count(*) from Expedient where date_part('year',data_entrada)= :any");
			Query query = em.createQuery(queryString);
			query.setParameter("any", any);
			long comptador = (long) query.getSingleResult();
			e.setId((any * 100000) + comptador + 1);
			LOGGER.info("creada clau primària d'expedient: " + e.getId());
			
			// Afegim l'expedient
			em.persist(e);
			LOGGER.info("Inserit expedient");
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
	public ArrayList<Expedient> getLlista_Expedients() {
		
		ArrayList<Expedient> l = new ArrayList<Expedient>();
		
		String queryString = new String("select e from Expedient e order by id_expedient");
		try
		{
						
			LOGGER.info("in getLlista_Expedients, estat entity manager: " + em.toString());
			
			l = (ArrayList<Expedient>) em.createQuery(queryString).getResultList();
			
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
	public boolean getResultat() {return this.resultat;	}

	@Override
	public String getError() { return this.strError;	}

}
