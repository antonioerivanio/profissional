package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.EnumTipoCursoProfissional;

/**
 * Referente a tabela: TB_CURSOPROFISSIONAL
 * 
 * @since   : Sep 9, 2011, 13:59:44 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_CURSOPROFISSIONAL", schema="SRH")
public class CursoProfissional extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable=false)
	private Long id;

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDAREAPROFISSIONAL")
	private AreaProfissional area;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDINSTITUICAO")
	private Instituicao instituicao;

	@OneToMany(mappedBy = "cursoProfissional")
	private List<CompetenciaCurso> competencias;
		
	@Column(name="DESCRICAO", nullable=false, length=100)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name="INICIO")
	private Date inicio;

	@Temporal(TemporalType.DATE)
	@Column(name="FIM")
	private Date fim;

	@Column(name="CARGAHORARIA")
	private Long cargaHoraria;

	@Column(name="AREAATUACAO", nullable=false)
	private boolean areaAtuacao;

	@Column(name = "POSGRADUACAO")
	@Enumerated(EnumType.ORDINAL)
	private EnumTipoCursoProfissional posGraduacao;

	@Column(name="PRESENCIAL", nullable=false)
	private boolean presencial;

	@Column(name="TEMPOPROMOCAO", nullable=false)
	private boolean tempoPromocao;

	@Column(name="TREINAMENTO", nullable=false)
	private boolean treinamento;
	
	
	public CursoProfissional(Long id, AreaProfissional area, String descricao, Date inicio, Date fim) {
		this.id = id;
		this.area = area;
		this.descricao = descricao;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	public CursoProfissional() {
		
	}

	public AreaProfissional getArea() {return area;}
	public void setArea(AreaProfissional area) {this.area = area;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public Long getCargaHoraria() {return cargaHoraria;}
	public void setCargaHoraria(Long cargaHoraria) {this.cargaHoraria = cargaHoraria;}	

	public Instituicao getInstituicao() {return instituicao;}
	public void setInstituicao(Instituicao instituicao) {this.instituicao = instituicao;}

	public boolean isAreaAtuacao() {return areaAtuacao;}
	public void setAreaAtuacao(boolean areaAtuacao) {this.areaAtuacao = areaAtuacao;}

	public EnumTipoCursoProfissional getPosGraduacao() {return posGraduacao;}
	public void setPosGraduacao(EnumTipoCursoProfissional posGraduacao) {this.posGraduacao = posGraduacao;}

	public boolean isPresencial() {return presencial;}
	public void setPresencial(boolean presencial) {this.presencial = presencial;}

	public boolean isTempoPromocao() {return tempoPromocao;}
	public void setTempoPromocao(boolean tempoPromocao) {this.tempoPromocao = tempoPromocao;}

	public boolean isTreinamento() {return treinamento;}
	public void setTreinamento(boolean treinamento) {this.treinamento = treinamento;}

	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public List<CompetenciaCurso> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(List<CompetenciaCurso> competencias) {
		this.competencias = competencias;
	}
	
}