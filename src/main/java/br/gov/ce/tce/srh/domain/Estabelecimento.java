package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.TipoInscricao;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_ESTABELECIMENTO", schema = "SRH")
public class Estabelecimento extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "TIPOINSCRICAO")
	private Integer tipoInscricao;

	@Column(name = "NRINSCRICAO")
	private String numeroInscricao;	

	@Column(name = "INICIOVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date inicioValidade;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public TipoInscricao getTipoInscricao() {
		return TipoInscricao.toEnum(this.tipoInscricao);
	}

	public void setTipoInscricao(TipoInscricao tipo) {
		if(tipo != null)
			this.tipoInscricao = tipo.getCodigo();
	}

	public Date getInicioValidade() {
		return inicioValidade;
	}

	public void setInicioValidade(Date inicioValidade) {
		this.inicioValidade = inicioValidade;
	}

	public String getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

}