package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_EVENTO_VIGENCIA", schema = "SRH")
public class ESocialEventoVigencia extends BasicEntity<Long> implements Serializable{

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
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "EVENTO")
	@Enumerated(EnumType.STRING)
	private TipoEventoESocial tipoEvento;
		
	@Transient
	private Boolean vigenciaDesabilitada;
	
	@Transient
	private Boolean vigenciaEditavel;
	
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

	public ESocialEventoVigencia setInicioValidade(Date inicioValidade) {
		this.inicioValidade = inicioValidade;
		return this;
	}

	public Date getFimValidade() {
		return fimValidade;
	}

	public ESocialEventoVigencia setFimValidade(Date fimValidade) {
		this.fimValidade = fimValidade;
		return this;
	}

	public Date getInicioNovaValidade() {
		return inicioNovaValidade;
	}

	public ESocialEventoVigencia setInicioNovaValidade(Date inicioNovaValidade) {
		this.inicioNovaValidade = inicioNovaValidade;
		return this;
	}

	public Date getFimNovaValidade() {
		return fimNovaValidade;
	}

	public ESocialEventoVigencia setFimNovaValidade(Date fimNovaValidade) {
		this.fimNovaValidade = fimNovaValidade;
		return this;
	}

	public boolean isExcluido() {
		return excluido;
	}

	public ESocialEventoVigencia setExcluido(boolean excluido) {
		this.excluido = excluido;
		return this;
	}

	public boolean isTransmitido() {
		return transmitido;
	}

	public ESocialEventoVigencia setTransmitido(boolean transmitido) {
		this.transmitido = transmitido;
		return this;
	}	
	
	public String getReferencia() {
		return referencia;
	}

	public ESocialEventoVigencia setReferencia(String referencia) {
		this.referencia = referencia;
		return this;
	}
	
	public TipoEventoESocial getTipoEvento() {
		return tipoEvento;
	}

	public ESocialEventoVigencia setTipoEvento(TipoEventoESocial tipoEvento) {
		this.tipoEvento = tipoEvento;
		return this;
	}

	public Boolean getVigenciaDesabilitada() {
		if(vigenciaDesabilitada == null)
			return this.getInicioValidade() != null;		
		return vigenciaDesabilitada;
	}

	public ESocialEventoVigencia setVigenciaDesabilitada(Boolean vigenciaDesabilitada) {
		this.vigenciaDesabilitada = vigenciaDesabilitada;
		return this;
	}

	public Boolean getVigenciaEditavel() {
		if(vigenciaEditavel == null)
			return this.getInicioNovaValidade() != null;
		return vigenciaEditavel;
	}

	public ESocialEventoVigencia setVigenciaEditavel(Boolean vigenciaEditavel) {
		this.vigenciaEditavel = vigenciaEditavel;
		return this;
	}
	
	@Transient
	public ESocialEventoVigencia excluirVigencia() {
		this.setVigenciaEditavel(false)
			.setExcluido(true);
		return this;
	}
	

	@Transient
	public boolean exclusaoTransmitida() {
		return this.excluido && this.transmitido;
	}
	
	
	@Transient
	public boolean podeCancelarOperacaoAnterior() {
		return this.inicioValidade != null 
				&& (this.inicioNovaValidade != null 
						|| this.fimNovaValidade != null 
						|| (this.excluido));
	}
	
	@Transient
	public ESocialEventoVigencia apagaAlteracao() {
		this.setVigenciaEditavel(false)
			.setInicioNovaValidade(null)
			.setFimNovaValidade(null)
			.setExcluido(false);
		return this;
	}
	
	@Transient
	public ESocialEventoVigencia incluirNovamente() {
		this.setVigenciaDesabilitada(false)
			.setVigenciaEditavel(false)
			.setExcluido(false)
			.setInicioValidade(null)
			.setFimValidade(null);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (excluido ? 1231 : 1237);
		result = prime * result + ((fimNovaValidade == null) ? 0 : fimNovaValidade.hashCode());
		result = prime * result + ((fimValidade == null) ? 0 : fimValidade.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inicioNovaValidade == null) ? 0 : inicioNovaValidade.hashCode());
		result = prime * result + ((inicioValidade == null) ? 0 : inicioValidade.hashCode());
		result = prime * result + ((referencia == null) ? 0 : referencia.hashCode());
		result = prime * result + ((tipoEvento == null) ? 0 : tipoEvento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ESocialEventoVigencia other = (ESocialEventoVigencia) obj;
		if (excluido != other.excluido)
			return false;
		if (fimNovaValidade == null) {
			if (other.fimNovaValidade != null)
				return false;
		} else if (!fimNovaValidade.equals(other.fimNovaValidade))
			return false;
		if (fimValidade == null) {
			if (other.fimValidade != null)
				return false;
		} else if (!fimValidade.equals(other.fimValidade))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inicioNovaValidade == null) {
			if (other.inicioNovaValidade != null)
				return false;
		} else if (!inicioNovaValidade.equals(other.inicioNovaValidade))
			return false;
		if (inicioValidade == null) {
			if (other.inicioValidade != null)
				return false;
		} else if (!inicioValidade.equals(other.inicioValidade))
			return false;
		if (referencia == null) {
			if (other.referencia != null)
				return false;
		} else if (!referencia.equals(other.referencia))
			return false;
		if (tipoEvento != other.tipoEvento)
			return false;
		return true;
	}	
	

}
