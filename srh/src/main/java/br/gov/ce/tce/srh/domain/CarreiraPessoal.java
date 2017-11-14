package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.EnumCarreira;

@SuppressWarnings("serial")
@Entity
@Table(name="TB_CARREIRAPESSOAL", schema="SRH")
public class CarreiraPessoal extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPESSOAL")	
	private Pessoal pessoal;
	
	@Column(name = "IDCARREIRA")
    @Enumerated(EnumType.ORDINAL)
    private EnumCarreira carreira;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DTINICIOCARREIRA")
	private Date inicioCarreira;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DTFINALCARREIRA")
	private Date fimCarreira;	
		
	@ManyToOne
	@JoinColumn(name = "IDOCUPACAO")	
	private Ocupacao ocupacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DTINICIOCARGOATUAL")
	private Date inicioCargoAtual;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DTFINALCARGOATUAL")
	private Date fimCargoAtual;
	
	@Override
	public Long getId() {return this.id;}
	@Override
	public void setId(Long id) {this.id = id;}
	
	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}
	
	public EnumCarreira getCarreira() {return carreira;}
	public void setCarreira(EnumCarreira carreira) {this.carreira = carreira;}
	
	public Date getInicioCarreira() {return inicioCarreira;}
	public void setInicioCarreira(Date inicioCarreira) {this.inicioCarreira = inicioCarreira;}
	
	public Date getFimCarreira() {return fimCarreira;}
	public void setFimCarreira(Date fimCarreira) {this.fimCarreira = fimCarreira;}
	
	public Ocupacao getOcupacao() {return ocupacao;}
	public void setOcupacao(Ocupacao ocupacao) {this.ocupacao = ocupacao;}
	
	public Date getInicioCargoAtual() {return inicioCargoAtual;}
	public void setInicioCargoAtual(Date inicioCargoAtual) {this.inicioCargoAtual = inicioCargoAtual;}
	
	public Date getFimCargoAtual() {return fimCargoAtual;}
	public void setFimCargoAtual(Date fimCargoAtual) {this.fimCargoAtual = fimCargoAtual;}
		
}
