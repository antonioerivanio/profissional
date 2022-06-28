package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Entity
@SuppressWarnings("serial")
public class ServidorCompetencia implements Serializable{
	
	private static final String CURSO_FORMACAO = "CURSO FORMAÇÃO";
	private static final String CURSO_PROFISSIONAL = "CURSO PROFISSIONAL";
	private static final String CURSO_ATESTO = "ATESTO";
	
	@Id
	private Long id;
	
	private Setor setor;
	
	private AreaSetorCompetencia areaSetorCompetencia;
	
	private Pessoal pessoal;
	
	private Boolean img;
	
	private CompetenciaGraduacao competenciaGraduacao;
	
	private AtestoPessoa atestoPessoa;
	
	
	// Valida as Competencias: Atesto | Curso Formação ou Curso Profissional
	private  String validaCompetencia() {
		if (competenciaGraduacao != null) {
			return CURSO_FORMACAO;
			} else if (competenciaGraduacao == null && areaSetorCompetencia.getCompetencia() != null ) {
				return CURSO_PROFISSIONAL;
				} else if (atestoPessoa.getObservacao() != null) {
					return CURSO_ATESTO;
					} else {
						return "";
					}
			
			
	}
	
	public String getValidaCompetencia() {
		return validaCompetencia();
	}
	
	public AtestoPessoa getAtestoPessoa() {
		return atestoPessoa;
	}

	public void setAtestoPessoa(AtestoPessoa atestoPessoa) {
		this.atestoPessoa = atestoPessoa;
	}

	

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}
	
	public AreaSetorCompetencia getAreaSetorCompetencia() {return areaSetorCompetencia;}
	public void setAreaSetorCompetencia(AreaSetorCompetencia areaSetorCompetencia) {this.areaSetorCompetencia = areaSetorCompetencia;}

	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}
	
	public Boolean getImg() {return img;}
	public void setImg(Boolean img) {this.img = img;}
	public CompetenciaGraduacao getCompetenciaGraduacao() {
		return competenciaGraduacao;
	}
	public void setCompetenciaGraduacao(CompetenciaGraduacao competenciaGraduacao) {
		this.competenciaGraduacao = competenciaGraduacao;
	}


	
	
}
