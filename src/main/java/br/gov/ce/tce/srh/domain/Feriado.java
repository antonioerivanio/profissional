package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "TB_FERIADO", schema = "SRH")
public class Feriado extends BasicEntity<Long> implements Serializable{

	@Id
	@Column(name = "ID")
	private Long id;
		
	@Column(name = "DIA")
	@Temporal(value = TemporalType.DATE)
	private Date dia;
	
	@Column(name = "TIPO")
	private Integer tipo; 

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}		
}