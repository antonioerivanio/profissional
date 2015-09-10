package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_CATEGFUNCSETORRESP", schema="SRH")
public class CategoriaFuncionalSetorResponsabilidade extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;
			
	@ManyToOne
	@JoinColumn(name = "IDCATEGFUNCSETOR")
	private CategoriaFuncionalSetor categoriaFuncionalSetor;
	
	@Column(name="DESCRICAO")
	private String descricao;	
				
	@Column(name = "INICIO")
	private Date inicio;

	@Column(name = "FIM")
	private Date fim;
	
	@Column(name="TIPO")
	private Long tipo;

	@Override
	public Long getId() {return this.id;}	
	@Override
	public void setId(Long id) {this.id = id;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}
	
	public CategoriaFuncionalSetor getCategoriaFuncionalSetor() {return categoriaFuncionalSetor;}
	public void setCategoriaFuncionalSetor(CategoriaFuncionalSetor categoriaFuncionalSetor) {this.categoriaFuncionalSetor = categoriaFuncionalSetor;}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}
	
	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}
	

	
}
