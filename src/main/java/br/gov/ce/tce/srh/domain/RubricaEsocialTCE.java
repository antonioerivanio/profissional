package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.enums.RubricaIncidenciaCP;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaFGTS;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaIRRF;
import br.gov.ce.tce.srh.enums.RubricaIncidenciaSIND;
import br.gov.ce.tce.srh.enums.TipoRubrica;

@Entity
@SuppressWarnings("serial")
@Table(name="ESOCIAL_RUBRICACONFIG", schema="SRH")
public class RubricaEsocialTCE extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGORUBRICA")
	private String codigo;

	@ManyToOne
	@JoinColumn(name="IDTABELARUBRICA")
	private RubricaESocialTabela tabela;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
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
	
	@Column(name="CODIGOSINDICATO")
	private String codigoSindicato;	
	
	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia;	

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		this.tipo = tipo.getCodigo();
	}

	public RubricaIncidenciaCP getCodigoPrevid() {
		return RubricaIncidenciaCP.getByCodigo(this.codigoPrevid);
	}

	public void setCodigoPrevid(RubricaIncidenciaCP codigoPrevid) {
		this.codigoPrevid = codigoPrevid.getCodigo();
	}

	public RubricaIncidenciaIRRF getCodigoIrrf() {
		return RubricaIncidenciaIRRF.getByCodigo(this.codigoIrrf);
	}

	public void setCodigoIrrf(RubricaIncidenciaIRRF codigoIrrf) {
		this.codigoIrrf = codigoIrrf.getCodigo();
	}

	public RubricaIncidenciaFGTS getCodigoFgts() {
		return RubricaIncidenciaFGTS.getByCodigo(this.codigoFgts);
	}

	public void setCodigoFgts(RubricaIncidenciaFGTS codigoFgts) {
		this.codigoFgts = codigoFgts.getCodigo();
	}

	public RubricaIncidenciaSIND getCodigoSindicato() {
		return RubricaIncidenciaSIND.getByCodigo(codigoSindicato);
	}

	public void setCodigoSindicato(RubricaIncidenciaSIND codigoSindicato) {
		this.codigoSindicato = codigoSindicato.getCodigo();
	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}
	
	
	
	
	
	

}
