package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 09:08:20 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_PESSOAJURIDICA", schema = DatabaseMetadata.SCHEMA_SRH)
public class PessoaJuridica extends BasicEntity<Long> implements Serializable {
	

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CNPJ", nullable = false)
	private String cnpj;

	@Column(name = "RAZAOSOCIAL", nullable = false)
	private String razaoSocial;

	@Column(name = "NOMEFANTASIA", nullable = false)
	private String nomeFantasia;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIOINCLUSAO")
    private Usuario usuarioInclusao;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "DATAALTERACAO")
    private Date dataAlteracao;
	
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "FG_EMPRESAAREASAUDE")
	private EmpresaAreaSaude flgEmpresaAreSaude;
	
	
	public PessoaJuridica() {
	}

	public PessoaJuridica(Long id, String cnpj, String razaoSocial, String nomeFantasia) {
		this.id = id;
		this.cnpj = cnpj.trim();
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCnpj() {
		if(cnpj != null) {
			cnpj.trim();
		}
		
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	

	
}
