package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import es.caib.qssiEJB.entity.Centre;

/**
 * Interfície del servei (EJB) CentreService
 * @author [u97091] Toni Juanico Soler
 * data: 06/09/2018
 */

public interface CentreServiceInterface {
	public void addCentre(Centre e);
	public void removeCentre(Integer id_centre);
	public Centre getCentre(Integer id_centre);
	public ArrayList<Centre> getLlista_CentresActiusWeb();
	public ArrayList<Centre> getLlista_Centres();
	public boolean getResultat();
	public String getError();
}
