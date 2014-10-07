package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_ESCOLARIDADE
 *
 * @since   : Ago 31, 2011, 13:19:12 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ESCOLARIDADE", schema="SRH")
public class Escolaridade extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", nullable=false)
	private String descricao;

	@Column(name="ORDEM", nullable=false)
	private Long ordem;

	@Column(name="OBSERVACAO")
	private String observacao;

	@Column(name="CODIGORAIS", nullable=false)
	private Long codigoRais;

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Long getOrdem() {return ordem;}
	public void setOrdem(Long ordem) {this.ordem = ordem;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	public Long getCodigoRais() {return codigoRais;}
	public void setCodigoRais(Long codigoRais) {this.codigoRais = codigoRais;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
