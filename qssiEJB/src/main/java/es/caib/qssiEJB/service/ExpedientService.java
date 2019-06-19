package es.caib.qssiEJB.service;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.entity.SequenciaExpedient;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;

/**
 * Servei (EJB) per a l'entitat Expedient
 * @author [u97091] Antoni Juanico soler
 * data 18/09/2018
 */

@Stateless
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
	
	private boolean getPrimaryKey(Integer any, Expedient e) {
		
		String queryStringPK = new String("");
		
		try
		{
			// La idea és obtenir el següent valor de la seqüència qsi_expedient_seq_ANY
			SequenciaExpedient se = em.find(SequenciaExpedient.class,any,javax.persistence.LockModeType.PESSIMISTIC_WRITE);
			//SequenciaExpedient se = em.find(SequenciaExpedient.class,any);
			//em.lock(se, LockModeType.PESSIMISTIC_WRITE);
			
			if (se != null)
			{
				e.setId((any * 100000)+ se.getValor());
				
				LOGGER.info("obtinguda clau primària d'expedient amb transacció: " + e.getId());
				
				if (e.getEmail().equals("dorm@dorm.net"))
				{
					LOGGER.info("dormim 60 segons havent obtingut la clau: " + e.getId());
					Thread.sleep(60000);
				}
				
				se.nextval();
				em.persist(se);
				return true;
							
			}
			else
			{
				// No existeix la seqüència per l'any donat
				LOGGER.info("No existeix la seqüència per l'any donat");
				return false;
			}
		}
		catch(Exception ex)
		{
			LOGGER.info("Error obtenint la seqüència: " + queryStringPK + " descripció de l'error: " + ex.toString());
			return false;
		}
	}
	
	private boolean generateSequence(long any) {
		try {
		
			String queryStringCreatePK = new String("INSERT INTO qsi_sequencia_expedient (id_sequencia, valor) values (:num_any, 1)");
			Query queryCreatePK = em.createNativeQuery(queryStringCreatePK);
			queryCreatePK.setParameter("num_any", any);
			queryCreatePK.executeUpdate();
			LOGGER.info("Generada seqüència nova: qsi_expedient_seq_" + any);
			return true;
		}
		catch (Exception ex) {
			LOGGER.info("error generant nova seqüència: " + ex.toString());
			return false;
		}	
	}
	
	@Override
	public void addExpedient(Expedient e) {
		
		LOGGER.info("in addExpedient, estat entity manager: " + em.toString());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(e.getDataentrada());
		Integer any = calendar.get(Calendar.YEAR);
		
		boolean obtingudaPK;
		obtingudaPK = getPrimaryKey(any,e);
		
		if (obtingudaPK)
		{
			try {
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
		else
		{
			// Si no hem pogut obtenir la clau primària molt probablement no existeix la seqüència, 
			// estem a un any nou i s'ha de generar la seqüència
			if (generateSequence(any)){
				obtingudaPK = getPrimaryKey(any,e);
				if (obtingudaPK)
				{
					try {
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
			}
			else
			{
				LOGGER.info("No s'ha pogut generar la seqüència per a obtenir la clau primària: ");
				this.resultat = false;
			}
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
