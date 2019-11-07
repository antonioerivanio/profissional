package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.gov.ce.tce.srh.enums.ContagemEspecial;
import br.gov.ce.tce.srh.enums.SituacaoLei;
import br.gov.ce.tce.srh.enums.TipoAcumuloDeCargo;

/**
 * Referente a tabela: TB_OCUPACAO
 * 
 * @since : Sep 13, 2011, 15:04:56 AM
 * @author : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "TB_OCUPACAO", schema = "SRH")
public class Ocupacao extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDESCOLARIDADEEXIGIDA")
	private Escolaridade escolaridade;

	@Size(max = 100)
	@Column(name = "NOMENCLATURA")
	private String nomenclatura;

	@ManyToOne
	@JoinColumn(name = "TIPOOCUPACAO")
	private TipoOcupacao tipoOcupacao;

	@Column(name = "QUANTIDADE")
	private Long quantidade;

	@Column(name = "SITUACAO")
	private Long situacao;

	@Column(name = "REFERENCIAINICIAL")
	private Long referenciaInicial;

	@Column(name = "REFERENCIAFINAL")
	private Long referenciaFinal;

	@Column(name = "FLCARGOISOLADO", nullable = false)
	private boolean cargoIsolado;

	@Column(name = "CODIGO_ESOCIAL")
	private String codigoEsocial;

	@Column(name = "CBO")
	private String cbo;

	@Column(name = "TIPOACUMULACAO")
	private Integer tipoAcumulacao;
	
	@Column(name = "TEMPOESPECIAL")
	private Integer tempoEspecial;

	@Column(name = "DEDICACAOEXCLUSIVA")
	private Character dedicacaoExclusiva;
	
	@Column(name = "NUMEROLEI")
	private String lei;

	@Column(name = "DATALEI")
	private Date dataLei;

	@Column(name = "SITUACAOLEI")
	private Integer situacaoLei;

	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();
	
	@Transient
	private String descricaoSituacao;

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getNomenclatura() {
		return nomenclatura;
	}

	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}

	public TipoOcupacao getTipoOcupacao() {
		return tipoOcupacao;
	}

	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {
		this.tipoOcupacao = tipoOcupacao;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Long getSituacao() {
		return situacao;
	}

	public void setSituacao(Long situacao) {
		this.situacao = situacao;
	}

	public Long getReferenciaInicial() {
		return referenciaInicial;
	}

	public void setReferenciaInicial(Long referenciaInicial) {
		this.referenciaInicial = referenciaInicial;
	}

	public Long getReferenciaFinal() {
		return referenciaFinal;
	}

	public void setReferenciaFinal(Long referenciaFinal) {
		this.referenciaFinal = referenciaFinal;
	}

	public boolean isCargoIsolado() {
		return cargoIsolado;
	}

	public void setCargoIsolado(boolean cargoIsolado) {
		this.cargoIsolado = cargoIsolado;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricaoSituacao() {
		switch (situacao.intValue()) {
		case 1:
			descricaoSituacao = "Normal";
			break;
		case 2:
			descricaoSituacao = "Extinto ao Vagar";
			break;
		case 3:
			descricaoSituacao = "Extinto";
			break;
		case 4:
			descricaoSituacao = "Em Transição";
			break;
		default:
			descricaoSituacao = "Não identificado";
			break;
		}

		return descricaoSituacao;
	}

	public String getCodigoEsocial() {
		return codigoEsocial;
	}

	public void setCodigoEsocial(String codigoEsocial) {
		this.codigoEsocial = codigoEsocial;
	}

	public String getCbo() {
		return cbo;
	}

	public void setCbo(String cbo) {
		this.cbo = cbo;
	}

	public TipoAcumuloDeCargo getTipoAcumulacao() {
		return TipoAcumuloDeCargo.getByCodigo(this.tipoAcumulacao);
	}

	public void setTipoAcumulacao(TipoAcumuloDeCargo tipoAcumulacao) {
		if(tipoAcumulacao != null)
			this.tipoAcumulacao = tipoAcumulacao.getCodigo();
	}

	public ContagemEspecial getTempoEspecial() {
		return ContagemEspecial.getByCodigo(this.tempoEspecial);
	}

	public void setTempoEspecial(ContagemEspecial tempoEspecial) {
		if(tempoEspecial != null)
			this.tempoEspecial = tempoEspecial.getCodigo();
	}

	public Character getDedicacaoExclusiva() {
		return dedicacaoExclusiva;
	}

	public void setDedicacaoExclusiva(Character dedicacaoExclusiva) {
		this.dedicacaoExclusiva = dedicacaoExclusiva;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

	public void setDescricaoSituacao(String descricaoSituacao) {
		this.descricaoSituacao = descricaoSituacao;
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

	public SituacaoLei getSituacaoLei() {
		return SituacaoLei.getByCodigo(this.situacaoLei);
	}

	public void setSituacaoLei(SituacaoLei situacao) {
		if (situacao != null)
			this.situacaoLei = situacao.getCodigo();
	}	
	
	@Transient
	public String getReferenciaESocial() {
		return this.codigoEsocial;
	}

}