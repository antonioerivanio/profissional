package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.util.SRHUtils;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ABONOPERMANENCIA", schema="SRH")
@NamedQueries({
	@NamedQuery(name = "AbonoPermanencia.findAll", query = "SELECT a FROM AbonoPermanencia a ORDER BY a.funcional.nomeCompleto")
})
public class AbonoPermanencia extends BasicEntity<Long> implements Serializable{

	@Id
    @Column(name = "ID")
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
		
	@Temporal(TemporalType.DATE)
	@Column(name = "DATAIMPLANTACAO")
	private Date dataImplantacao;
	
	@Column(name = "PROCESSO", length=10)
    private String processo;	
	
	@Column(name = "OBSERVACAO")
	private String observacao;
	
	public AbonoPermanencia() {
		
	}
	@Override
	public Long getId() {return this.id;}

	@Override
	public void setId(Long id) {this.id = id;}
	
	
	public Funcional getFuncional() {
		return funcional;
	}
	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}
	public Date getDataImplantacao() {
		return dataImplantacao;
	}
	public void setDataImplantacao(Date dataImplantacao) {
		this.dataImplantacao = dataImplantacao;
	}
	public String getProcesso() {
		processo = SRHUtils.removerMascara(processo);
		return processo;
	}
	public void setProcesso(String processo) {this.processo = processo;}
	public String getProcessoFormatado() {		
		try {
			processo = SRHUtils.formatarProcesso(processo);
		} catch (ParseException e) {
			return "";
		}
		return processo;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}		

}
