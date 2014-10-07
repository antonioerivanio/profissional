package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Referente a tabela: TB_PESSOALCURSO
 * 
 * @since   : Nov 15, 2011, 10:15:38 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_PESSOALCURSO", schema="SRH")
public class PessoalCursoAcademica implements Serializable {

    @EmbeddedId
    protected PessoalCursoAcademicaPk pk;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPESSOAL", insertable=false, updatable=false )
    private Pessoal pessoal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCURSOFORMACAO", insertable=false, updatable=false )
    private CursoAcademica cursoAcademica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDINSTITUICAO")
    private Instituicao instituicao;
    
    @Temporal(TemporalType.DATE)
    @Column(name="DATACONCLUSAO")
    private Date dataConclusao;
    

	public PessoalCursoAcademicaPk getPk() {return pk;}
	public void setPk(PessoalCursoAcademicaPk pk) {this.pk = pk;}

	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public CursoAcademica getCursoAcademica() {return cursoAcademica;}
	public void setCursoAcademica(CursoAcademica cursoAcademica) {this.cursoAcademica = cursoAcademica;}

	public Instituicao getInstituicao() {return instituicao;}
	public void setInstituicao(Instituicao instituicao) {this.instituicao = instituicao;}
	
	public Date getDataConclusao() {return dataConclusao;}
	public void setDataConclusao(Date dataconclusao) {this.dataConclusao = dataconclusao;}

}
