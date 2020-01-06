package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_FOLHA
 * 
 * @since   : Sep 1, 2011, 17:36:11 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FOLHA", schema=DatabaseMetadata.SCHEMA_SRH)
@NamedQueries({
    @NamedQuery(name = "Folha.getMaxId", query = "SELECT max(f.id) FROM Folha f "),
    @NamedQuery(name = "Folha.getByCodigo", query = "SELECT f FROM Folha f WHERE upper( f.codigo ) = :codigo"),
    @NamedQuery(name = "Folha.getByDescricao", query = "SELECT f FROM Folha f WHERE upper( f.descricao ) = :descricao"),
    @NamedQuery(name = "Folha.delete", query = "DELETE FROM Folha f WHERE f.id=:id"),
    @NamedQuery(name = "Folha.findAll", query = "SELECT f FROM Folha f "),
    @NamedQuery(name = "Folha.findByDescricao", query = "SELECT f FROM Folha f WHERE upper( f.descricao ) like :descricao ORDER BY f.id ")})
public class Folha extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO")
	private String codigo;

	@Column(name="DESCRICAO")
	private String descricao;

	@Column(name="ATIVO")
	private boolean ativo;

	
	public String getCodigo() {return codigo;}
	public void setCodigo(String codigo) {this.codigo = codigo;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean getAtivo() {return ativo;}
	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
