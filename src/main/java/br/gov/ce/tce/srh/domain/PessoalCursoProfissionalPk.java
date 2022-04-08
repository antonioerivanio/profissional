package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Referente a tabela: TB_PESSOALCURSO
 * 
 * @since   : Nov 15, 2011, 10:10:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */

@Embeddable
@SuppressWarnings("serial")
public class PessoalCursoProfissionalPk implements Serializable {

    @Column(name = "IDPESSOAL", nullable=false)
    private Long pessoal;

    @Column(name = "IDCURSOPROFISSIONAL", nullable=false)
    private Long cursoProfissional;


	public Long getPessoal() {return pessoal;}
	public void setPessoal(Long pessoal) {this.pessoal = pessoal;}

	public Long getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(Long cursoProfissional) {this.cursoProfissional = cursoProfissional;}
	
	@Override
	public int hashCode() {
		return Objects.hash(cursoProfissional, pessoal);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoalCursoProfissionalPk other = (PessoalCursoProfissionalPk) obj;
		return Objects.equals(cursoProfissional, other.cursoProfissional) && Objects.equals(pessoal, other.pessoal);
	}
	
	

}
