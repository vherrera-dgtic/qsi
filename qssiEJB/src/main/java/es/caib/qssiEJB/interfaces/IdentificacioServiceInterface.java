package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Identificacio;

/**
 * Interfície del servei (EJB) IdentificacioService
 * @author [u97091] Toni Juanico Soler
 * data: 14/09/2018
 */

@Local
public interface IdentificacioServiceInterface {
	public void addIdentificacio(Identificacio i);
	public ArrayList<Identificacio> getLlista_Identificacions();
	public boolean getResultat();
	public String getError();
}
