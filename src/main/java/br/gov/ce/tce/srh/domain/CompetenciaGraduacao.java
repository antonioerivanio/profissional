package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Referente a tabela: TB_COMPETENCIAGRADUACAO
 * 
 * @since   : Nov 15, 2011, 11:02:19 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_COMPETENCIAGRADUACAO", schema=DatabaseMetadata.SCHEMA_SRH)
public class CompetenciaGraduacao extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCURSOFORMACAO")
    private CursoAcademica cursoAcademica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCOMPETENCIA")
    private Competencia competencia;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIOCOMPETENCIA", nullable=false)
    private Date inicioCompetencia;


	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public CursoAcademica getCursoAcademica() {return cursoAcademica;}
	public void setCursoAcademica(CursoAcademica cursoAcademica) {this.cursoAcademica = cursoAcademica;}

	public Competencia getCompetencia() {return competencia;}
	public void setCompetencia(Competencia competencia) {this.competencia = competencia;}

	public Date getInicioCompetencia() {return inicioCompetencia;}
	public void setInicioCompetencia(Date inicioCompetencia) {this.inicioCompetencia = inicioCompetencia;}
    
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}