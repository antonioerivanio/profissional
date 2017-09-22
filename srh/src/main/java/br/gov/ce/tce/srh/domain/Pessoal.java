package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_PESSOAL
 * 
 * @since   : Sep 30, 2011, 10:14:50 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_PESSOAL", schema="SRH")
public class Pessoal extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDESCOLARIDADE")	
	private Escolaridade escolaridade;

	@ManyToOne
	@JoinColumn(name = "IDESTADOCIVIL")	
	private EstadoCivil estadoCivil;

	@ManyToOne
	@JoinColumn(name = "IDRACA")	
	private Raca raca;

	@Column(name="CODCREDORSIC")
	private Long codCredorSic;

	@Column(name="NOME")
	private String nome;	
	
	@Column(name="NOMECOMPLETO")
	private String nomeCompleto;	

	@Column(name="NOMEPESQUISA")
	private String nomePesquisa;	

	@Column(name="ABREVIATURA")
	private String abreviatura;	

	@Column(name="NOMEPAI")
	private String nomePai;	

	@Column(name="NOMEMAE")
	private String nomeMae;	

	@Column(name="EMAIL")
	private String email;

	@Column(name="SEXO")
	private String sexo;

	@Temporal(TemporalType.DATE)
	@Column(name="DATANASCIMENTO")
	private Date nascimento;

	@Temporal(TemporalType.DATE)
	@Column(name="DATAOBITO")
	private Date obito;

	@Column(name="TIPOSANGUE")
	private String tipoSangue;

	@Column(name="FATORRH")
	private String fatorRH;

	@Column(name="ENDERECO")
	private String endereco;

	@Column(name="NUMERO")
	private String numero;

	@Column(name="COMPLEMENTO")
	private String complemento;

	@Column(name="BAIRRO")
	private String bairro;
		 
	@Column(name="CEP")
	private String cep;
		 
	@Column(name="MUNICIPIO")
	private String municipio;
 
	@ManyToOne
	@JoinColumn(name = "UFENDERECO")	
	private Uf ufEndereco;
 
	@Column(name="NATURALIDADE")
	private String naturalidade;
 
	@ManyToOne
	@JoinColumn(name="UF")	
	private Uf uf;

	@Column(name="PASEP")
	private String pasep;

	@Column(name="CPF")
	private String cpf;

	@Column(name="RG")
	private String rg;

	@Column(name="EMISSORRG")
	private String emissorRg;

	@ManyToOne
	@JoinColumn(name = "UFEMISSORRG")
	private Uf ufEmissorRg;

	@Temporal(TemporalType.DATE)
	@Column(name="DATAEMISSAORG")
	private Date dataEmissaoRg;

	@Column(name="TITULOELEITORAL")
	private String tituloEleitoral;

	@Column(name="ZONAELEITORAL")
	private String zonaEleitoral;

	@Column(name="SECAOELEITORAL")
	private String secaoEleitoral;

	@Column(name="DOCUMENTOMILITAR")
	private String documentoMilitar;

	@Column(name="TIPOCONTABBD")
	private Long tipoContaBbd;

	@Column(name="AGENCIABBD")
	private String agenciaBbd;

	@Column(name="CONTABBD")
	private String contaBbd;

	@Column(name="QTDDEPSF")
	private Long qtdDepsf;
		
	@Column(name="QTDDEPIR")
	private Long qtdDepir;
 
	@Column(name="QTDDEPPREV")
	private Long qtdDepprev;
 
	@Column(name="FOTO")
	private String foto;
	
	@Column(name="FICHA")
	private String ficha;

	@ManyToOne
	@JoinColumn(name="IDCATEGORIA")
	private PessoalCategoria categoria;

	@Temporal(TemporalType.DATE)
	@Column(name="DATAATUALIZACAO")
	private Date atualizacao;
 
	@Column(name="TELEFONE")
	private String telefone;

	@Column(name="CELULAR")
	private String celular;

	
	public Pessoal(){
		//seguindo padrao Java Beans...
	}


	public Pessoal(Long id, String cpf, String nomeCompleto) {
		//Para Dynamic Instatiation
		this.id = id;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
	}


	public Pessoal(Long id, String cpf, String nomeCompleto, String rg, String emissorRg, Uf ufEmissorRg, String nomeMae) {
		//Para Dynamic Instatiation
		this.id = id;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.rg = rg;
		this.emissorRg = emissorRg;
		this.ufEmissorRg = ufEmissorRg;
		this.nomeMae = nomeMae;
	}

	public Escolaridade getEscolaridade() {return escolaridade;}
	public void setEscolaridade(Escolaridade escolaridade) {this.escolaridade = escolaridade;}

	public EstadoCivil getEstadoCivil() {return estadoCivil;}
	public void setEstadoCivil(EstadoCivil estadoCivil) {this.estadoCivil = estadoCivil;}

	public Raca getRaca() {return raca;}
	public void setRaca(Raca raca) {this.raca = raca;}

	public Long getCodCredorSic() {return codCredorSic;}
	public void setCodCredorSic(Long codCredorSic) {this.codCredorSic = codCredorSic;}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public String getNomeCompleto() {return nomeCompleto;}
	public void setNomeCompleto(String nomeCompleto) {this.nomeCompleto = nomeCompleto;}

	public String getNomePesquisa() {return nomePesquisa;}
	public void setNomePesquisa(String nomePesquisa) {this.nomePesquisa = nomePesquisa;}

	public String getAbreviatura() {return abreviatura;}
	public void setAbreviatura(String abreviatura) {this.abreviatura = abreviatura;}

	public String getNomePai() {return nomePai;}
	public void setNomePai(String nomePai) {this.nomePai = nomePai;}

	public String getNomeMae() {return nomeMae;}
	public void setNomeMae(String nomeMae) {this.nomeMae = nomeMae;}

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getSexo() {return sexo;}
	public void setSexo(String sexo) {this.sexo = sexo;}

	public Date getNascimento() {return nascimento;}
	public void setNascimento(Date nascimento) {this.nascimento = nascimento;}
	
	public int getIdade(){return SRHUtils.calculaIdade(nascimento, obito);}

	public Date getObito() {return obito;}
	public void setObito(Date obito) {this.obito = obito;}

	public String getTipoSangue() {return tipoSangue;}
	public void setTipoSangue(String tipoSangue) {this.tipoSangue = tipoSangue;}

	public String getFatorRH() {return fatorRH;}
	public void setFatorRH(String fatorRH) {this.fatorRH = fatorRH;}

	public String getEndereco() {return endereco;}
	public void setEndereco(String endereco) {this.endereco = endereco;}

	public String getNumero() {return numero;}
	public void setNumero(String numero) {this.numero = numero;}

	public String getComplemento() {return complemento;}
	public void setComplemento(String complemento) {this.complemento = complemento;}

	public String getBairro() {return bairro;}
	public void setBairro(String bairro) {this.bairro = bairro;}

	public String getCep() {return cep;}
	public void setCep(String cep) {this.cep = cep;}

	public String getMunicipio() {return municipio;}
	public void setMunicipio(String municipio) {this.municipio = municipio;}

	public Uf getUfEndereco() {return ufEndereco;}
	public void setUfEndereco(Uf ufEndereco) {this.ufEndereco = ufEndereco;}

	public String getNaturalidade() {return naturalidade;}
	public void setNaturalidade(String naturalidade) {this.naturalidade = naturalidade;}

	public Uf getUf() {return uf;}
	public void setUf(Uf uf) {this.uf = uf;}

	public String getPasep() {return pasep;}
	public void setPasep(String pasep) {this.pasep = pasep;}

	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {this.cpf = cpf;}

	public String getRg() {return rg;}
	public void setRg(String rg) {this.rg = rg;}

	public String getEmissorRg() {return emissorRg;}
	public void setEmissorRg(String emissorRg) {this.emissorRg = emissorRg;}

	public Uf getUfEmissorRg() {return ufEmissorRg;}
	public void setUfEmissorRg(Uf ufEmissorRg) {this.ufEmissorRg = ufEmissorRg;}

	public Date getDataEmissaoRg() {return dataEmissaoRg;}
	public void setDataEmissaoRg(Date dataEmissaoRg) {this.dataEmissaoRg = dataEmissaoRg;}

	public String getTituloEleitoral() {return tituloEleitoral;}
	public void setTituloEleitoral(String tituloEleitoral) {this.tituloEleitoral = tituloEleitoral;}

	public String getZonaEleitoral() {return zonaEleitoral;}
	public void setZonaEleitoral(String zonaEleitoral) {this.zonaEleitoral = zonaEleitoral;}

	public String getSecaoEleitoral() {return secaoEleitoral;}
	public void setSecaoEleitoral(String secaoEleitoral) {this.secaoEleitoral = secaoEleitoral;}

	public String getDocumentoMilitar() {return documentoMilitar;}
	public void setDocumentoMilitar(String documentoMilitar) {this.documentoMilitar = documentoMilitar;}

	public Long getTipoContaBbd() {return tipoContaBbd;}
	public void setTipoContaBbd(Long tipoContaBbd) {this.tipoContaBbd = tipoContaBbd;}

	public String getAgenciaBbd() {return agenciaBbd;}
	public void setAgenciaBbd(String agenciaBbd) {this.agenciaBbd = agenciaBbd;}

	public String getContaBbd() {return contaBbd;}
	public void setContaBbd(String contaBbd) {this.contaBbd = contaBbd;}

	public Long getQtdDepsf() {return qtdDepsf;}
	public void setQtdDepsf(Long qtdDepsf) {this.qtdDepsf = qtdDepsf;}

	public Long getQtdDepir() {return qtdDepir;}
	public void setQtdDepir(Long qtdDepir) {this.qtdDepir = qtdDepir;}

	public Long getQtdDepprev() {return qtdDepprev;}
	public void setQtdDepprev(Long qtdDepprev) {this.qtdDepprev = qtdDepprev;}

	public String getFoto() {return foto;}
	public void setFoto(String foto) {this.foto = foto;}

	public String getFicha() {return ficha;}
	public void setFicha(String ficha) {this.ficha = ficha;}

	public PessoalCategoria getCategoria() {return categoria;}
	public void setCategoria(PessoalCategoria categoria) {this.categoria = categoria;}

	public Date getAtualizacao() {return atualizacao;}
	public void setAtualizacao(Date atualizacao) {this.atualizacao = atualizacao;}

	public String getTelefone() {return telefone;}
	public void setTelefone(String telefone) {this.telefone = telefone;}

	public String getCelular() {return celular;}
	public void setCelular(String celular) {this.celular = celular;}	

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}