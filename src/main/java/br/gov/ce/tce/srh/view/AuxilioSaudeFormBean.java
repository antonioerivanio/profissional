package br.gov.ce.tce.srh.view;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeBaseCalculo;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoItem;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.ExibeCampoFormAuxilioSaude;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.TipodeEmpresa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.exception.UsuarioException;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.FileUtils;
import br.gov.ce.tce.srh.util.RelatorioUtil;


@Component("auxilioSaudeFormBean")
@Scope("view")
public class AuxilioSaudeFormBean extends ControllerViewBase<AuxilioSaudeRequisicao> implements Serializable {

  private static final long serialVersionUID = 1L;

  static Logger logger = Logger.getLogger(AuxilioSaudeFormBean.class);

  private boolean exibirCamposDependente = Boolean.FALSE;
  int contadorBeneficiario = 1;
  int contadorDependente = 1;

  @Autowired
  private RelatorioUtil relatorioUtil;

  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;

  private static final String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private ExibeCampoFormAuxilioSaude exibeCampoFormAuxilioSaude;

  private List<PessoaJuridica> comboEmpresasCadastradas;

  private AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc;

  private ArquivoVO arquivoVO;

  private List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeDocBeneficiarioTempList;
  private List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeDocDependenteTempList;
  private List<AuxilioSaudeRequisicaoItem> auxilioSaudeRequisicaoItemsDeletadoList;
  private List<AuxilioSaudeRequisicaoDependente> auxilioSaudeRequisicaDependesDeletadoList;
  private AuxilioSaudeRequisicaoItem auxilioSaudeRequisicaoItem;
  private AuxilioSaudeRequisicaoDependente auxilioSaudeRequisicaoDependente;
  private List<Funcional> servidorEnvioList;


