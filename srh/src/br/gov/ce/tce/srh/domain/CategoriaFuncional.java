package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_CATEGORIAFUNCIONAL", schema = "SRH")
@NamedQueries({ @NamedQuery(name = "CategoriaFuncional.findAll", query = "SELECT c FROM CategoriaFuncional c ORDER BY c.descricao ") })
public class CategoriaFuncional extends BasicEntity<Long> implements
		Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "ATIVA")
	private Long ativa;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getAtiva() {
		return ativa;
	}

	public void setAtiva(Long ativa) {
		this.ativa = ativa;
	}
}
