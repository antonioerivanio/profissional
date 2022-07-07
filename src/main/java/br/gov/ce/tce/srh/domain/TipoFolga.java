package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPOFOLGA", schema=DatabaseMetadata.SCHEMA_SRH)
public class TipoFolga extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESCRICAO", length=400, nullable=false)
	private String descricao;
	
	@Column(name="FLATIVO")
	private boolean ativo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
