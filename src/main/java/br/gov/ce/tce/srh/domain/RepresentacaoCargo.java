package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_REPRESENTACAOCARGO", schema = "SRH")
@NamedQueries({ @NamedQuery(name = "RepresentacaoCargo.findAll", query = "SELECT r FROM RepresentacaoCargo r ORDER BY r.nomenclatura, r.simbolo ") })
public class RepresentacaoCargo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "ORDEM", nullable = false)
	private Long ordem;

	@Column(name = "SIMBOLO", length = 5)
	private String simbolo;

	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;

	@Column(name = "NOMENCLATURA", length = 60, nullable = false)
	private String nomenclatura;

	@Column(name = "OBSERVACAO")
	private String observacao;	

	@Column(name = "CODFUNCAO", length = 30)
	private String codFuncao;
	
	@Column(name = "CBO", length = 6)
	private String cbo;

	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia;
	
	
	public RepresentacaoCargo() {
		super();
		setEsocialVigencia(new ESocialEventoVigencia());
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getNomenclatura() {
		return nomenclatura;
	}

	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCbo() {
		return cbo;
	}

	public void setCbo(String cbo) {
		this.cbo = cbo;
	}

	public String getCodFuncao() {
		return codFuncao;
	}

	public void setCodFuncao(String codFuncao) {
		this.codFuncao = codFuncao;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}	

}