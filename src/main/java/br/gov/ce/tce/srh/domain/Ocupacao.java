package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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
		return TipoAcumuloDeCargo.toEnum(this.tipoAcumulacao);
	}

	public void setTipoAcumulacao(TipoAcumuloDeCargo tipoAcumulacao) {
		if(tipoAcumulacao != null)
			this.tipoAcumulacao = tipoAcumulacao.getCodigo();
	}

	public ContagemEspecial getTempoEspecial() {
		return ContagemEspecial.toEnum(this.tempoEspecial);
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
		return SituacaoLei.toEnum(this.situacaoLei);
	}

	public void setSituacaoLei(SituacaoLei situacao) {
		if (situacao != null)
			this.situacaoLei = situacao.getCodigo();
	}	

}