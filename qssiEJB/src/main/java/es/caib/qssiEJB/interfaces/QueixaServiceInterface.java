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
	public ArrayList<Queixa> getLLista_queixes();
	public boolean getResultat();
	public String getError();
}
