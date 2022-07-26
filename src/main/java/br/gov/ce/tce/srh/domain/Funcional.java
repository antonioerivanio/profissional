package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import br.gov.ce.tce.srh.enums.StatusFuncional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_FUNCIONAL
 * 
 * @since : Out 3, 2011, 14:09:10 AM
 * @author : robstownholanda@ivia.com.br
 *
 */
@Audited
@Entity
@SuppressWarnings("serial")
@Table(name = "TB_FUNCIONAL", schema = DatabaseMetadata.SCHEMA_SRH)
public class Funcional extends BasicEntity<Long> implements Serializable {

  @Id
  @Column(name = "ID")
  private Long id;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDPESSOAL")
  private Pessoal pessoal;

  @Column(name = "IDORGAOORIGEM")
  private Long idOrgaoOrigem;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDSETOR")
  private Setor setor;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDOCUPACAO")
  private Ocupacao ocupacao;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDCLASSEREFERENCIA")
  private ClasseReferencia classeReferencia;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "idespecialidadecargo")
  private EspecialidadeCargo especialidadeCargo;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "idorientacaocargo")
  private OrientacaoCargo orientacaoCargo;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDTIPOMOVIMENTOENTRADA")
  private TipoMovimento tipoMovimentoEntrada;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDTIPOMOVIMENTOSAIDA")
  private TipoMovimento tipoMovimentoSaida;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDCBO")
  private Cbo cbo;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDFOLHA")
  private Folha folha;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDTIPOPUBLICACAONOMEACAO")
  private TipoPublicacao tipoPublicacaoNomeacao;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDTIPOPUBLICACAOSAIDA")
  private TipoPublicacao tipoPublicacaoSaida;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDSITUACAO")
  private Situacao situacao;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDTIPOVINCULO")
  private Vinculo vinculo;

  @Column(name = "NOME")
  private String nome;

  @Column(name = "NOMECOMPLETO")
  private String nomeCompleto;

  @Column(name = "NOMEPESQUISA")
  private String nomePesquisa;

  @Column(name = "MATRICULA")
  private String matricula;

  @Column(name = "MATRICULAESTADUAL")
  private String matriculaEstadual;

  @Column(name = "CALCULOPCC")
  private boolean calculoPcc;

  @Column(name = "QTDQUINTOS")
  private Long qtdQuintos;

  @Transient
  private Long qtdeDias;

  @Column(name = "LEIINCORPORACAO")
  private String leiIncorporacao;

  @Column(name = "PONTO")
  private boolean ponto;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "ATIVOFP")
  private boolean atipoFp;

  @NotAudited
  @Column(name = "FLPORTALTRANSPARENCIA")
  private boolean ativoPortal;

  @Column(name = "IRRF")
  private boolean IRRF;

  @Column(name = "SUPSECINTEGRAL")
  private boolean supSecIntegral;

  @Column(name = "PROPORCIONALIDADE")
  private Long proporcionalidade;

  @Column(name = "SALARIOORIGEM")
  private BigDecimal salarioOrigem;

  @Transient
  private String salarioOrigemMonetario;

  @Column(name = "ABONOPREVIDENCIARIO")
  private boolean abonoPrevidenciario;

  @Temporal(TemporalType.DATE)
  @Column(name = "DATAPOSSE")
  private Date posse;

  @Temporal(TemporalType.DATE)
  @Column(name = "DATAEXERCICIO")
  private Date exercicio;

  @Temporal(TemporalType.DATE)
  @Column(name = "DATASAIDA")
  private Date saida;

  @Temporal(TemporalType.DATE)
  @Column(name = "DOENOMEACAO")
  private Date doeNomeacao;

  @Temporal(TemporalType.DATE)
  @Column(name = "DOESAIDA")
  private Date doeSaida;

  @Column(name = "DESCRICAONOMEACAO")
  private String descricaoNomeacao;

  @Column(name = "DESCRICAOSAIDA")
  private String descricaoSaida;

  @Column(name = "PREVIDENCIA")
  private Long previdencia;

  @Column(name = "REGIME")
  private Long regime;

  @Column(name = "IDREPRESENTACAOCARGO")
  private Long idRepresentacaoCargo;

  @NotAudited
  @Column(name = "IDSETORDESIGNADO")
  private Long idSetorDesignado;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "IDPESSOAJURIDICA")
  private PessoaJuridica pessoaJuridica;

  @Transient
  private int anos;

  @Transient
  private int meses;

  @Transient
  private int dias;

  @NotAudited
  @OneToOne
  @JoinColumn(name = "IDAPOSENTADORIA")
  private Aposentadoria aposentadoria;

  @NotAudited
  @ManyToOne
  @JoinColumn(name = "CODCATEGORIA", referencedColumnName = "CODIGO")
  private CodigoCategoria codigoCategoria;


  public Funcional() {
    // seguindo padrao Java Beans...
  }

  public Funcional(Long idFuncional, Long idPessoal, String nomePessoal, String cpfPessoal) {
    this.id = idFuncional;
   this.pessoal = new Pessoal(idPessoal, nomePessoal, cpfPessoal);
  }


  public Funcional(Long id, String matricula, Pessoal pessoal, String nomeCompleto, Setor setor, Ocupacao ocupacao, Date exercicio) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.pessoal = pessoal;
    this.nomeCompleto = nomeCompleto;
    this.setor = setor;
    this.ocupacao = ocupacao;
    this.exercicio = exercicio;

  }

  public Funcional(Long id, String matricula, TipoMovimento tipoMovimentoEntrada, Ocupacao ocupacao, Date dataEntrada, Date dataSaida) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.tipoMovimentoEntrada = tipoMovimentoEntrada;
    this.ocupacao = ocupacao;
    this.exercicio = dataEntrada;
    this.saida = dataSaida;

  }

  public Funcional(Long id, String matricula, TipoMovimento tipoMovimentoEntrada, Ocupacao ocupacao, Date dataEntrada, Date dataSaida, Date dataPosse) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.tipoMovimentoEntrada = tipoMovimentoEntrada;
    this.ocupacao = ocupacao;
    this.exercicio = dataEntrada;
    this.saida = dataSaida;
    this.posse = dataPosse;
  }

  public Funcional(Long id, String matricula, Pessoal pessoal, String nome) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.pessoal = pessoal;
    this.nome = nome;
  }

  public Funcional(Long id, String matricula, Pessoal pessoal, Setor setor) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.pessoal = pessoal;
    this.setor = setor;
  }

  public Funcional(Long id, String matricula, Pessoal pessoal, Setor setor, Ocupacao ocupacao, Vinculo vinculo) {
    // Para Dynamic Instatiation
    this.id = id;
    this.matricula = matricula;
    this.pessoal = pessoal;
    this.setor = setor;
    this.ocupacao = ocupacao;
    this.vinculo = vinculo;
  }
  
  public Funcional(Long id, String matricula,  String nome) {
	    // Para Dynamic Instatiation
	    this.id = id;
	    this.matricula = matricula;	   
	    this.nome = nome;
  }

  public Pessoal getPessoal() {
    return pessoal;
  }

  public void setPessoal(Pessoal pessoal) {
    this.pessoal = pessoal;
  }

  public Long getIdOrgaoOrigem() {
    return idOrgaoOrigem;
  }

  public void setIdOrgaoOrigem(Long idOrgaoOrigem) {
    this.idOrgaoOrigem = idOrgaoOrigem;
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public Ocupacao getOcupacao() {
    return ocupacao;
  }

  public void setOcupacao(Ocupacao ocupacao) {
    this.ocupacao = ocupacao;
  }

  public ClasseReferencia getClasseReferencia() {
    return classeReferencia;
  }

  public void setClasseReferencia(ClasseReferencia classeReferencia) {
    this.classeReferencia = classeReferencia;
  }

  public EspecialidadeCargo getEspecialidadeCargo() {
    return especialidadeCargo;
  }

  public void setEspecialidadeCargo(EspecialidadeCargo especialidadeCargo) {
    this.especialidadeCargo = especialidadeCargo;
  }

  public OrientacaoCargo getOrientacaoCargo() {
    return orientacaoCargo;
  }

  public void setOrientacaoCargo(OrientacaoCargo orientacaoCargo) {
    this.orientacaoCargo = orientacaoCargo;
  }

  public TipoMovimento getTipoMovimentoEntrada() {
    return tipoMovimentoEntrada;
  }

  public void setTipoMovimentoEntrada(TipoMovimento tipoMovimentoEntrada) {
    this.tipoMovimentoEntrada = tipoMovimentoEntrada;
  }

  public TipoMovimento getTipoMovimentoSaida() {
    return tipoMovimentoSaida;
  }

  public void setTipoMovimentoSaida(TipoMovimento tipoMovimentoSaida) {
    this.tipoMovimentoSaida = tipoMovimentoSaida;
  }

  public Cbo getCbo() {
    return cbo;
  }

  public void setCbo(Cbo cbo) {
    this.cbo = cbo;
  }

  public Folha getFolha() {
    return folha;
  }

  public void setFolha(Folha folha) {
    this.folha = folha;
  }

  public TipoPublicacao getTipoPublicacaoNomeacao() {
    return tipoPublicacaoNomeacao;
  }

  public void setTipoPublicacaoNomeacao(TipoPublicacao tipoPublicacaoNomeacao) {
    this.tipoPublicacaoNomeacao = tipoPublicacaoNomeacao;
  }

  public TipoPublicacao getTipoPublicacaoSaida() {
    return tipoPublicacaoSaida;
  }

  public void setTipoPublicacaoSaida(TipoPublicacao tipoPublicacaoSaida) {
    this.tipoPublicacaoSaida = tipoPublicacaoSaida;
  }

  public Situacao getSituacao() {
    return situacao;
  }

  public void setSituacao(Situacao situacao) {
    this.situacao = situacao;
  }

  public Vinculo getVinculo() {
    return vinculo;
  }

  public void setVinculo(Vinculo vinculo) {
    this.vinculo = vinculo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome.toUpperCase();
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto.toUpperCase();
  }

  public String getNomePesquisa() {
    return nomePesquisa;
  }

  public void setNomePesquisa(String nomePesquisa) {
    this.nomePesquisa = nomePesquisa.toLowerCase();
  }

  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  public String getMatriculaEstadual() {
    return matriculaEstadual;
  }

  public void setMatriculaEstadual(String matriculaEstadual) {
    this.matriculaEstadual = matriculaEstadual;
  }

  public boolean isCalculoPcc() {
    return calculoPcc;
  }

  public void setCalculoPcc(boolean calculoPcc) {
    this.calculoPcc = calculoPcc;
  }

  public Long getQtdQuintos() {
    return qtdQuintos;
  }

  public void setQtdQuintos(Long qtdQuintos) {
    this.qtdQuintos = qtdQuintos;
  }

  public String getLeiIncorporacao() {
    return leiIncorporacao;
  }

  public void setLeiIncorporacao(String leiIncorporacao) {
    this.leiIncorporacao = leiIncorporacao;
  }

  public boolean isPonto() {
    return ponto;
  }

  public void setPonto(boolean ponto) {
    this.ponto = ponto;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public boolean isAtipoFp() {
    return atipoFp;
  }

  public void setAtipoFp(boolean atipoFp) {
    this.atipoFp = atipoFp;
  }

  public boolean isIRRF() {
    return IRRF;
  }

  public void setIRRF(boolean iRRF) {
    IRRF = iRRF;
  }

  public boolean getSupSecIntegral() {
    return supSecIntegral;
  }

  public void setSupSecIntegral(boolean supSecIntegral) {
    this.supSecIntegral = supSecIntegral;
  }

  public Long getProporcionalidade() {
    return proporcionalidade;
  }

  public void setProporcionalidade(Long proporcionalidade) {
    this.proporcionalidade = proporcionalidade;
  }

  public BigDecimal getSalarioOrigem() {
    return salarioOrigem;
  }

  public void setSalarioOrigem(BigDecimal salarioOrigem) {
    this.salarioOrigem = salarioOrigem;
  }

  public String getSalarioOrigemMonetario() {
    if (getSalarioOrigem() != null) {
      return salarioOrigemMonetario = getSalarioOrigem().toString();
    }
    return salarioOrigemMonetario;
  }

  public void setSalarioOrigemMonetario(String salarioOrigemMonetario) {
    this.salarioOrigemMonetario = salarioOrigemMonetario;
    if (((salarioOrigemMonetario != null && !salarioOrigemMonetario.equals("")))) {
      setSalarioOrigem(SRHUtils.valorMonetarioStringParaBigDecimal(salarioOrigemMonetario));
    }
  }

  public boolean getAbonoPrevidenciario() {
    return abonoPrevidenciario;
  }

  public void setAbonoPrevidenciario(boolean abonoPrevidenciario) {
    this.abonoPrevidenciario = abonoPrevidenciario;
  }

  public Date getPosse() {
    return posse;
  }

  public void setPosse(Date posse) {
    this.posse = posse;
  }

  public Date getExercicio() {
    return exercicio;
  }

  public void setExercicio(Date exercicio) {
    this.exercicio = exercicio;
  }

  public String getExercicioComoString() {
    return SRHUtils.formataData(SRHUtils.FORMATO_DATA, exercicio);
  }


  public Date getSaida() {
    return saida;
  }

  public void setSaida(Date saida) {
    this.saida = saida;
  }

  public Date getDoeNomeacao() {
    return doeNomeacao;
  }

  public void setDoeNomeacao(Date doeNomeacao) {
    this.doeNomeacao = doeNomeacao;
  }

  public Date getDoeSaida() {
    return doeSaida;
  }

  public void setDoeSaida(Date doeSaida) {
    this.doeSaida = doeSaida;
  }

  public String getDescricaoNomeacao() {
    return descricaoNomeacao;
  }

  public void setDescricaoNomeacao(String descricaoNomeacao) {
    this.descricaoNomeacao = descricaoNomeacao;
  }

  public String getDescricaoSaida() {
    return descricaoSaida;
  }

  public void setDescricaoSaida(String descricaoSaida) {
    this.descricaoSaida = descricaoSaida;
  }

  public Long getPrevidencia() {
    return previdencia;
  }

  public void setPrevidencia(Long previdencia) {
    this.previdencia = previdencia;
  }

  public Long getRegime() {
    return regime;
  }

  public void setRegime(Long regime) {
    this.regime = regime;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Long getQtdeDias() {
    return qtdeDias;
  }

  public void setQtdeDias(Long qtdeDias) {
    if (qtdeDias >= 0)
      this.qtdeDias = qtdeDias;
  }

  public Long getIdRepresentacaoCargo() {
    return idRepresentacaoCargo;
  }

  public void setIdRepresentacaoCargo(Long idRepresentacaoCargo) {
    this.idRepresentacaoCargo = idRepresentacaoCargo;
  }

  public Long getIdSetorDesignado() {
    return idSetorDesignado;
  }

  public void setIdSetorDesignado(Long idSetorDesignado) {
    this.idSetorDesignado = idSetorDesignado;
  }

  public boolean isAtivoPortal() {
    return ativoPortal;
  }

  public void setAtivoPortal(boolean ativoPortal) {
    this.ativoPortal = ativoPortal;
  }

  public int getAnos() {
    return anos;
  }

  public void setAnos(int anos) {
    if (anos >= 0)
      this.anos = anos;
  }

  public int getMeses() {
    return meses;
  }

  public void setMeses(int meses) {
    if (meses >= 0)
      this.meses = meses;
  }

  public int getDias() {
    return dias;
  }

  public void setDias(int dias) {
    if (dias >= 0)
      this.dias = dias;
  }

  public Aposentadoria getAposentadoria() {
    return aposentadoria;
  }

  public void setAposentadoria(Aposentadoria aposentadoria) {
    this.aposentadoria = aposentadoria;
  }

  public CodigoCategoria getCodigoCategoria() {
    return codigoCategoria;
  }

  public void setCodigoCategoria(CodigoCategoria codigoCategoria) {
    this.codigoCategoria = codigoCategoria;
  }

  public boolean isProvenienteDoTCM() {
    return this.tipoMovimentoEntrada != null && this.tipoMovimentoEntrada.getId().longValue() == 43L;
  }

  public boolean isEnquadramento() {
    return this.tipoMovimentoEntrada != null && this.tipoMovimentoEntrada.getId().longValue() == 11L;
  }

  public boolean isAposentado() {
    return StatusFuncional.INATIVO.getId().equals(this.status) || (this.tipoMovimentoSaida != null && tipoMovimentoSaida.getId() == TipoMovimento.APOSENTADORIA)
                              || (this.aposentadoria != null && this.aposentadoria.getId() > 0);
  }

  public PessoaJuridica getPessoaJuridica() {
    return pessoaJuridica;
  }

  public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
    this.pessoaJuridica = pessoaJuridica;
  }

  public boolean getRegimeTrabalhistaIsnull() {
    if (getRegime() == null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  public boolean getRegimePrevidenciarioIsnull() {
    if (getPrevidencia() == null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }
}
