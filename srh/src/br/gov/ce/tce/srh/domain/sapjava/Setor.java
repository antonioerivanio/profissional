package br.gov.ce.tce.srh.domain.sapjava;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.BasicEntity;

/**
 * Referente a tabela: SETOR
 * 
 * @author Robstown Holanda, em 01/09/2011
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="SETOR", schema="SAPJAVA")
@NamedQueries({@NamedQuery(name = "Setor.findAll", query = "SELECT s FROM Setor s WHERE s.tipo = 1 ORDER BY s.nome "), 
				@NamedQuery(name="Setor.findTodosAtivos", query="SELECT s FROM Setor s WHERE s.tipo = 1 and s.ativo = 1 order by s.nome" )})
public class Setor extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="IDSETOR")
	private Long id;

	@Column(name="NMSETOR")
	private String nome;

	@Column(name="TPSETOR")
	private Long tipo;


	@Column(name="FLATIVO")
	private Long ativo;
	
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}

	public Long getAtivo() {return ativo;}

	public void setAtivo(Long ativo) {this.ativo = ativo;}
	
}
