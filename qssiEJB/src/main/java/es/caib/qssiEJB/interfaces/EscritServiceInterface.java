package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Escrit;

/**
 * Interfície del servei (EJB) EscritService
 * @author [u97091] Toni Juanico Soler
 * data: 05/09/2018
 */

@Local
public interface EscritServiceInterface {
	
	public void addEscrit(Escrit e);
	public ArrayList<Escrit> getLlista_Escrits();
	public boolean getResultat();
	public String getError();

}
