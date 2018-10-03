package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Expedient;

/**
 * Interfície del servei (EJB) ExpedientService - És el core de l'aplicació
 * @author [u97091] Toni Juanico Soler
 * data: 18/09/2018
 */

@Local
public interface ExpedientServiceInterface {
	public void addExpedient(Expedient e);
	public ArrayList<Expedient> getLlista_Expedients();
	public boolean getResultat();
	public String getError();
}
