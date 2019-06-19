package es.caib.qssiEJB.interfaces;

import javax.ejb.Local;

@Local
public interface ControladorInterface {
	public String sayHello(String name);	
}
