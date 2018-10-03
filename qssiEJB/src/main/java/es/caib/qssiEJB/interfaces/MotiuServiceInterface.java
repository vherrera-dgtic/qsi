package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Motiu;

/**
 * Interfície del servei (EJB) MotiuService
 * @author [u97091] Toni Juanico Soler
 * data: 04/09/2018
 */

@Local
public interface MotiuServiceInterface {
	
	public void addMotiu(Motiu m);
	public ArrayList<Motiu> getLlista_Motius();
	public boolean getResultat();
	public String getError();
	
}
