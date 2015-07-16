package br.gov.ce.tce.srh.sapjava.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.BasicEntity;

/**
 * Referente a tabela: ENTIDADE
 * 
 * @author ROBSON CASTRO, em 09/01/2012
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="ENTIDADE", schema="SAPJAVA")
public class Entidade extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="IDENTIDADE")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDSETOR")	
	private Setor setor;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDSETORORIGEM")	
	private Setor setorOrigem;

	@Column(name="CNPJ", nullable=true, length=20)
	private String cnpj;

	@Column(name="DSENTIDADE", nullable=false, length=200)
	private String descricaoEntidade;

	@Column(name="DSENDERECOENTIDADE", nullable=true, length=100)
	private String endereco;

	@Column(name="DSBAIRROENTIDADE", nullable=true, length=100)
	private String bairro;
	
	@Column(name="DSCIDADEENTIDADE", nullable=true, length=100)
	private String cidade;
	
	@Column(name="DSESTADOENTIDADE", nullable=true, length=2)
	private String estado;
	
	@Column(name="DSCEPENTIDADE", nullable=true, length=10)
	private String cep;
	
	@Column(name="DSFONEENTIDADE", nullable=true, length=10)
	private String fone;
	
	@Column(name="DSFAXENTIDADE", nullable=true, length=10)
	private String fax;

	@Column(name="DSEMAILENTIDADE", nullable=true, length=100)
	private String email;
	
	@Column(name="DSENTIDADESIGLA", nullable=true, length=20)
	private String entidadeSigla;
	
	@Column(name="TPENTIDADEESFERA", nullable=false)
	private Long tipoEntidadeEsfera;
	
	@Column(name="DSSENHA", nullable=true, length=20)
	private String senha;
	
	@Column(name="FISCALIZADO", nullable=false)
	private Long fiscalizado;

	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public Setor getSetorOrigem() {return setorOrigem;}
	public void setSetorOrigem(Setor setorOrigem) {this.setorOrigem = setorOrigem;}

	public String getCnpj() {return cnpj;}
	public void setCnpj(String cnpj) {this.cnpj = cnpj;}

	public String getDescricaoEntidade() {return descricaoEntidade;}
	public void setDescricaoEntidade(String descricaoEntidade) {this.descricaoEntidade = descricaoEntidade;}

	public String getEndereco() {return endereco;}
	public void setEndereco(String endereco) {this.endereco = endereco;}

	public String getBairro() {return bairro;}
	public void setBairro(String bairro) {this.bairro = bairro;}

	public String getCidade() {return cidade;}
	public void setCidade(String cidade) {this.cidade = cidade;}

	public String getEstado() {return estado;}
	public void setEstado(String estado) {this.estado = estado;}

	public String getCep() {return cep;}
	public void setCep(String cep) {this.cep = cep;}

	public String getFone() {return fone;}
	public void setFone(String fone) {this.fone = fone;}

	public String getFax() {return fax;}
	public void setFax(String fax) {this.fax = fax;}

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getEntidadeSigla() {return entidadeSigla;}
	public void setEntidadeSigla(String entidadeSigla) {this.entidadeSigla = entidadeSigla;}

	public Long getTipoEntidadeEsfera() {return tipoEntidadeEsfera;}
	public void setTipoEntidadeEsfera(Long tipoEntidadeEsfera) {this.tipoEntidadeEsfera = tipoEntidadeEsfera;}

	public String getSenha() {return senha;}
	public void setSenha(String senha) {this.senha = senha;}

	public Long getFiscalizado() {return fiscalizado;}
	public void setFiscalizado(Long fiscalizado) {this.fiscalizado = fiscalizado;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
