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

@Entity
@SuppressWarnings("serial")
@Table(name="TB_PESSOALRECADASTRAMENTO", schema="SRH")
public class PessoalRecadastramento extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @ManyToOne
    @JoinColumn(name = "IDRECADASTRAMENTO")
    private Recadastramento recadastramento;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATAATUALIZACAO")
    private Date dataAtualizacao = new Date();
    
    @Column(name = "IDSTATUS")
    private Integer status;
    
    
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public Pessoal getPessoal() {
		return pessoal;
	}

	public void setPessoal(Pessoal pessoal) {
		this.pessoal = pessoal;
	}

	public Recadastramento getRecadastramento() {
		return recadastramento;
	}

	public void setRecadastramento(Recadastramento recadastramento) {
		this.recadastramento = recadastramento;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}		
	
}