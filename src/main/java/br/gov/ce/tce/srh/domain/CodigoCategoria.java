package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@SuppressWarnings("serial")
@Table(name="ESOCIAL_CODIGOCATEGORIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class CodigoCategoria extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO")
	private Long codigo;
	
	@Column(name="DESCRICAO")
	private String descricao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		// TODO Auto-generated method stub
		
	}
	
	public CodigoCategoria getCodigoCategoraByList(List<CodigoCategoria> codigoCategoriaList) {
		int index = codigoCategoriaList.indexOf(this);
		CodigoCategoria codigoCategoriaEncontrado = codigoCategoriaList.get(index);
		
		return codigoCategoriaEncontrado;
	}

}