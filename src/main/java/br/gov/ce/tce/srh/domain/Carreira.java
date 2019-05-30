package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.SituacaoLei;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_CARREIRA", schema = "SRH")
public class Carreira extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "NUMEROLEI")
	private String lei;

	@Column(name = "DATALEI")
	private Date dataLei;

	@Column(name = "SITUACAO")
	private Integer situacao;

	@Column(name = "INICIOVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date inicioValidade;

	@Column(name = "FIMVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date fimValidade;

	@Column(name = "INICIONOVAVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date inicioNovaValidade;

	@Column(name = "FIMNOVAVALIDADE")
	@Temporal(TemporalType.DATE)
	private Date fimNovaValidade;

	@Column(name = "INICIOEXCLUSAO")
	@Temporal(TemporalType.DATE)
	private Date inicioExclusao;

	@Column(name = "FIMEXCLUSAO")
	@Temporal(TemporalType.DATE)
	private Date fimExclusao;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLei() {
		return lei;
	}

	public void setLei(String lei) {
		this.lei = lei;
	}

	public Date getDataLei() {
		return dataLei;
	}

	public void setDataLei(Date dataLei) {
		this.dataLei = dataLei;
	}

	public SituacaoLei getSituacao() {
		return SituacaoLei.toEnum(this.situacao);
	}

	public void setSituacao(SituacaoLei situacao) {
		if(situacao != null)
			this.situacao = situacao.getCodigo();
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

	public Date getInicioExclusao() {
		return inicioExclusao;
	}

	public void setInicioExclusao(Date inicioExclusao) {
		this.inicioExclusao = inicioExclusao;
	}

	public Date getFimExclusao() {
		return fimExclusao;
	}

	public void setFimExclusao(Date fimExclusao) {
		this.fimExclusao = fimExclusao;
	}

}