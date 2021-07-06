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

import br.gov.ce.tce.srh.sca.domain.Usuario;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_DEBITOFOLGA", schema=DatabaseMetadata.SCHEMA_SRH)
public class DebitoFolga extends BasicEntity<Long> implements Serializable {
	
	@Id
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "IDFOLGA")
    private Folga folga; 
	
	@Temporal(TemporalType.DATE)
    @Column(name = "DTUTILIZACAO")
    private Date data;
	
	@Column(name = "SALDOUTILIZADO")
	private Double saldoUtilizado;
	
	@Column(name = "OBSERVACAO")
	private String observacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIOINCLUSAO")
    private Usuario usuarioInclusao;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "DTINCLUSAO")
    private Date dataInclusao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Folga getFolga() {
		return folga;
	}

	public void setFolga(Folga folga) {
		this.folga = folga;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getSaldoUtilizado() {
		return saldoUtilizado;
	}

	public void setSaldoUtilizado(Double saldoUtilizado) {
		this.saldoUtilizado = saldoUtilizado;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
}
