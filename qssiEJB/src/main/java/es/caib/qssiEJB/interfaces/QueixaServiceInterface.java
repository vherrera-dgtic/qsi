package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Queixa;

/**
 * Interfície del servei (EJB) QueixaService
 * @author [u97091] Toni Juanico Soler
 * data: 05/09/2018
 */

@Local
public interface QueixaServiceInterface {
	
	public void addQueixa(Queixa q);
	public void updateQueixa(Queixa q);
	public Queixa getQueixa(Integer id_queixa);
	public ArrayList<Queixa> getLLista_queixes();
	public void removeQueixa(Integer id_queixa);
	public boolean getResultat();
	public String getError();
}
