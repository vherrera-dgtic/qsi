package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Municipi;

/**
 * Interfície del servei (EJB) MunicipiService
 * @author [u97091] Toni Juanico Soler
 * data: 11/09/2018
 */

@Local
public interface MunicipiServiceInterface {
	public void addMunicipi(Municipi m);
	public ArrayList<Municipi> getLlista_MunicipisActius(Integer id_illa);
	public ArrayList<Municipi> getLlista_Municipis(Integer id_illa);
	public boolean getResultat();
	public String getError();
}
