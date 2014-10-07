package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
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

}
