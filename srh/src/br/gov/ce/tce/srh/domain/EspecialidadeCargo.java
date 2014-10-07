package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_ESPECIALIDADECARGO
 * 
 * @since   : Jan 05, 2012, 10:00:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ESPECIALIDADECARGO", schema="SRH")
public class EspecialidadeCargo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDOCUPACAO")
	private Ocupacao ocupacao;

	@ManyToOne
	@JoinColumn(name = "IDESPECIALIDADE")
	private Especialidade especialidade;


	public Ocupacao getOcupacao() {return ocupacao;}
	public void setOcupacao(Ocupacao ocupacao) {this.ocupacao = ocupacao;}

	public Especialidade getEspecialidade() {return especialidade;}
	public void setEspecialidade(Especialidade especialidade) {this.especialidade = especialidade;}


	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}