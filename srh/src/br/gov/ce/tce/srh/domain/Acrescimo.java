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
 * Referente a tabela: TB_ACRECIMO
 * 
 * @since   : Apr 17, 2011, 17:15:11 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ACRESCIMO", schema="SRH")
public class Acrescimo extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @Column(name = "DESCRICAO", length=200)
    private String descricao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FIM")
    private Date fim;

    @Column(name = "QTDEDIAS", nullable=false)
    private Long qtdeDias;


    public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public Long getQtdeDias() {return qtdeDias;}
	public void setQtdeDias(Long qtdeDias) {this.qtdeDias = qtdeDias;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}