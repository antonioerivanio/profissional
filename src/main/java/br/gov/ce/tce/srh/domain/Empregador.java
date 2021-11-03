package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;

import br.gov.ce.tce.srh.enums.ClassificacaoTributaria;
//import br.gov.ce.tce.srh.enums.NaturezaJuridica;
import br.gov.ce.tce.srh.enums.SimNao;
//import br.gov.ce.tce.srh.enums.SituacaoPj;
//import br.gov.ce.tce.srh.enums.Subteto;
import br.gov.ce.tce.srh.enums.TipoInscricao;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_EMPREGADOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class Empregador extends BasicEntity<Long> implements Serializable{

	@Id
	@Column(name = "ID")	
	private Long id;

	@Column(name = "TIPOINSCRICAO")
	private Integer tipoInscricao;

	@Column(name = "CNPJ")
	private String cnpj;
		
	@Column(name = "NOMEORGAO")
	private String nomeOrgao;
	
	@Column(name = "CLASSIFICACAOTRIBUTARIA")
	private Integer classificacaoTributaria;	
	
//	@Column(name = "NATUREZAJURIDICA")
//	private String naturezaJuridica;	
	
	@Column(name = "COOPERATIVA")
	@Enumerated(EnumType.ORDINAL)
	private SimNao cooperativa;
	
	@Column(name = "CONSTRUTORA")
	@Enumerated(EnumType.ORDINAL)
	private SimNao construtora;
	
	@Column(name = "DESONERACAOFOLHA")
	@Enumerated(EnumType.ORDINAL)
	private SimNao desoneracaoFolha;
	
	@Column(name = "REGISTROEMPREGADOS")
	@Enumerated(EnumType.ORDINAL)
	private SimNao registroEmpregados;	
		
	@Column(name = "ENTIDADEEDUCATIVA")
	@Enumerated(EnumType.STRING)
	private SimNao entidadeEducativa;
	
	@Column(name = "TRABALHOTEMPORARIO")
	@Enumerated(EnumType.STRING)
	private SimNao trabalhoTemporario;
	
//	@OneToOne
//	@JoinColumn(name="IDCONTATO")
//	private Pessoal contato;	
	
//	@Column(name = "ENTEFEDERATIVO")
//	@Enumerated(EnumType.STRING)
//	private SimNao enteFederativo;
	
	@Column(name = "ENTEFEDERATIVOCNPJ")	
	private String enteFederativoCnpj;
	
//	@Column(name = "ENTEFEDERATIVONOME")
//	private String enteFederativoNome;
//	
//	@Column(name = "ENTEFEDERATIVOUF")	
//	private String enteFederativoUf;	
	
//	@Column(name = "ENTEFEDERATIVORPPS")
//	@Enumerated(EnumType.STRING)
//	private SimNao enteFederativoRpps;
	
//	@Column(name = "SUBTETO")
//	private Integer subteto;
//	
//	@NotNull(message = "Valor do subteto obrigatório!")	
//	@Column(name = "VALORSUBTETO",precision = 14, scale = 2)
//	private Double valorSubteto;
//	
//	@Column(name = "SITUACAO")	
//	private Integer situacaoPj;	
	
	@OneToOne
	@JoinColumn(name = "IDESOCIALVIGENCIA")
	private ESocialEventoVigencia esocialVigencia = new ESocialEventoVigencia();

	public Long getId() {
		return id;
	}

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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeOrgao() {
		return nomeOrgao;
	}

	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}

	
	public ClassificacaoTributaria getClassificacaoTributaria() {
		return ClassificacaoTributaria.getByCodigo(this.classificacaoTributaria);
	}

	public void setClassificacaoTributaria(ClassificacaoTributaria classificacaoTributaria) {
		if(classificacaoTributaria != null)
			this.classificacaoTributaria = classificacaoTributaria.getCodigo();
	}

