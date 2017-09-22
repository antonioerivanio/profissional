package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_PESSOALCURSOPROF
 * 
 * @since   : Out 25, 2011, 15:14:18 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_PESSOALCURSOPROF", schema="SRH")
public class PessoalCursoProfissional implements Serializable {

    @EmbeddedId
    protected PessoalCursoProfissionalPk pk;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL", insertable=false, updatable=false )
    private Pessoal pessoal;

    @ManyToOne
    @JoinColumn(name = "IDCURSOPROFISSIONAL", insertable=false, updatable=false )
    private CursoProfissional cursoProfissional;

	@Column(name="AREAATUACAO", nullable=false)
	private boolean areaAtuacao;

	@Column(name="TEMPOPROMOCAO", nullable=false)
	private boolean tempoPromocao;


	public PessoalCursoProfissionalPk getPk() {return pk;}
	public void setPk(PessoalCursoProfissionalPk pk) {this.pk = pk;}

	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public CursoProfissional getCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(CursoProfissional cursoProfissional) {this.cursoProfissional = cursoProfissional;}

	public boolean isAreaAtuacao() {return areaAtuacao;}
	public void setAreaAtuacao(boolean areaAtuacao) {this.areaAtuacao = areaAtuacao;}

	public boolean isTempoPromocao() {return tempoPromocao;}
	public void setTempoPromocao(boolean tempoPromocao) {this.tempoPromocao = tempoPromocao;}

}
