package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;

import es.caib.qssiEJB.entity.Motiu;
import es.caib.qssiEJB.interfaces.MotiuServiceInterface;

/**
 * Servei (EJB) per a l'entitat Materia
 * @author [u97091] Antoni Juanico soler
 */

@Stateless
@LocalBinding(jndiBinding="es.caib.qssiEJB.service.MotiuService")
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class MotiuService implements MotiuServiceInterface{

	private final static Logger LOGGER = Logger.getLogger(MotiuService.class);
	
	private String strError = new String("");
	private boolean resultat;
	
	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	@Override
	public void addMotiu(Motiu m) {
		try
		{
			LOGGER.info("in addMotiu, estat entity manager: " + em.toString());
			
			em.getTransaction().begin();
			em.persist(m);
			em.getTransaction().commit();
			em.close();
			
			LOGGER.info("Inserit motiu");
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
	public ArrayList<Motiu> getLlista_Motius() {

		ArrayList<Motiu> l = new ArrayList<Motiu>();
		
		String queryString = new String("select m from Motiu m");
		try
		{
						
			LOGGER.info("in getLlista_Motius, estat entity manager: " + em.toString());
			
			l = (ArrayList<Motiu>) em.createQuery(queryString).getResultList();
			
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