//	public NaturezaJuridica getNaturezaJuridica() {
//		return NaturezaJuridica.getByCodigo(this.naturezaJuridica);
//	}
//
//	public void setNaturezaJuridica(NaturezaJuridica naturezaJuridica) {
//		if(naturezaJuridica != null)
//			this.naturezaJuridica = naturezaJuridica.getCodigo();
//	}	

	public SimNao getCooperativa() {
		return cooperativa;
	}

	public void setCooperativa(SimNao cooperativa) {
		this.cooperativa = cooperativa;
	}

	public SimNao getConstrutora() {
		return construtora;
	}

	public void setConstrutora(SimNao construtora) {
		this.construtora = construtora;
	}

	public SimNao getDesoneracaoFolha() {
		return desoneracaoFolha;
	}

	public void setDesoneracaoFolha(SimNao desoneracaoFolha) {
		this.desoneracaoFolha = desoneracaoFolha;
	}

	public SimNao getRegistroEmpregados() {
		return registroEmpregados;
	}

	public void setRegistroEmpregados(SimNao registroEmpregados) {
		this.registroEmpregados = registroEmpregados;
	}

	public SimNao getEntidadeEducativa() {
		return entidadeEducativa;
	}

	public void setEntidadeEducativa(SimNao entidadeEducativa) {
		this.entidadeEducativa = entidadeEducativa;
	}

	public SimNao getTrabalhoTemporario() {
		return trabalhoTemporario;
	}

	public void setTrabalhoTemporario(SimNao trabalhoTemporario) {
		this.trabalhoTemporario = trabalhoTemporario;
	}

//	public Pessoal getContato() {
//		return contato;
//	}
//
//	public void setContato(Pessoal contato) {
//		this.contato = contato;
//	}

//	public SimNao getEnteFederativo() {
//		return enteFederativo;
//	}
//
//	public void setEnteFederativo(SimNao enteFederativo) {
//		this.enteFederativo = enteFederativo;
//	}

	public String getEnteFederativoCnpj() {
		return enteFederativoCnpj;
	}

	public void setEnteFederativoCnpj(String enteFederativoCnpj) {
		this.enteFederativoCnpj = enteFederativoCnpj;
	}

//	public String getEnteFederativoNome() {
//		return enteFederativoNome;
//	}
//
//	public void setEnteFederativoNome(String enteFederativoNome) {
//		this.enteFederativoNome = enteFederativoNome;
//	}
//
//	public String getEnteFederativoUf() {
//		return enteFederativoUf;
//	}
//
//	public void setEnteFederativoUf(String enteFederativoUf) {
//		this.enteFederativoUf = enteFederativoUf;
//	}	
//
//	public SimNao getEnteFederativoRpps() {
//		return enteFederativoRpps;
//	}
//
//	public void setEnteFederativoRpps(SimNao enteFederativoRpps) {
//		this.enteFederativoRpps = enteFederativoRpps;
//	}
//
//	public Subteto getSubteto() {
//		return Subteto.getByCodigo(this.subteto);
//	}
//
//	public void setSubteto(Subteto subteto) {
//		if(subteto != null)
//			this.subteto = subteto.getCodigo();
//	}
//
//	public Double getValorSubteto() {
//		return valorSubteto;
//	}
//
//	public void setValorSubteto(Double valorSubteto) {
//		this.valorSubteto = valorSubteto;
//	}
//
//	public SituacaoPj getSituacaoPj() {
//		return SituacaoPj.getByCodigo(this.situacaoPj) ;
//	}
//
//	public void setSituacaoPj(SituacaoPj situacaoPj) {
//		if(situacaoPj != null)
//			this.situacaoPj = situacaoPj.getCodigo();
//	}

	public ESocialEventoVigencia getEsocialVigencia() {
		return esocialVigencia;
	}

	public void setEsocialVigencia(ESocialEventoVigencia esocialVigencia) {
		this.esocialVigencia = esocialVigencia;
	}

	public String getReferenciaESocial() {
		return this.cnpj;
	}
}
