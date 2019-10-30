package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import br.gov.ce.tce.srh.enums.TipoInscricao;
import br.gov.ce.tce.srh.enums.TipoLotacaoTributaria;

@Entity
@SuppressWarnings("serial")
@Table(name="ESOCIAL_TRIBUTARIO", schema="SRH")
public class LotacaoTributaria extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Size(max = 30)
	@Column(name="CODIGOLOTACAO")
	private String codigo;
	
	@Column(name = "TIPOLOTACAO")
	private String tipoLotacao;
	
	@Column(name = "TIPOINSCRICAOLOTACAO")
	private Integer tipoInscricao;

	@Column(name = "NUMEROINSCRICAO")
	private String numeroInscricao;
	
	@Column(name = "FPASLOTACAO")
	private Integer fpasLotacao;
	
	@Column(name = "CODIGOTERCEIROS")
	private String codigoTerceiros;
	
	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public TipoLotacaoTributaria getTipoLotacao() {
		return TipoLotacaoTributaria.getByCodigo(tipoLotacao);
	}

	public void setTipoLotacao(TipoLotacaoTributaria tipoLotacao) {
		if (tipoLotacao != null)
			this.tipoLotacao = tipoLotacao.getCodigo();
	}

	public TipoInscricao getTipoInscricao() {
		return TipoInscricao.getByCodigo(tipoInscricao);
	}

	public void setTipoInscricao(TipoInscricao tipoInscricao) {
		if(tipoInscricao != null)
			this.tipoInscricao = tipoInscricao.getCodigo();
	}

	public String getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public Integer getFpasLotacao() {
		return fpasLotacao;
	}

	public void setFpasLotacao(Integer fpasLotacao) {
		this.fpasLotacao = fpasLotacao;
	}

	public String getCodigoTerceiros() {
		return codigoTerceiros;
	}

	public void setCodigoTerceiros(String codigoTerceiros) {
		this.codigoTerceiros = codigoTerceiros;
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

}
