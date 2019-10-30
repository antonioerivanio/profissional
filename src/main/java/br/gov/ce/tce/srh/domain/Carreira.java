package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

	@Column(name = "SITUACAOLEI")
	private Integer situacao;

	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();
	

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
		return SituacaoLei.getByCodigo(this.situacao);
	}

	public void setSituacao(SituacaoLei situacao) {
		if(situacao != null)
			this.situacao = situacao.getCodigo();
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}	

}