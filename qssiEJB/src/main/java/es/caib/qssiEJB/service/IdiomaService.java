package es.caib.qssiEJB.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
	public boolean getResultat() { return this.resultat;	}
	public String getError() { return this.strError; }
	
	
	@Override
	public void addIdioma(Idioma i) {
		
		LOGGER.info("in addIdioma, estat entity manager: " + em.toString());
		
		try
		{
			em.persist(i);
			LOGGER.info("Insert Idioma");
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
	public void updateIdioma(Idioma i_update) {
		
		try 
		{
			LOGGER.info("in updateIdioma, estat entity manager: " + em.toString());
			
			Idioma i = em.find(Idioma.class, i_update.getId() );
			  
			i.setNom(i_update.getNom());
			i.setActiu(i_update.getActiu());
			i.setUsuari(i_update.getUsuari());
			i.setDatacreacio(i_update.getDatacreacio());
			  
			LOGGER.info("in updateIdioma, commit; ");
			
			this.resultat = true;
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			
		}
		
	}

	@Override
	public Idioma getIdioma(Integer id_idioma) {
		try
		{
			LOGGER.info("in getIdioma, estat entity manager: " + em.toString());
			Idioma i = em.find(Idioma.class, id_idioma);
			this.resultat = true;
			return i;
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
	public void removeIdioma(Integer id_idioma) {
		
		String queryString = new String("delete from Idioma where id_idioma = :id_idioma");
		
		try
		{
			LOGGER.info("in removeIdioma, estat entity manager: " + em.toString());
			
		    Query query = em.createQuery(queryString);
		    query.setParameter("id_idioma", id_idioma);
			query.executeUpdate();
			
			LOGGER.info("Removed idioma");
			this.resultat = true;	
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
		}	
	}
	
}
