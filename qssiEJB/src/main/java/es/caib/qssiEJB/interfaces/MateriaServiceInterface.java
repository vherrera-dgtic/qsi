package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;
import javax.ejb.Local;
import es.caib.qssiEJB.entity.Materia;

/**
 * Interfície del servei (EJB) MateriaService
 * @author [u97091] Toni Juanico Soler
 * data: 27/08/2018
 */

@Local
public interface MateriaServiceInterface {
	
	public void addMateria(Materia m);
	public ArrayList<Materia> getLlista_Materies();
	public boolean getResultat();
	public String getError();
	
		
}