  @PostConstruct
  private void init() {
    try {

      if (FacesUtil.getFlashParameter(ENTIDADE) instanceof AuxilioSaudeRequisicao) {
        inicializarDadosParaEdicao();
        isEdicao = true;
        consultar();
      } else {
        inicializar();
      }

      exibirTableAuxilioSaudeBase();
    } catch (UsuarioException e) {
      logger.fatal(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.fatal(e.getMessage());
    }
  }

  private void inicializarDadosParaEdicao() {
    // carregar todos os servidor do tce, excluido os estágios    
    setServidorEnvioList(funcionalService.findServidoresEventoAuxilioSaude());
    AuxilioSaudeRequisicao entidadeEditar = entidadeService.getAuxilioSaudePorId((AuxilioSaudeRequisicao) FacesUtil.getFlashParameter("entidade"));
    entidadeEditar.setAuxilioSaudeRequisicaoDependenteList(entidadeService.getAuxilioSaudeDependenteList(entidadeEditar.getId()));
    entidadeService.setValorSolicitado(entidadeEditar);
    entidadeEditar.setAuxilioSaudeRequisicaoItem(new AuxilioSaudeRequisicaoItem());
    setEntidade(entidadeEditar);
    validarValorTotalSolicitacao();
  }

  private void inicializar() throws Exception {
    // carregar todos os servidor do tce, excluindo os estágios
    setServidorEnvioList(funcionalService.findServidoresEventoAuxilioSaude());

    createInstanceEntidade();
    getEntidade().setAuxilioSaudeRequisicaoItem(new AuxilioSaudeRequisicaoItem());
    getEntidade().setUsuario(loginBean.getUsuarioLogado());
    entidadeService.setDadosIniciaisDaEntidadePorCpf(getEntidade(), getEntidade().getUsuario().getCpf());
    getEntidade().setValorMaximoAserRestituido(0.0);
    exibirTableAuxilioSaudeBase();
    consultar();
  }

  @Override
  public void consultar() {
    try {
      entidadeService.setDadosIniciaisDaEntidade(getEntidade());
    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
    } catch (Exception e) {

      FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
      logger.fatal(e.getMessage());
    }
  }


  /***
   * salvar ou alterar dados
   */
  @Override
  public void salvar() {
    try {
      if (isEdicao) {
        adicionarDadosDeletadosList();

        entidadeService.atualizar(getEntidade());

        FacesUtil.addInfoMessage("Registro atualizado com sucesso!");
      } else {
        if (entidadeService.isOK(getEntidade())) {
          entidadeService.salvar(getEntidade());

          isRegistroSalvo = Boolean.TRUE;
          FacesUtil.addInfoMessage("Registro salvo com sucesso!");
        }
      }

      if (!isAnalista()) {
        inicializar();
      }
    } catch (NullPointerException e) {
      getEntidade().setDataFimRequisicao(null);
      //e.printStackTrace();
      FacesUtil.addErroMessage(e.getMessage());
      logger.error(e);
    } catch (Exception e) {      
      //e.printStackTrace();
      FacesUtil.addErroMessage("Ops! Não foi possível salvar a requisição, Por gentileza entre em contato o setor responsável");
      logger.error(e);
    }
  }

  @Override
  public void salvar(boolean finalizar) {

  }

  public void deferir(boolean deferido) {
    logger.info("Iniciando o deferimento dos dados!");
    try {

      if (deferido) {
        isEdicao = Boolean.TRUE;
        
        if (isValorSolicitadoMaiorValorAserRestituido()) {
          double valorSolicitado = getEntidade().getValorMaximoAserRestituido();
          getEntidade().setValorTotalSolicitado(valorSolicitado);
        }

        getEntidade().setDataFimRequisicao(new Date());
        getEntidade().setStatusAprovacao(AuxilioSaudeRequisicao.DEFERIDO);

      } else {
        getEntidade().setDataFimRequisicao(new Date());

        getEntidade().setStatusAprovacao(AuxilioSaudeRequisicao.INDEFERIDO);
      }

      salvar();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro ao salvar o deferimentos dados dados");
      logger.error(e.getMessage());
    }
  }

  private boolean isValorSolicitadoMaiorValorAserRestituido() {
    return getEntidade().getValorTotalSolicitado() > getEntidade().getValorMaximoAserRestituido();
  }


  private void exibirTableAuxilioSaudeBase() {
    getEntidade().setAuxilioSaudeBaseCalculo(new AuxilioSaudeBaseCalculo());
    getEntidade().getAuxilioSaudeBaseCalculo().gerarTabelaAuxilioSaudeBase();
  }

  public PessoaJuridica getPessoaJuridicaPorId(AuxilioSaudeRequisicaoItem bean) {
    return entidadeService.getPessoaJuridicaPorId(bean.getPessoaJuridica(), comboEmpresasCadastradas);
  }


  private void checkAnexoBeneficiarioIsNull() {
    if (auxilioSaudeDocBeneficiarioTempList == null) {
      throw new NullPointerException("Ops!, Precisa adiconar um anexo para continuar!");
    } else {
      if (auxilioSaudeDocBeneficiarioTempList.isEmpty()) {
        throw new NullPointerException("Ops!, Precisa adiconar um anexo para continuar!");
      }
    }
  }

  private void adicionarDadosDeletadosList() {
    getEntidade().setDataAlteracao(new Date());

    /*** atualizar a flag de deletado no banco **/
    if (auxilioSaudeRequisicaoItemsDeletadoList != null) {
      for (AuxilioSaudeRequisicaoItem bean : auxilioSaudeRequisicaoItemsDeletadoList) {
        getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList().add(bean);
      }
    }

    if (auxilioSaudeRequisicaDependesDeletadoList != null) {
      for (AuxilioSaudeRequisicaoDependente bean : auxilioSaudeRequisicaDependesDeletadoList) {
        getEntidade().getAuxilioSaudeRequisicaoDependenteList().add(bean);
      }
    }

  }

  /***
   * Metodo procura o item da lista de documentos, cria o diretorio se não existir e Faz o upload do
   * arquivo
   * 
   * @param isBeneficiario
   * @throws Exception
   */
  private void fazerUploadArquivos(boolean isBeneficiario) throws Exception {
    if (isBeneficiario) {
      criarDiretorioEfazerUploadArquivoBeneficiarioList();
    } else {
      criarDiretorioEfazerUploadArquivoDependenteList();
    }
  }

  private void criarDiretorioEfazerUploadArquivoBeneficiarioList() throws Exception {
    for (AuxilioSaudeRequisicaoItem bean : getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
      if (bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null) {
        throw new NullPointerException("Ops!, O anexo não foi encontrado!");
      }

      for (AuxilioSaudeRequisicaoDocumento beanDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        if (beanDoc.getId() == null) {
          criarDiretorio(beanDoc);
          salvarArquivoDiretorio(beanDoc);
        }
      }
    }

  }

  private void criarDiretorioEfazerUploadArquivoDependenteList() throws Exception {
    for (AuxilioSaudeRequisicaoDependente bean : getEntidade().getAuxilioSaudeRequisicaoDependenteList()) {
      if (bean.getAuxilioSaudeRequisicaoDocumentoList() == null) {
        throw new NullPointerException("Ops!, O anexo não foi encontrado!");
      }

      for (AuxilioSaudeRequisicaoDocumento beanDoc : bean.getAuxilioSaudeRequisicaoDocumentoList()) {
        if (beanDoc.getId() == null) {
          criarDiretorio(beanDoc);
          salvarArquivoDiretorio(beanDoc);
        }
      }

    }
  }

  private void criarDiretorio(AuxilioSaudeRequisicaoDocumento doc) throws Exception {
    FileUtils.criarDiretorio(doc.getCaminhoArquivo());
  }

  private void salvarArquivoDiretorio(AuxilioSaudeRequisicaoDocumento doc) throws Exception {
    FileUtils.upload(doc.getCaminhoCompleto(), doc.getArquivoVO().getComprovante());
  }

  private void checkPessoaJuridicaIsNull(AuxilioSaudeRequisicaoItem bean) {
    if (bean.getPessoaJuridica() == null) {
      throw new NullPointerException("Ops!. Por favor escolha a empresa do plano de saúde para continuar!");
    }
  }

  private void checkListaAnexoDependenteIsNull() {
    if (auxilioSaudeDocDependenteTempList == null) {
      throw new NullPointerException("Ops!. Por favor adicionar o anexo do dependente para continuar!");
    } else {
      if (auxilioSaudeDocDependenteTempList.isEmpty()) {
        throw new NullPointerException("Ops!. Por favor adicionar o anexo do dependente para continuar!");
      }
    }
  }

  private void checkDepedenteSeleciona() {
    if (getEntidade().getDependenteSelecionado() == null) {
      throw new NullPointerException("Ops!. Por favor escolha um dependente para continuar!");
    }
  }

  /***
   * Metodo procura o dependente que usuario selecionou na lista
   * 
   * @return depentende encontrado
   */
  private Dependente getDependenteSelecionado() {
    try {
      return entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(), getEntidade().getDependentesComboList());
    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
    }

    return null;
  }


