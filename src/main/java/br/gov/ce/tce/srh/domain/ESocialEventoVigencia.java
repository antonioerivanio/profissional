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
	
	// TODO mapear coluna EVENTO
	
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
	
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}	
	
	public Boolean getVigenciaDesabilitada() {
		if(vigenciaDesabilitada == null)
			return this.getInicioValidade() != null;		
		return vigenciaDesabilitada;
	}

	public void setVigenciaDesabilitada(Boolean vigenciaDesabilitada) {
		this.vigenciaDesabilitada = vigenciaDesabilitada;
	}

	public Boolean getVigenciaEditavel() {
		if(vigenciaEditavel == null)
			return this.getInicioNovaValidade() != null;
		return vigenciaEditavel;
	}

	public void setVigenciaEditavel(Boolean vigenciaEditavel) {
		this.vigenciaEditavel = vigenciaEditavel;
	}
	
	@Transient
	public void excluirVigencia() {
		setVigenciaEditavel(false);
		this.setExcluido(true);
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
	public void apagaAlteracao() {
		this.setVigenciaEditavel(false);
		this.inicioNovaValidade = null;
		this.fimNovaValidade = null;
		this.excluido = false;
	}
	
	@Transient
	public void incluirNovamente() {
		this.setVigenciaDesabilitada(false);
		this.setVigenciaEditavel(false);
		this.setExcluido(false);
		this.setInicioValidade(null);
		this.setFimValidade(null);
	}

	
	// FIXME refazer o equals e o hashCode depois de mapear a coluna evento
	
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
		return true;
	}

}
