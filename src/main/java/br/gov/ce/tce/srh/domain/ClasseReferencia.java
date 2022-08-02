package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_CLASSEREFERENCIA
 * 
 * @since   : Sep 14, 2011, 10:14:50 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_CLASSEREFERENCIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class ClasseReferencia extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDESCOLARIDADEEXIGIDA")	
	private Escolaridade escolaridade;

	@Column(name="REFERENCIA")
	private Long referencia;
	
	@ManyToOne
	@JoinColumn(name = "IDSIMBOLO")	
	private Simbolo simbolo;	

	@Column(name="HORASEXIGIDAS")	
	private Long horasExigidas;

	@Column(name="CLASSE")
	private String classe;


	public Escolaridade getEscolaridade() {return escolaridade;}
	public void setEscolaridade(Escolaridade escolaridade) {this.escolaridade = escolaridade;}

	public Long getReferencia() {return referencia;}
	public void setReferencia(Long referencia) {this.referencia = referencia;}

	public String getClasse() {return classe;}
	public void setClasse(String classe) {this.classe = classe;}

	public Simbolo getSimbolo() {return simbolo;}
	public void setSimbolo(Simbolo simbolo) {this.simbolo = simbolo;}

	public Long getHorasExigidas() {return horasExigidas;}
	public void setHorasExigidas(Long horasExigidas) {this.horasExigidas = horasExigidas;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
