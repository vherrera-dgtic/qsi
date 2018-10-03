package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Idioma;



/**
 * Interfície del servei (EJB) IdiomaService
 * @author [u97091] Toni Juanico Soler
 * data: 11/09/2018
 */

@Local
public interface IdiomaServiceInterface {
	
	public void addIdioma(Idioma e);
	public ArrayList<Idioma> getLlista_Idiomes();
	public boolean getResultat();
	public String getError();
	
}
