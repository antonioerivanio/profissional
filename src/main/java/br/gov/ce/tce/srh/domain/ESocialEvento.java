package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_EVENTO", schema = "SRH")
public class ESocialEvento extends BasicEntity<Long> implements Serializable{

	@Id
	private Long id;
	
	@Column(name = "INICIOVALIDADE")
	private Date inicioValidade;
	
	@Column(name = "FIMVALIDADE")
	private Date fimValidade;
	
	@Column(name = "INICIONOVAVALIDADE")
	private Date inicioNovaValidade;
	
	@Column(name = "FIMNOVAVALIDADE")
	private Date fimNovaValidade;
	
	@Column(name = "FLEXCLUIDO")
	private boolean excluido;
	
	@Column(name = "FLTRANSMITIDO")
	private boolean transmitido;
	
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getInicioValidade() {
		return inicioValidade;
	}

	public void setInicioValidade(Date inicioValidade) {
		this.inicioValidade = inicioValidade;
	}

	public Date getFimValidade() {
		return fimValidade;
	}

	public void setFimValidade(Date fimValidade) {
		this.fimValidade = fimValidade;
	}

	public Date getInicioNovaValidade() {
		return inicioNovaValidade;
	}

	public void setInicioNovaValidade(Date inicioNovaValidade) {
		this.inicioNovaValidade = inicioNovaValidade;
	}

	public Date getFimNovaValidade() {
		return fimNovaValidade;
	}

	public void setFimNovaValidade(Date fimNovaValidade) {
		this.fimNovaValidade = fimNovaValidade;
	}

	public boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}

	public boolean isTransmitido() {
		return transmitido;
	}

	public void setTransmitido(boolean transmitido) {
		this.transmitido = transmitido;
	}	
	
	@Transient
	public boolean houveAlteracao() {
		return this.inicioValidade != null && (this.inicioNovaValidade != null || this.fimNovaValidade != null || this.excluido);
	}
	
	@Transient
	public void apagaAlteracao() {
		this.inicioNovaValidade = null;
		this.fimNovaValidade = null;
		this.excluido = false;
	}

}
