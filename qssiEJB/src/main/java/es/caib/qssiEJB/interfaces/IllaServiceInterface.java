package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Illa;

/**
 * Interfície del servei (EJB) IllaService
 * @author [u97091] Toni Juanico Soler
 * data: 11/09/2018
 */

@Local
public interface IllaServiceInterface {
	public void addIlla(Illa i);
	public ArrayList<Illa> getLlista_Illes();
	public boolean getResultat();
	public String getError();
}
