package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Escrit;

/**
 * Interfície del servei (EJB) TipusEscrit
 * @author [u97091] Toni Juanico Soler
 * data: 28/08/2018
 */

@Local
public interface TipusEscritInterface {
	
	public ArrayList<Escrit> getLlista_TipusEscrit();
	
}
