package es.caib.qssiEJB.service;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Expedient;
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
		
				
		try
		{
			// La idea és obtenir el següent valor de la seqüència qsi_expedient_seq_ANY
			// NOTA, Toni Juanico, 20/06/2019 amb la finalitat d'evitar repeticions en els
			// números de seqüència la idea és fer un bloqueig, llegir el núm. incrementar-lo i actualitzar-lo
			// això amb JPA 2.0 és pot fer mitjançant la següent sentència
			// SequenciaExpedient se = em.find(SequenciaExpedient.class,any,LockModeType.PESSIMISTIC_WRITE);
			// però amb JPA 1.0 no existeix aquesta signatura i per tant ho fem directament amb query's damunt la base de dades (createQueryString);
			// Recordem que a JBoss 5.2 tenim JPA 1.0, JSP 2.1, EJB 3.0, etc.
						
			String strQuery = "select valor from QSI_SEQUENCIA_EXPEDIENT where id_sequencia =" + any +" for update";
			
			LOGGER.info("Obtenim clau primaria " + e.getEmail() + " query: " + strQuery );
			
			Query myQuery = em.createNativeQuery(strQuery);

			Integer val = (Integer) myQuery.getSingleResult();
			
			LOGGER.info("Obtinguda: " + val);
			
			if (val != null) 
			{
				e.setId((any * 100000)+ val);
				LOGGER.info("Calculada: " + e.getId());
				
				if (e.getEmail().equals("dorm@dorm.net"))
				{
					LOGGER.info("dormim 60 segons havent obtingut la clau: " + e.getId());
					Thread.sleep(60000);
				}
				
				val = val + 1;
				strQuery = "update QSI_SEQUENCIA_EXPEDIENT set valor=" + val + " where id_sequencia=" + any;
				LOGGER.info("Ara executem " + strQuery);
				myQuery = em.createNativeQuery(strQuery);
				myQuery.executeUpdate();
				
				return true;
			}
			else
			{
				// No existeix la seqüència per l'any donat
				LOGGER.info("No existeix la seqüència per l'any donat");
				return false;
			}
			
			/*
			SequenciaExpedient se = em.find(SequenciaExpedient.class,any);
					
			if (se != null)
			{
				LOGGER.info("Execucio find valor " + se.getValor() + " i  correu " + e.getEmail());
				em.lock(se, LockModeType.WRITE); // Bloqueig de l'any en qüestió
				LOGGER.info("Execució lock en mode WRITE amb valor actualitzat " + se.getValor());
				
				e.setId((any * 100000)+ se.getValor());
				
				LOGGER.info("obtinguda clau primària d'expedient amb transacció: " + e.getId());
				
				if (e.getEmail().equals("dorm@dorm.net"))
				{
					LOGGER.info("dormim 60 segons havent obtingut la clau: " + e.getId());
					Thread.sleep(60000);
				}
				
				se.nextval();
				LOGGER.info("Incrementat valor");
				em.persist(se);
				LOGGER.info("Persist valor");
				
				return true;
							
			}
			else
			{
				// No existeix la seqüència per l'any donat
				LOGGER.info("No existeix la seqüència per l'any donat");
				return false;
			}
			*/
		}
		catch(Exception ex)
		{
			LOGGER.info("Error obtenint la seqüència: " + ex.toString());
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
