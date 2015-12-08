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

/**
 * Referente a tabela: TB_AVERBACAO
 * 
 * @since   : Apr 17, 2011, 19:16:02 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_AVERBACAO", schema="SRH")
public class Averbacao extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FIM")
    private Date fim;

    @Column(name = "QTDEDIAS", nullable=false)
    private Long qtdeDias;

    @Column(name = "ENTIDADE", length=200)
    private String entidade;
    
	@ManyToOne
	@JoinColumn(name="UF")	
	private Uf uf;

	@ManyToOne
	@JoinColumn(name = "IDMUNICIPIO")
	private Municipio municipio;
    
    @Column(name = "ESFERA", nullable=false)
    private Long esfera;

    @Column(name = "PREVIDENCIA", nullable=false)
    private Long previdencia;

    @Column(name = "DESCRICAO", length=200)
    private String descricao;

    @ManyToOne
	@JoinColumn(name = "SUBTIPOTEMPOSERVICO")
	private SubtipoTempoServico subtipo;

    public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public Long getQtdeDias() {return qtdeDias;}
	public void setQtdeDias(Long qtdeDias) {this.qtdeDias = qtdeDias;}

	public String getEntidade() {return entidade;}
	public void setEntidade(String entidade) {this.entidade = entidade;}

	public Uf getUf() {return uf;}
	public void setUf(Uf uf) {this.uf = uf;}

	public Municipio getMunicipio() {return municipio;}
	public void setMunicipio(Municipio municipio) {this.municipio = municipio;}

	public Long getEsfera() {return esfera;}
	public void setEsfera(Long esfera) {this.esfera = esfera;}

	public Long getPrevidencia() {return previdencia;}
	public void setPrevidencia(Long previdencia) {this.previdencia = previdencia;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	public SubtipoTempoServico getSubtipo() {return subtipo;}
	public void setSubtipo(SubtipoTempoServico subtipoTempoServico) {this.subtipo = subtipoTempoServico;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}