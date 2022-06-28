package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import br.gov.ce.tce.srh.enums.AprendizContrato;
import br.gov.ce.tce.srh.enums.PcdContrato;
import br.gov.ce.tce.srh.enums.RegistroPonto;
import br.gov.ce.tce.srh.enums.TipoInscricao;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_ESTABELECIMENTO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Estabelecimento extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "TIPOINSCRICAO")
	private Integer tipoInscricao;

	@Column(name = "NRINSCRICAO")
	private String numeroInscricao;	

	@Column(name = "CNAE")
	private Integer cnae;
	
	@Column(name = "RAT")
	private Integer rat;
	
	@Column(name = "FAP", precision = 5, scale = 4)	
	private Double fap;
	
	@Column(name = "RAT_AJUSTADO", precision = 5, scale = 4)	
	private Double ratAjustado;
	
	@Column(name = "REGISTROPONTO")
	private Integer registroPonto;
	
	@Column(name = "APRENDIZCONTRATO")
	private Integer aprendizContrato;
		
	@Column(name = "PCDCONTRATO")
	private Integer pcdContrato;
	
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

	public TipoInscricao getTipoInscricao() {
		return TipoInscricao.getByCodigo(this.tipoInscricao);
	}

	public void setTipoInscricao(TipoInscricao tipo) {
		if(tipo != null)
			this.tipoInscricao = tipo.getCodigo();
	}

	public String getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public Integer getCnae() {
		return cnae;
	}

	public void setCnae(Integer cnae) {
		this.cnae = cnae;
	}

	public Integer getRat() {
		return rat;
	}

	public void setRat(Integer rat) {
		this.rat = rat;
	}

	public Double getFap() {
		return fap;
	}

	public void setFap(Double fap) {
		this.fap = fap;
	}

	public Double getRatAjustado() {
		return ratAjustado;
	}

	public void setRatAjustado(Double ratAjustado) {
		this.ratAjustado = ratAjustado;
	}

	public RegistroPonto getRegistroPonto() {
		return RegistroPonto.getByCodigo(this.registroPonto);
	}

	public void setRegistroPonto(RegistroPonto registroPonto) {
		if (registroPonto != null)
			this.registroPonto = registroPonto.getCodigo();
	}

	public AprendizContrato getAprendizContrato() {
		return AprendizContrato.getByCodigo(this.aprendizContrato);
	}

	public void setAprendizContrato(AprendizContrato aprendizContrato) {
		if(aprendizContrato != null)
			this.aprendizContrato = aprendizContrato.getCodigo();
	}

	public PcdContrato getPcdContrato() {
		return PcdContrato.getByCodigo(this.pcdContrato);
	}

	public void setPcdContrato(PcdContrato pcdContrato) {
		if(pcdContrato != null)
			this.pcdContrato = pcdContrato.getCodigo();
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}
	
	public void atualizaAliqRatAjust() {
		this.ratAjustado = this.rat * this.fap;
	}

	public String getReferenciaESocial() {
		return this.numeroInscricao;
	}
	
}