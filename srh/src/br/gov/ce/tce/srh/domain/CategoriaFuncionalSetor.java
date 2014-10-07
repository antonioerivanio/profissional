package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.sapjava.Setor;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_CATEGORIAFUNCIONALSETOR", schema = "SRH")
public class CategoriaFuncionalSetor extends BasicEntity<Long> implements
		Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDSETOR")
	private Setor setor;

	@ManyToOne
	@JoinColumn(name = "IDCATEGORIA")
	private CategoriaFuncional categoriaFuncional;

	@Column(name = "INICIO")
	private Date inicio;

	@Column(name = "FIM")
	private Date fim;

	@Column(name = "ATIVA")
	private Long ativa;

	@Column(name = "OBS" , length = 200)
	private String observacao;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public CategoriaFuncional getCategoriaFuncional() {
		return categoriaFuncional;
	}

	public void setCategoriaFuncional(CategoriaFuncional categoriaFuncional) {
		this.categoriaFuncional = categoriaFuncional;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Long getAtiva() {
		return ativa;
	}

	public void setAtiva(Long ativa) {
		this.ativa = ativa;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
