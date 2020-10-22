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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_REFERENCIAFUNCIONAL
 * 
 * @since   : Jan 17, 2012, 17:51:00
 * @author  : robson.castro@ivia.com.br
 *
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_REFERENCIAFUNCIONAL", schema=DatabaseMetadata.SCHEMA_SRH)
public class ReferenciaFuncional extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable = false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL", nullable = false)	
	private Funcional funcional;

	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDCLASSEREFERENCIA", nullable = false)	
	private ClasseReferencia classeReferencia;

	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDTIPOMOVIMENTO", nullable = false)	
	private TipoMovimento tipoMovimento;
	
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDTIPOPUBLICACAO", nullable = true)
	private TipoPublicacao tipoPublicacao;
	
	@Column(name="DESCRICAO", nullable = false, length = 2000)
	private String descricao;	
	
	@Temporal(TemporalType.DATE)
	@Column(name="INICIO", nullable = false)
	private Date inicio;

	@Temporal(TemporalType.DATE)
	@Column(name="FIM", nullable = true)
	private Date fim;

	@Temporal(TemporalType.DATE)
	@Column(name="DATAATO", nullable = true)
	private Date dataAto;

	@Temporal(TemporalType.DATE)
	@Column(name="DOEATO", nullable = true)
	private Date doeAto;
	
	@Transient
	private Integer dias;

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public ClasseReferencia getClasseReferencia() {return classeReferencia;}
	public void setClasseReferencia(ClasseReferencia classeReferencia) {this.classeReferencia = classeReferencia;}

	public TipoMovimento getTipoMovimento() {return tipoMovimento;}
	public void setTipoMovimento(TipoMovimento tipoMovimento) {this.tipoMovimento = tipoMovimento;}
	
	public TipoPublicacao getTipoPublicacao() {return tipoPublicacao;}
	public void setTipoPublicacao(TipoPublicacao tipoPublicacao) {this.tipoPublicacao = tipoPublicacao;}
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao.toUpperCase();}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public Date getDataAto() {return dataAto;}
	public void setDataAto(Date dataAto) {this.dataAto = dataAto;}

	public Date getDoeAto() {return doeAto;}
	public void setDoeAto(Date doeAto) {this.doeAto = doeAto;}


	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public Integer getDias() {
		Date dfim = (getFim()==null?new Date():getFim());		
		Date dini = getInicio();
		dias = SRHUtils.dataDiff(dini, dfim);
		return dias;
	}
	
}