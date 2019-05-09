package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Provincia;

/**
 * Interfície del servei (EJB) ProvinciaService
 * @author [u97091] Toni Juanico Soler
 * data: 28/01/2019
 */

@Local
public interface ProvinciaServiceInterface {
	public void addProvincia(Provincia p);
	public ArrayList<Provincia> getLlista_Provincies();
	public boolean getResultat();
	public String getError();
}
