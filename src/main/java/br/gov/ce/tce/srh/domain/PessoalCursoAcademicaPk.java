package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Referente a tabela: TB_PESSOALCURSO
 * 
 * @since   : Out 25, 2011, 15:14:08 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Embeddable
@SuppressWarnings("serial")
public class PessoalCursoAcademicaPk implements Serializable {

    @Column(name = "IDPESSOAL", nullable=false)
    private Long pessoal;

    @Column(name = "IDCURSOFORMACAO", nullable=false)
    private Long cursoAcademico;

	public Long getPessoal() {return pessoal;}
	public void setPessoal(Long pessoal) {this.pessoal = pessoal;}

	public Long getCursoAcademico() {return cursoAcademico;}
	public void setCursoAcademico(Long cursoAcademico) {this.cursoAcademico = cursoAcademico;}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(cursoAcademico, pessoal);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoalCursoAcademicaPk other = (PessoalCursoAcademicaPk) obj;
		return Objects.equals(cursoAcademico, other.cursoAcademico) && Objects.equals(pessoal, other.pessoal);
	}

	
}
