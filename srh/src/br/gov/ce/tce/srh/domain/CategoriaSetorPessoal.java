package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.NotAudited;

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_CATEGORIASETORPESSOAL", schema = "SRH")
public class CategoriaSetorPessoal extends BasicEntity<Long> implements
		Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDCATEGFUNCSETOR")
	private CategoriaFuncionalSetor categoriaFuncionalSetor;

	@NotAudited
	@ManyToOne
	@JoinColumn(name = "IDPESSOAL")
	private Pessoal pessoal;

	@Column(name = "INICIO")
	private Date inicio;

	@Column(name = "FIM")
	private Date fim;

	@Column(name = "ATIVA")
	private Long ativa;

	@Column(name = "OBS")
	private String observacao;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public CategoriaFuncionalSetor getCategoriaFuncionalSetor() {
		return categoriaFuncionalSetor;
	}

	public void setCategoriaFuncionalSetor(
			CategoriaFuncionalSetor categoriaFuncionalSetor) {
		this.categoriaFuncionalSetor = categoriaFuncionalSetor;
	}

	public Pessoal getPessoal() {
		return pessoal;
	}

	public void setPessoal(Pessoal pessoal) {
		this.pessoal = pessoal;
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
