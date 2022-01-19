package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.sca.domain.Usuario;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name = "TB_VINCULORGPS", schema = DatabaseMetadata.SCHEMA_SRH)
public class VinculoRGPS extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;

	@ManyToOne
	@JoinColumn(name = "IDPESSOAJURIDICA")
	private PessoaJuridica pessoaJuridica;

	@JoinColumn(name = "IDTIPOESOCIAL")
	private Long tipoEsocial;
	
	@JoinColumn(name = "VALOROUTRAEMPRESA")
	private Double valorOutraEmpresa;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO")
	private Date inicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "FIM")
	private Date fim;
	
	@ManyToMany
	@Column(name = "IDUSUARIO")
	private Usuario usuario;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public Long getTipoEsocial() {
		return tipoEsocial;
	}

	public void setTipoEsocial(Long tipoEsocial) {
		this.tipoEsocial = tipoEsocial;
	}

	public Double getValorOutraEmpresa() {
		return valorOutraEmpresa;
	}

	public void setValorOutraEmpresa(Double valorOutraEmpresa) {
		this.valorOutraEmpresa = valorOutraEmpresa;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
