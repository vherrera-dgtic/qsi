package es.caib.qssiEJB.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entitat de mapeig de la taula QSI_FITXER - Aquesta entitat emmagatzema la referència dels fitxers a l'arxiu CAIB
 * @author [u97091] Toni Juanico Soler
 * data: 06/08/2019
 */

@Entity
@Table(name="QSI_FITXER")
public class Fitxer {
	@Id
	private Integer id_fitxer;
	
	@Column
	private Integer id_expedient;
	
	@Column
	private String id_document_arxiu_caib;
	
	@Column
	private String url_versio_imprimible_arxiu_caib;
	
}
