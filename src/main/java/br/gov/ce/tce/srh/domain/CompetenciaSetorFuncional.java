package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_COMPETENCIAOSETORFUNCIONAL
 * 
 * @since : Dez 19, 2012, 10:55:10 AM
 * @author : raphael.ferreira@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "TB_COMPETENCIAFUNCIONALSETOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class CompetenciaSetorFuncional extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

    @ManyToOne
    @JoinColumn(name = "IDCATEGFUNCSETOR", nullable = false)
	private CategoriaFuncionalSetor categoria;

    @ManyToOne
    @JoinColumn(name = "IDCOMPETENCIA", nullable = false)
	private Competencia competencia;

	@Column(name = "INICIO")
	private Date inicio;

	@Column(name = "FIM")
	private Date fim;

	@Column(name="ATIVA", nullable=false)
	private boolean ativa;

	@Column(name = "OBS")
	private String obs;

	
	
	public CompetenciaSetorFuncional() {
		super();
		competencia = new Competencia();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
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

	public boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public CategoriaFuncionalSetor getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaFuncionalSetor categoria) {
		this.categoria = categoria;
	}


}
