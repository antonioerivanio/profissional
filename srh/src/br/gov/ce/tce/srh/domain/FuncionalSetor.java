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
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_FUNCIONALSETOR
 * 
 * @since   : Dez 19, 2011, 17:10:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FUNCIONALSETOR", schema="SRH")
public class FuncionalSetor extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDSETOR")
	private Setor setor;
	
	@Column(name="OBSERVACAO" , nullable=false)
	private String observacao;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INICIO" , nullable=false)
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FIM")
	private Date dataFim;

	@Transient
	private Integer dias;

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}
	
	public Date getDataInicio() {return dataInicio;}
	public void setDataInicio(Date dataInicio) {this.dataInicio = dataInicio;}

	public Date getDataFim() {return dataFim;}
	public void setDataFim(Date dataFim) {this.dataFim = dataFim;}
  
	public Integer getDias() {
		Date dfim = (getDataFim()==null?new Date():getDataFim());		
		Date dini = getDataInicio();
		dias = SRHUtils.dataDiff(dini, dfim);
		return dias;
	}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
