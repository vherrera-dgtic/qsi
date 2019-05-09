package es.caib.qssiWeb.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.log4j.Logger;

import es.caib.qssiEJB.entity.Centre;
import es.caib.qssiEJB.entity.Provincia;
import es.caib.qssiEJB.entity.Subcentre;
import es.caib.qssiEJB.interfaces.CentreServiceInterface;
import es.caib.qssiEJB.interfaces.ProvinciaServiceInterface;
import es.caib.qssiEJB.interfaces.SubcentreServiceInterface;


@WebService(targetNamespace="http://domini.integracio.helium.conselldemallorca.net/",name="DominiService")
public class DominiHeliumImpl implements DominiHelium {
	
	private final static Logger LOGGER = Logger.getLogger(DominiHeliumImpl.class);
	private InitialContext ic;
	
	@Override
	@Action(input="consultaDomini", output="consultaDominiResponse")
	@RequestWrapper(localName = "consultaDomini")
	@ResponseWrapper(localName ="consultaDominiResponse")
	public List<FilaResultat> consultaDomini(String id, List<ParellaCodiValor> parametres) throws DominiHeliumException {
		
		List<FilaResultat> resultat;
		// Log dels paràmetres d'entrada
		LOGGER.info("Crida a Webservice :-), amb parametre id: " +id );
		
		for (ParellaCodiValor val: parametres)
		{
			LOGGER.info("  --> parametres in: codi: " + val.codi + ", valor: " + val.valor);
		}
		// Comprovació dels paràmetres d'entrada		
		if (id != null)
		{
			switch (id)
			{
				case "obtenirConselleries" : 
				{
					resultat = getConselleries();
					break;
				}
				case "obtenirUnitatsOrganiques" :
				{
					if (parametres == null)
						throw new DominiHeliumException("Per l'operació obtenirUnitatsOrganiques es necessari passar al manco un element a la llista paràmetres indicant el DIR3 de la Conselleria. "
								                      + "Per exemple: [codi,valor]=[dir3,A04013523]");
					else
					{
						String dir3conselleria = new String("");
						for (ParellaCodiValor val: parametres)
						{
							if (val.codi.toLowerCase().compareTo("dir3")==0)
							{
								dir3conselleria = (String) val.valor;
							}
								
						}
						resultat = getUnitatsOrganiques(dir3conselleria);
					}
					break;
				}
				case "obtenirProvincies" :
				{
					resultat = getProvincies();
					break;
				}
				default:
				{
					throw new DominiHeliumException("El paràmetre id només pot tenir un dels següents valors posibles: obtenirConselleries (sense paràmetres)| obtenirUnitatsOrganiques amb paràmetres. Exemple: [<codi>,<valor>]=[<dir3,A04013523>]");		
				}
			}
			return resultat;
		}
		else
		{
			throw new DominiHeliumException("El paràmetre id no pot ser null. Valors posibles: obtenirConselleries | obtenirDireccionsGenerals | obtenirProvincies | obtenirMunicipis");
		}
	}
	
	private List<FilaResultat> getProvincies() throws DominiHeliumException
	{
		List<FilaResultat> resultat = new ArrayList<FilaResultat>();
		
		ArrayList<Provincia> llista_provincies;
		ProvinciaServiceInterface ProvinciaServ;
		
		LOGGER.info("Obtenir provincies ");
		
		try
		{
			ic = new InitialContext();
			ProvinciaServ = (ProvinciaServiceInterface) ic.lookup("es.caib.qssiEJB.service.ProvinciaService");
			LOGGER.info("EJB lookup " + ProvinciaServ);
			
			llista_provincies = ProvinciaServ.getLlista_Provincies(); // Cridem l'EJB
			if (!ProvinciaServ.getResultat() )
			{
				LOGGER.info("error: obtingut: " + ProvinciaServ.getError());
			}
			else
			{
				for (Provincia p: llista_provincies) {
					FilaResultat f1 = new FilaResultat();
					
					ParellaCodiValor c1 = new ParellaCodiValor("codi",p.getId());
					ParellaCodiValor c2 = new ParellaCodiValor("valor",p.getNom());
					
					f1.columnes.add(c1);
					f1.columnes.add(c2);
					
					resultat.add(f1);	
				}
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			throw new DominiHeliumException(e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			throw new DominiHeliumException(e.toString());
		}
		LOGGER.info("Resultat ofert getProvincies: " + resultat.size());
		return resultat;
		
	}
	private List<FilaResultat> getConselleries() throws DominiHeliumException
	{
		List<FilaResultat> resultat = new ArrayList<FilaResultat>();
		ArrayList<Centre> llista_centres;
		CentreServiceInterface CentreServ;
				
		LOGGER.info("Obtenim llista de centres gestors ");
		
		try
		{
			ic = new InitialContext();
			CentreServ = (CentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.CentreService");	
			LOGGER.info("EJB lookup "+ CentreServ);	
			
			llista_centres = CentreServ.getLlista_CentresActiusWeb(); // Cridem l'EJB
			
			if (!CentreServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ CentreServ.getError());
			}
			else
			{
				for (Centre c: llista_centres) {
					
					FilaResultat f1 = new FilaResultat();
					
					ParellaCodiValor c1 = new ParellaCodiValor("codi",c.getDir3());
					ParellaCodiValor c2 = new ParellaCodiValor("valor",c.getNom());
					
					f1.columnes.add(c1);
					f1.columnes.add(c2);
					
					resultat.add(f1);	
				}
				
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			throw new DominiHeliumException(e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			throw new DominiHeliumException(e.toString());
		}
		LOGGER.info("Resultat ofert getConselleries: " + resultat.size());
		return resultat;
	}
	
	private List<FilaResultat> getUnitatsOrganiques(String dir3conselleria) throws DominiHeliumException
	{
		List<FilaResultat> resultat = new ArrayList<FilaResultat>();
		ArrayList<Subcentre> llista_unitatsOrganiques;
		SubcentreServiceInterface SubcentreServ;
				
		LOGGER.info("Obtenim llista d'unitats organiques ");
		
		try
		{
			ic = new InitialContext();
			SubcentreServ = (SubcentreServiceInterface) ic.lookup("es.caib.qssiEJB.service.SubcentreService");	
			LOGGER.info("EJB lookup "+ SubcentreServ);	
			
			llista_unitatsOrganiques = SubcentreServ.getLlista_SubcentresActiusWebperDir3(dir3conselleria); // Cridem l'EJB
			
			if (!SubcentreServ.getResultat())
			{
				LOGGER.info("error obtingut: "+ SubcentreServ.getError());
			}
			else
			{
				for (Subcentre c: llista_unitatsOrganiques) {
					
					FilaResultat f1 = new FilaResultat();
					ParellaCodiValor c1 = new ParellaCodiValor("codi",c.getDir3());
					ParellaCodiValor c2 = new ParellaCodiValor("valor",c.getNom());
					
					f1.columnes.add(c1);
					f1.columnes.add(c2);
				
					resultat.add(f1);
				}
				
			}
		}
		catch (NamingException e) {
			LOGGER.info("Error___ "+e.toString());
			throw new DominiHeliumException(e.toString());
		} catch (Exception e) {
			LOGGER.info("Error_+ " + e.toString());
			throw new DominiHeliumException(e.toString());
		}
		LOGGER.info("Resultat ofert getUnitatsOrganiques: " + resultat.size());
		return resultat;
	}
}
