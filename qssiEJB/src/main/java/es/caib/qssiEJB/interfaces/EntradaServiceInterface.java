package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Entrada;

/**
 * Interfície del servei (EJB) EntradaService
 * @author [u97091] Toni Juanico Soler
 * data: 06/09/2018
 */

@Local
public interface EntradaServiceInterface {
	
	public void addEntrada(Entrada e);
	public ArrayList<Entrada> getLlista_Entrades();
	public boolean getResultat();
	public String getError();
	
}
