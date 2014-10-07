package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_REPRESENTACAOCARGO
 * 
 * @since   : Ago 31, 2011, 10:53:50 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_REPRESENTACAOCARGO", schema="SRH")
@NamedQueries({
    @NamedQuery(name = "RepresentacaoCargo.findAll", query = "SELECT r FROM RepresentacaoCargo r ORDER BY r.nomenclatura, r.simbolo ")})
public class RepresentacaoCargo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable=false)
	private Long id;

	@Column(name="ORDEM", nullable=false)
	private Long ordem;

	@Column(name="SIMBOLO", length=5)
	private String simbolo;

	@Column(name="ATIVO", nullable=false)
	private boolean ativo;

	@Column(name="NOMENCLATURA", length=60, nullable=false)
	private String nomenclatura;

	@Column(name="OBSERVACAO")
	private String observacao;


	public Long getOrdem() {return ordem;}
	public void setOrdem(Long ordem) {this.ordem = ordem;}

	public String getSimbolo() {return simbolo;}
	public void setSimbolo(String simbolo) {this.simbolo = simbolo;}

	public boolean isAtivo() {return ativo;}
	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	public String getNomenclatura() {return nomenclatura;}
	public void setNomenclatura(String nomenclatura) {this.nomenclatura = nomenclatura;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}