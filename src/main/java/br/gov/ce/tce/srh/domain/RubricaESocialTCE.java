package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.enums.RubricaIncidenciaCPCodigo;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaFGTS;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaIRRFCodigo;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaSIND;
import br.gov.ce.tce.srh.enums.TipoRubrica;

@Entity
@SuppressWarnings("serial")
@Table(name="ESOCIAL_RUBRICACONFIG", schema=DatabaseMetadata.SCHEMA_SRH)
public class RubricaESocialTCE extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGORUBRICA")
	private String codigo;

	@ManyToOne
	@JoinColumn(name="IDTABELARUBRICA")
	private RubricaESocialTabela tabela;	
	
	@ManyToOne
	@JoinColumn(name="IDRUBRICATCE")
	private Rubrica rubrica;
	
	@ManyToOne
	@JoinColumn(name="IDRUBRICAESOCIAL")
	private RubricaESocial rubricaESocial;	

	@Column(name="TIPO")
	private Integer tipo;
	
	@Column(name="CODIGOPREVID")
	private String codigoPrevid;
	
	@Column(name="CODIGOIRRF")
	private String codigoIrrf;
	
	@Column(name="CODIGOFGTS")
	private String codigoFgts;
	
//	@Column(name="CODIGOSINDICATO")
//	private String codigoSindicato;	
	
	@Column(name="OBSERVACAO")
	private String observacao;
	
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

	public RubricaESocialTabela getTabela() {
		return tabela;
	}

	public void setTabela(RubricaESocialTabela tabela) {
		this.tabela = tabela;
	}	

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public RubricaESocial getRubricaESocial() {
		return rubricaESocial;
	}

	public void setRubricaESocial(RubricaESocial rubricaESocial) {
		this.rubricaESocial = rubricaESocial;
	}
	
	public TipoRubrica getTipo() {
		return TipoRubrica.getByCodigo(this.tipo);
	}

	public void setTipo(TipoRubrica tipo) {
		if(tipo != null)
			this.tipo = tipo.getCodigo();
	}

	public RubricaIncidenciaCPCodigo getCodigoPrevid() {
		return RubricaIncidenciaCPCodigo.getByCodigo(this.codigoPrevid);
	}

	public void setCodigoPrevid(RubricaIncidenciaCPCodigo codigoPrevid) {
		if(codigoPrevid != null)
			this.codigoPrevid = codigoPrevid.getCodigo();
	}

	public RubricaIncidenciaIRRFCodigo getCodigoIrrf() {
		return RubricaIncidenciaIRRFCodigo.getByCodigo(this.codigoIrrf);
	}

	public void setCodigoIrrf(RubricaIncidenciaIRRFCodigo codigoIrrf) {
		if(codigoIrrf != null)
			this.codigoIrrf = codigoIrrf.getCodigo();
	}

	public RubricaIncidenciaFGTS getCodigoFgts() {
		return RubricaIncidenciaFGTS.getByCodigo(this.codigoFgts);
	}

	public void setCodigoFgts(RubricaIncidenciaFGTS codigoFgts) {
		if(codigoFgts != null)
			this.codigoFgts = codigoFgts.getCodigo();
	}

//	public RubricaIncidenciaSIND getCodigoSindicato() {
//		return RubricaIncidenciaSIND.getByCodigo(codigoSindicato);
//	}
//
//	public void setCodigoSindicato(RubricaIncidenciaSIND codigoSindicato) {
//		if(codigoSindicato != null)
//			this.codigoSindicato = codigoSindicato.getCodigo();
//	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getReferenciaESocial() {
		return this.rubrica.getCodigo() + "-" + this.tabela.getCodigo();
	}

}
