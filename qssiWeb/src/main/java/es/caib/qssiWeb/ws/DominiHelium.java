package es.caib.qssiWeb.ws;


import java.util.List;

import es.caib.qssiWeb.ws.ParellaCodiValor;

public interface DominiHelium {
	public List<FilaResultat> consultaDomini(String id, List<ParellaCodiValor> parametres) throws DominiHeliumException;
}