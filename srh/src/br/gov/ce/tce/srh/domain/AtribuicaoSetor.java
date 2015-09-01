package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_ATRIBUICAOSETOR", schema="SRH")
public class AtribuicaoSetor extends BasicEntity<Long> implements Serializable{

	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="TIPO")
	private Long tipo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDSETOR")
	private Setor setor;
	
	@Column(name="DESCRICAO")
	private String descricao;	
	
	
	
	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

}
