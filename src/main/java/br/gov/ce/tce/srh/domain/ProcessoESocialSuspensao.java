package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ce.tce.srh.enums.IndicativoSuspensao;
import br.gov.ce.tce.srh.enums.SimNao;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_PROCESSO_SUSPENSAO", schema = "SRH")
public class ProcessoESocialSuspensao extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "INDICATIVOSUSPENSAO")
	private String indicativoSuspensao;
	
	@Column(name = "DATADECISAO")
	@Temporal(TemporalType.DATE)
	private Date dataDecisao;
	
	@Column(name = "INDICATIVODEPOSITO")
	@Enumerated(EnumType.STRING)
	private SimNao indicativoDeposito;
	
	@Column(name = "IDPROCESSO")
	private Long processo;
	
	@Transient
	private boolean excluida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IndicativoSuspensao getIndicativoSuspensao() {
		return IndicativoSuspensao.getByCodigo(this.indicativoSuspensao);
	}

	public void setIndicativoSuspensao(IndicativoSuspensao indicativoSuspensao) {
		if(indicativoSuspensao != null)
			this.indicativoSuspensao = indicativoSuspensao.getCodigo();
	}

	public Date getDataDecisao() {
		return dataDecisao;
	}

	public void setDataDecisao(Date dataDecisao) {
		this.dataDecisao = dataDecisao;
	}		

	public SimNao getIndicativoDeposito() {
		return indicativoDeposito;
	}

	public void setIndicativoDeposito(SimNao indicativoDeposito) {
		this.indicativoDeposito = indicativoDeposito;
	}

	public Long getProcesso() {
		return processo;
	}

	public void setProcesso(Long processo) {
		this.processo = processo;
	}

	public boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}
	
	public boolean isValido() {
		return this.indicativoSuspensao != null && !this.indicativoSuspensao.isEmpty()
				&& this.dataDecisao != null
				&& this.indicativoDeposito != null;				
	}
	
	

}
