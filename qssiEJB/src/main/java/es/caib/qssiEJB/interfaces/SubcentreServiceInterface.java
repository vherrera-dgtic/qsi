package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import es.caib.qssiEJB.entity.Subcentre;

/**
 * Interfície del servei (EJB) CentreService
 * @author [u97091] Toni Juanico Soler
 * data: 06/09/2018
 */

public interface SubcentreServiceInterface {
	
	public void addSubcentre(Subcentre sc);
	public ArrayList<Subcentre> getLlista_SubcentresActiusWeb(Integer id_centre);
	public ArrayList<Subcentre> getLlista_Subcentres(Integer id_centre);
	public boolean getResultat();
	public String getError();
	
}
