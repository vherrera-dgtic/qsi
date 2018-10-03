package es.caib.qssiEJB.service;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import es.caib.qssiEJB.entity.Escrit;
import es.caib.qssiEJB.interfaces.TipusEscritInterface;

@Stateless
@LocalBean
@RolesAllowed({"tothom", "PB_ADMIN"})
public class TipusEscritService implements TipusEscritInterface
{
	private ArrayList<Escrit> llista_tipusEscrit;
	
	public ArrayList<Escrit> getLlista_TipusEscrit(){
		// Establim TipusEscrit
		Escrit e1 = new Escrit(1,"Queixa",new Date(),"u97091",true);
		Escrit e2 = new Escrit(2,"Suggeriment", new Date(), "u97091", true);
		Escrit e3 = new Escrit(3, "Sol·licitud d'informació", new Date(), "u97091", true);
		Escrit e4 = new Escrit(4, "Agraïment", new Date(), "u97091", true);
				
		this.llista_tipusEscrit = new ArrayList<Escrit>();
				
		llista_tipusEscrit.add(e1);
		llista_tipusEscrit.add(e2);
		llista_tipusEscrit.add(e3);
		llista_tipusEscrit.add(e4);
		
		return this.llista_tipusEscrit;
	}
}
