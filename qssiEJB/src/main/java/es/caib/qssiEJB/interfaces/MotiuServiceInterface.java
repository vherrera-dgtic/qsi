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
	public void updateMotiu(Motiu m_update);
	public Motiu getMotiu(Integer id_motiu);
	public ArrayList<Motiu> getLlista_Motius();
	public void removeMotiu(Integer id_motiu);
	public boolean getResultat();
	public String getError();
	
}
