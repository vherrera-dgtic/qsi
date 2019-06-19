package es.caib.qssiEJB.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="QSI_SEQUENCIA_EXPEDIENT")
public class SequenciaExpedient {

	@Id
	private Integer id_sequencia;
			
	@Column
	private Integer valor;
	
	// Constructors
	public SequenciaExpedient() {	}
	public SequenciaExpedient(Integer id, Integer valor)
	{
		this.id_sequencia = id;
		this.valor = valor;
	}
		
	// Mètodes set i get
	public Integer getId() { return id_sequencia;	}
	public void setId(Integer id) {	this.id_sequencia = id;	}
		
	public Integer getValor() { return this.valor; }
	public void setValor(Integer value) { this.valor = value; }
	
	public void nextval() { this.valor = this.valor + 1; }
}
