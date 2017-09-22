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

/**
 * Referente a tabela: TB_DEDUCAO
 * 
 * @since   : Apr 17, 2011, 17:07:05 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_DEDUCAO", schema="SRH")
public class Deducao extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL", nullable=false)
    private Pessoal pessoal;

    @Column(name = "NRPROCESSO", length=10)
    private String nrProcesso;

	@Column(name="FALTA", nullable=false)
	private boolean falta;
    
    @Column(name = "ANOREFERENCIA")
    private Long anoReferencia;    

    @Column(name = "MESREFERENCIA")
    private Long mesReferencia;    

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO")
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FIM")
    private Date fim;

    @Column(name = "QTDEDIAS", nullable=false)
    private Long qtdeDias;

    @Column(name = "DESCRICAO", length=200)
    private String descricao;
    
    @Transient
	private String anos;
	
	@Transient
	private String meses;
		
	@Transient
	private String dias;

    @Transient
    private String descricaoCompleta;

	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public String getNrProcesso() {return nrProcesso;}
	public void setNrProcesso(String nrProcesso) {this.nrProcesso = nrProcesso;}

	public boolean isFalta() {return falta;}
	public void setFalta(boolean falta) {this.falta = falta;}

	public Long getAnoReferencia() {return anoReferencia;}
	public void setAnoReferencia(Long anoReferencia) {this.anoReferencia = anoReferencia;}

	public Long getMesReferencia() {return mesReferencia;}
	public void setMesReferencia(Long mesReferencia) {this.mesReferencia = mesReferencia;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public Long getQtdeDias() {return qtdeDias;}
	public void setQtdeDias(Long qtdeDias) {this.qtdeDias = qtdeDias;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
	public String getDescricaoCompleta() {
		if (descricaoCompleta == null) {
			descricaoCompleta = "Ano:" + anoReferencia + " / Mês:" + mesReferencia;
		}
		return descricaoCompleta;
	}
	public void setDescricaoCompleta(String descricaoCompleta) {
		this.descricaoCompleta = descricaoCompleta;
	}
	
	public String getAnos() {return anos;}
	public void setAnos(String anos) {this.anos = anos;}

	public String getMeses() {return meses;}
	public void setMeses(String meses) {this.meses = meses;}

	public String getDias() {return dias;}
	public void setDias(String dias) {this.dias = dias;}
}