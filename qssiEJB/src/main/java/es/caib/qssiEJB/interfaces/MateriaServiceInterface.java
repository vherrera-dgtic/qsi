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
	public void updateMateria(Materia m);
	public Materia getMateria(Integer id_materia); 
	public ArrayList<Materia> getLlista_Materies();
	public void removeMateria(Integer id_materia);
	public boolean getResultat();
	public String getError();
	
		
}
