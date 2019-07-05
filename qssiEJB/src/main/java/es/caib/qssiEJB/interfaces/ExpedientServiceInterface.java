package es.caib.qssiEJB.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Local;

import es.caib.qssiEJB.entity.Expedient;

/**
 * Interfície del servei (EJB) ExpedientService - És el core de l'aplicació
 * @author [u97091] Toni Juanico Soler
 * data: 18/09/2018
 */


@Local
public interface ExpedientServiceInterface {
	
	// Tipus de cerca
	public static enum TipusCerca {PENDENTS_CENTRE, PENDENTS_ESTAT, REBUTJADES, FINALITZADES, SENSE_RESPOSTA, TOTS};
	
	
	// Estats en que pot estar un expedient
	public static enum EstatExpedient {
		NOU(0), 
		EQUIP_FILTRATGE(1), 
		RESPONSABLE_CONSELLERIA(2), 
		ASSIGNAT_TRAMITADOR(3), 
		RESPOSTA(4), 
		TANCADA(5),
		REBUTJADA(6);
		
		private int value;
		private static Map<Object, Object> map = new HashMap<>();
		
		EstatExpedient(int value)
		{
			this.value = value;
		}
		
		static {
			for (EstatExpedient ee : EstatExpedient.values()) {
				map.put(ee.value, ee);
			}
		}
		
		public static EstatExpedient valueOf(int estat)
		{
			return (EstatExpedient) map.get(estat);
		}
		
		public int getValue() { return value; }
		
		public String getTag()  
		{ 
			switch (value)
			{
				case 0: return "Nou"; 
				case 1: return "Pendent equip filtratge";  
				case 2: return "Assignat a responsable Conselleria";
				case 3: return "Assignat a tramitador (pendent resposta)";
				case 4: return "Resposta";
				case 5: return "Tancat";
				case 6: return "Rebutjada";
				default: return "";
			}
		}
	};
	
	// Accions que podem realitzar a un expedient segons el seu estat
	public static enum AccioExpedient {
		ASSIGNAR_CONSELLERIA(0), ASSIGNAR_TRAMITADOR(1), TRAMITAR_RESPOSTA(2), REBUTJAR(3);
		
		private int value;
		private static Map<Object, Object> map = new HashMap<>();
		
		AccioExpedient(int value)
		{
			this.value = value;
		}
		
		static {
			for (AccioExpedient ae : AccioExpedient.values()) {
				map.put(ae.value, ae);
			}
		}
		
		public static AccioExpedient valueOf(int accio)
		{
			return (AccioExpedient) map.get(accio);
		}
		
		public int getValue() { return value; }
		
		public String getTag()  
		{ 
			switch (value)
			{
				case 0: return "Assignar a conselleria"; 
				case 1: return "Assignar a unitat orgànica i tramitador";  
				case 2: return "Tramitar resposta";
				case 3: return "Rebutjar";
				default: return "";
			}
		}
	};
	
	public void addExpedient(Expedient e);
	public Expedient getExpedient(Integer id_expedient);
	public AccioExpedient[] getAccionsDisponiblesExpedient (EstatExpedient e);
	public ArrayList<Expedient> getLlista_Expedients(TipusCerca tc);
	public ArrayList<Expedient> getLlista_Expedients_assignats_usuari(String usuari);
	public boolean getResultat();
	public String getError();
}
