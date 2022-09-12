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
import br.gov.ce.tce.srh.enums.DadoTCE;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_REABERTURAFOLHA", schema=DatabaseMetadata.SCHEMA_SRH)
public class ReaberturaFolhaEsocial extends BasicEntity<Long> implements Serializable{
 
	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "TP_INSC")
	private Integer tipoInscricaoEmpregador;
	  
	@Column(name = "NR_INSC")
	private String numeroInscricaoEmpregador;  
    
    @Column(name = "PER_APUR")
    private String periodoApuracao;
    
    @Column(name = "IND_APURACAO")
    private Integer indicativoApuracao;
    
    @Transient
    private String anoReferencia;
    @Transient
    private String mesReferencia;    
    @Transient
    private String referenciaMesAnoTransient;    
    
    
    public ReaberturaFolhaEsocial() {
      super();
    }

    public ReaberturaFolhaEsocial(Integer tipoInscricaoEmpregador, String numeroInscricaoEmpregador,  String peridoApuracao, Integer indicativoApuracao) {
      super();
      this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
      this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;     
      this.periodoApuracao = peridoApuracao;
      this.indicativoApuracao = indicativoApuracao;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }


    public String getReferencia() {
      return referencia;
    }

    public void setReferencia(String referencia) {
      this.referencia = referencia;
    }

    public Integer getTipoInscricaoEmpregador() {
      return tipoInscricaoEmpregador;
    }

    public void setTipoInscricaoEmpregador(Integer tipoInscricaoEmpregador) {
      this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
    }

    public String getNumeroInscricaoEmpregador() {  
      return numeroInscricaoEmpregador;
    }
    
    public void setNumeroInscricaoEmpregador(String numeroInscricaoEmpregador) {
      this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
    }
   
    public String getPeriodoApuracao() {
      return periodoApuracao;
    }

    public void setPeriodoApuracao(String peridoApuracao) {
      this.periodoApuracao = peridoApuracao;
    }

    public String getAnoReferencia() {
      return anoReferencia;
    }

    public void setAnoReferencia(String anoReferencia) {
      this.anoReferencia = anoReferencia;
    }

    public String getMesReferencia() {
      return mesReferencia;
    }

    public void setMesReferencia(String mesReferencia) {
      this.mesReferencia = mesReferencia;
    }

    public Integer getIndicativoApuracao() {
      return indicativoApuracao;
    }

    public void setIndicativoApuracao(Integer indicativoApuracao) {
      this.indicativoApuracao = indicativoApuracao;
    }

    public String getReferenciaMesAnoTransient() {
      return referenciaMesAnoTransient;
    }

    public void setReferenciaMesAnoTransient(String referenciaMesAno) {
      this.referenciaMesAnoTransient = referenciaMesAno;
    }
}
	