  public List<PessoaJuridica> getComboEmpresasCadastradas() {

    try {

      if (isListaEmpresaJuridicaNull())
        this.comboEmpresasCadastradas = pessoaJuridicaService.findAllByTipo(TipodeEmpresa.PLANOS_SAUDE);

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o campo ComboEmpresasCadastrada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

    return this.comboEmpresasCadastradas;
  }

  /***
   * Verificar se lista de empresa é nula return true se a lista for nula e false caso contrario
   */
  private boolean isListaEmpresaJuridicaNull() {
    if (this.comboEmpresasCadastradas == null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  /***
   * Salvar o arquivo para comprovar os gastos com o auxilio saude
   * 
   * @param event
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public void uploadComprovanteBeneficario(FileUploadEvent event) {
    try {
      auxilioSaudeDocBeneficiarioTempList = new ArrayList<AuxilioSaudeRequisicaoDocumento>();
      this.auxilioSaudeRequisicaoItem = new AuxilioSaudeRequisicaoItem();

      UploadedFile comprovante = event.getUploadedFile();

      arquivoVO = getInstanciaArquivoVO(AuxilioSaudeRequisicaoDocumento.NOME_ARQUIVO_BENEFICIARIO, comprovante.getName(), comprovante.getData(), contadorBeneficiario);
      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(arquivoVO, new Date());

      auxSaudeRequisicaoDoc.setAuxilioSaudeRequisicaoItem(auxilioSaudeRequisicaoItem);
      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());
      auxilioSaudeDocBeneficiarioTempList.add(auxSaudeRequisicaoDoc);

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

  }

  public void uploadComprovanteDependente(FileUploadEvent event) {
    try {
      auxilioSaudeDocDependenteTempList = new ArrayList<AuxilioSaudeRequisicaoDocumento>();
      auxilioSaudeRequisicaoDependente = new AuxilioSaudeRequisicaoDependente();

      UploadedFile comprovante = event.getUploadedFile();

      arquivoVO = getInstanciaArquivoVO(AuxilioSaudeRequisicaoDocumento.NOME_ARQUIVO_DEPENDENTE, comprovante.getName(), comprovante.getData(), contadorDependente);

      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(arquivoVO, new Date());
      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());

      auxilioSaudeRequisicaoDependente.setAuxilioSaudeRequisicaoDocumento(auxSaudeRequisicaoDoc);
      auxSaudeRequisicaoDoc.setAuxilioSaudeRequisicaoDependente(auxilioSaudeRequisicaoDependente);
      auxilioSaudeDocDependenteTempList.add(auxSaudeRequisicaoDoc);
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

  }


  /***
   * metodo adiciona os dados do titular e validade se este é dependente, caso seja dependente o
   * metodo para cadastro de dependentes é chamado
   * 
   * @param beanEntidade
   * @param isTitular
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public void adicionar(AuxilioSaudeRequisicaoItem bean, boolean isBeneficiario) {
    try {
      if (isBeneficiario) {
        adicionarDadosBeneficiario(bean);
      } else {
        adicionarDadosDependente(bean);
      }

      entidadeService.setValorSolicitado(getEntidade());
      entidadeService.setValorMaximoSolicitadoPorIdade(getEntidade());
      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento();

      validarValorTotalSolicitacao();

      fazerUploadArquivos(isBeneficiario);

    } catch (Exception e) {
      FacesUtil.addErroMessage(e.getMessage());
    }
  }

  public void adicionarDadosDependente(AuxilioSaudeRequisicaoItem bean) {
    checkDepedenteSeleciona();
    checkListaAnexoDependenteIsNull();

    PessoaJuridica pessoaJuridica = getPessoaJuridicaPorId(bean);
    /*** adicionar os dependentes na lista */
    auxilioSaudeRequisicaoDependente.setAuxilioSaudeRequisicao(getEntidade());
    auxilioSaudeRequisicaoDependente.setDependente(getDependenteSelecionado());
    auxilioSaudeRequisicaoDependente.setPessoaJuridica(pessoaJuridica);
    auxilioSaudeRequisicaoDependente.setValorGastoPlanoSaude(bean.getValorGastoPlanoSaude());
    auxilioSaudeRequisicaoDependente.setArquivoVO(arquivoVO);
    auxilioSaudeRequisicaoDependente.setAuxilioSaudeRequisicaoDocumentoList(auxilioSaudeDocDependenteTempList);
    getEntidade().adicionarDadosDependenteList(auxilioSaudeRequisicaoDependente);

    getEntidade().setAuxilioSaudeRequisicaoItem(new AuxilioSaudeRequisicaoItem());
    getEntidade().setDependenteSelecionado(null);
    contadorDependente++;
  }

  private void adicionarDadosBeneficiario(AuxilioSaudeRequisicaoItem bean) {
    if (bean.getValorGastoPlanoSaude() == null) {
      throw new NullPointerException("Ops!, Precisa adiconar o valor mensal para continuar!");
    }
    checkPessoaJuridicaIsNull(bean);
    checkAnexoBeneficiarioIsNull();

    PessoaJuridica pessoaJuridica = getPessoaJuridicaPorId(bean);

    this.auxilioSaudeRequisicaoItem.setAuxilioSaudeRequisicao(getEntidade());
    this.auxilioSaudeRequisicaoItem.setPessoaJuridica(pessoaJuridica);
    this.auxilioSaudeRequisicaoItem.setValorGastoPlanoSaude(bean.getValorGastoPlanoSaude());
    this.auxilioSaudeRequisicaoItem.setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(auxilioSaudeDocBeneficiarioTempList);
    getEntidade().adicionarDadosRequisicaoList(this.auxilioSaudeRequisicaoItem);

    // limpar
    getEntidade().setAuxilioSaudeRequisicaoItem(new AuxilioSaudeRequisicaoItem());
    contadorBeneficiario++;
  }


  private ArquivoVO getInstanciaArquivoVO(String nome, String descricao, byte[] comprovante, int contador) {
    return new ArquivoVO(nome, descricao, ArquivoVO.CAMINHO_PARA_SALVAR_ARQUIVO, comprovante, contador);
  }


  /**
   * deletar os registro da lista e arquivo
   * 
   * @param bean
   */
  public void deletar(AuxilioSaudeRequisicaoItem bean) {
    if (getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList() == null) {
      throw new NullPointerException("Ops!. A lista de documentos do beneficiário está vazia!");
    }


    for (AuxilioSaudeRequisicaoDocumento auxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      if (auxDoc.getAuxilioSaudeRequisicaoItem().equals(bean)) {
        if (auxDoc.getId() == null) {
          FileUtils.removerArquivo(auxDoc.getCaminhoCompleto());
        }
      }
    }

    if (bean.getId() != null) {
      bean.setDataDelete(new Date());
      bean.setFlgDeletado(Boolean.TRUE);

      if (auxilioSaudeRequisicaoItemsDeletadoList == null) {
        auxilioSaudeRequisicaoItemsDeletadoList = new ArrayList<AuxilioSaudeRequisicaoItem>();
      }

      auxilioSaudeRequisicaoItemsDeletadoList.add(bean);

      getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList().remove(bean);
    } else {
      getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList().remove(bean);
    }
    entidadeService.setValorSolicitado(getEntidade());
    FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
  }

  /**
   * deletar os registro da lista e arquivo
   * 
   * @param bean
   */
  public void deletarDependente(AuxilioSaudeRequisicaoDependente bean) {
    if (bean.getAuxilioSaudeRequisicaoDocumentoList() == null) {
      throw new NullPointerException("Ops!. A lista de documentos do(s) dependente(s) está vazia!");
    }

    for (AuxilioSaudeRequisicaoDocumento auxDoc : bean.getAuxilioSaudeRequisicaoDocumentoList()) {
      if (auxDoc.getAuxilioSaudeRequisicaoDependente().equals(bean)) {
        if (auxDoc.getId() == null) {
          FileUtils.removerArquivo(auxDoc.getCaminhoCompleto());
        } else {
          auxDoc.setDataDelete(new Date());
          auxDoc.setDeletado(Boolean.TRUE);
        }
      }
    }
    if (bean.getId() != null) {

      bean.setFlgDeletado(Boolean.TRUE);

      if (auxilioSaudeRequisicaDependesDeletadoList == null) {
        auxilioSaudeRequisicaDependesDeletadoList = new ArrayList<AuxilioSaudeRequisicaoDependente>();
      }
      auxilioSaudeRequisicaDependesDeletadoList.add(bean);

      getEntidade().getAuxilioSaudeRequisicaoDependenteList().remove(bean);
    } else {
      getEntidade().getAuxilioSaudeRequisicaoDependenteList().remove(bean);

    }
    entidadeService.setValorSolicitado(getEntidade());
    FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
  }

  /***
   * Procurar anexo na lista e visualizar
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoBeneficiario(AuxilioSaudeRequisicaoItem bean) {
    try {
      for (AuxilioSaudeRequisicaoDocumento auxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        if (auxDoc.getAuxilioSaudeRequisicaoItem().equals(bean)) {

          if (auxDoc.getCaminhoCompleto() == null) {
            throw new NullPointerException("Caminho do arquivo não encontrado!");
          }

          if (auxDoc.getId() != null) {
            FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
          }

          if (auxDoc.getAuxilioSaudeRequisicaoItem().getId() != null) {
            FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
          } else {
            FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
          }
        }
      }
    } catch (FileNotFoundException e) {
      logger.fatal(OCORREU_ERRO + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn(OCORREU_ERRO + e.getMessage());
    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }
  }


  /***
   * Procurar anexo na lista e visualizar
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoDependente(AuxilioSaudeRequisicaoDependente bean) {
    try {
      for (AuxilioSaudeRequisicaoDocumento auxDoc : bean.getAuxilioSaudeRequisicaoDocumentoList()) {
        if (auxDoc.getAuxilioSaudeRequisicaoDependente().equals(bean)) {
          FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
          break;
        }
      }
    } catch (FileNotFoundException e) {
      logger.fatal(OCORREU_ERRO + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn(OCORREU_ERRO + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }
  }

  public void exibirCamposFormChange(boolean isBeneficiario) {
    exibeCampoFormAuxilioSaude = new ExibeCampoFormAuxilioSaude();

    if (isBeneficiario)
      exibeCampoFormAuxilioSaude.setExibirCamposInputFormBeneficiario(isBeneficiario);

  }

  public ExibeCampoFormAuxilioSaude getExibeCampoFormAuxilioSaude() {
    return exibeCampoFormAuxilioSaude;
  }

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }


  public ArquivoVO getArquivoVO() {
    return arquivoVO;
  }

  public void setArquivoVO(ArquivoVO arquivoVO) {
    this.arquivoVO = arquivoVO;
  }

  public boolean getExibirCamposDependente() {
    if (getEntidade().getDependentesComboList() != null) {
      exibirCamposDependente = Boolean.TRUE;
    }
    return exibirCamposDependente;
  }

  public void setExibirCamposDependente(boolean exibirCamposDependente) {
    this.exibirCamposDependente = exibirCamposDependente;
  }

  /***
   * valida se o valor solicitado é menor que o valor a ser restituido   * 
   */
  public void validarValorTotalSolicitacao() {
    Double valorSolicitado = getEntidade().getValorTotalSolicitado();

    if (getEntidade().getFuncional() != null && getEntidade().getFuncional().getPessoal() != null) {
      entidadeService.setValorMaximoSolicitadoPorIdade(getEntidade());

      if (valorSolicitado < getEntidade().getValorMaximoAserRestituido()) {
        getEntidade().setValorMaximoAserRestituido(valorSolicitado);
      }
    }
  }


  public String voltar() {
    getEntidade().setDataInicioRequisicao(null);
    FacesUtil.setFlashParameter(ENTIDADE, getEntidade());
    return "/paginas/cadastros/auxilioSaudeList.xhtml?faces-redirect=true";
  }

  public AuxilioSaudeRequisicaoDocumento getAuxSaudeRequisicaoDoc() {
    return auxSaudeRequisicaoDoc;
  }

  public void setAuxSaudeRequisicaoDoc(AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc) {
    this.auxSaudeRequisicaoDoc = auxSaudeRequisicaoDoc;
  }

  public List<Funcional> getServidorEnvioList() {
    return servidorEnvioList;
  }

  public void setServidorEnvioList(List<Funcional> servidorEnvioList) {
    this.servidorEnvioList = servidorEnvioList;
  }
}
