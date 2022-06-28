package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FOLGA", schema=DatabaseMetadata.SCHEMA_SRH)
public class Folga extends BasicEntity<Long> implements Serializable {
	
	@Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;

    @ManyToOne
    @JoinColumn(name = "IDTIPOFOLGA")
    private TipoFolga tipoFolga;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DTINICIO")
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DTFIM")
    private Date fim;
    
    @Column(name = "DESCRICAO", length = 4000)
    private String descricao;
    
    @Column(name = "PATHCOMPROVANTE", length = 4000)
	private String caminhoComprovante;
    
    @Column(name = "SALDOINICIAL")
    private Double saldoInicial;
    
    @Column(name = "SALDOFINAL")
    private Double saldoFinal;
    
    @NotAudited
    @OneToMany(mappedBy = "folga", fetch = FetchType.EAGER)
    private List<DebitoFolga> debitos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoal getPessoal() {
		return pessoal;
	}

	public void setPessoal(Pessoal pessoal) {
		this.pessoal = pessoal;
	}

	public TipoFolga getTipoFolga() {
		return tipoFolga;
	}

	public void setTipoFolga(TipoFolga tipoFolga) {
		this.tipoFolga = tipoFolga;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCaminhoComprovante() {
		return caminhoComprovante;
	}

	public void setCaminhoComprovante(String caminhoComprovante) {
		this.caminhoComprovante = caminhoComprovante;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Double getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public List<DebitoFolga> getDebitos() {
		return debitos;
	}

	public void setDebitos(List<DebitoFolga> debitos) {
		this.debitos = debitos;
	}

}
