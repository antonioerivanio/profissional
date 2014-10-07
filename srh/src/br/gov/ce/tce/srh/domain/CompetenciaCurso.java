package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Referente a tabela: TB_COMPETENCIACURSO
 * 
 * @since   : Out 25, 2011, 15:15:34 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_COMPETENCIACURSO", schema="SRH")
public class CompetenciaCurso extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID", nullable=false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDCOMPETENCIA")
    private Competencia competencia;

    @ManyToOne
    @JoinColumn(name = "IDCURSOPROFISSIONAL")
    private CursoProfissional cursoProfissional;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIOCOMPETENCIA", nullable=false)
    private Date iniciocompetencia;


	public Competencia getCompetencia() {return competencia;}
	public void setCompetencia(Competencia competencia) {this.competencia = competencia;}

	public CursoProfissional getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(CursoProfissional cursoProfissional) {this.cursoProfissional = cursoProfissional;}

	public Date getIniciocompetencia() {return iniciocompetencia;}
	public void setIniciocompetencia(Date iniciocompetencia) {this.iniciocompetencia = iniciocompetencia;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

}