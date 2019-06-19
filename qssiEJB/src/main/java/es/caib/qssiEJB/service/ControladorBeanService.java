package es.caib.qssiEJB.service;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import es.caib.qssiEJB.interfaces.ControladorInterface;

/**
 * Servei (EJB) per fer proves d'injecció EJB, veure el Servlet ProvaEJB per més referències.
 * @author [u97091] Antoni Juanico soler
 * data 19/06/2019
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class ControladorBeanService implements ControladorInterface {
	
	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
