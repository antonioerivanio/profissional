package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_DIREITOLICENCAESP
 * 
 * @since   : Nov 20, 2011, 14:50:10 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_DIREITOLICENCAESP", schema="SRH")
public class LicencaEspecial extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @Column(name = "QTDEDIAS", nullable=false)
    private Long qtdedias;

    @Column(name = "ANOINICIAL", nullable=false)
    private Long anoinicial;

    @Column(name = "ANOFINAL", nullable=false)
    private Long anofinal;

    @Column(name = "DESCRICAO", nullable=false, length=150)
    private String descricao;

    @Column(name = "SALDODIAS", nullable=false)
    private Long saldodias;

    @Column(name = "CONTAREMDOBRO", nullable=false)
    private boolean contaremdobro;


	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public Long getQtdedias() {return qtdedias;}
	public void setQtdedias(Long qtdedias) {this.qtdedias = qtdedias;}

	public Long getAnoinicial() {return anoinicial;}
	public void setAnoinicial(Long anoinicial) {this.anoinicial = anoinicial;}

	public Long getAnofinal() {return anofinal;}
	public void setAnofinal(Long anofinal) {this.anofinal = anofinal;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Long getSaldodias() {return saldodias;}
	public void setSaldodias(Long saldodias) {this.saldodias = saldodias;}

	public boolean isContaremdobro() {return contaremdobro;}
	public void setContaremdobro(boolean contaremdobro) {this.contaremdobro = contaremdobro;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}