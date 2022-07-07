package br.gov.ce.tce.srh.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDAO;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase.FlagAtivo;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.BaseCalculoValorRestituido;
import br.gov.ce.tce.srh.exception.UsuarioException;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;

@Service("auxilioSaudeRequisicaoService")
public class AuxilioSaudeRequisicaoServiceImp implements AuxilioSaudeRequisicaoService {


  static Logger logger = Logger.getLogger(AuxilioSaudeRequisicaoServiceImp.class);

  @Autowired
  private AuxilioSaudeRequisicaoDAO dao;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  private DependenteService dependenteService;


  @Override
  public void salvar(AuxilioSaudeRequisicao bean) {
    dao.salvar(bean);
  }


  @Override
  @Transactional
  public void salvar(List<AuxilioSaudeRequisicao> beanList) {
    try {
      logger.info("Iniciando o salvamento do auxilio saude requisição");

      if (beanList != null && !beanList.isEmpty()) {
        for (AuxilioSaudeRequisicao bean : beanList) {
          dao.salvar(bean);

          salvarDependentes(bean);
          salvarDocumentosBeneficiario(bean);

          if (bean.getStatusAprovacao() != null && bean.getStatusAprovacao().equals(AuxilioSaudeRequisicao.DEFERIDO)) {
            atualizarDadosTabelaAuxilioSaudeBase(bean);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erro ao salvar o auxilio requisição : " + e.getMessage());
      System.out.println(e.getMessage());
    }
  }


  @Override
  @Transactional
  public void atualizar(AuxilioSaudeRequisicao bean) {
    try {
      logger.info("Iniciando o Edicao do auxilio saude requisição");

      if (bean.checkBeneficiarioItemListNotNull()) {
        for (AuxilioSaudeRequisicao beanAuxilio : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
          beanAuxilio.setDataAlteracao(new Date());

          if (beanAuxilio.getId() != null) {
            dao.atualizar(beanAuxilio);
          } else {
            dao.salvar(beanAuxilio);
          }

          salvarDependentes(beanAuxilio);
          salvarDocumentosBeneficiario(beanAuxilio);

          if (bean.getStatusAprovacao() != null && bean.getStatusAprovacao().equals(AuxilioSaudeRequisicao.DEFERIDO)) {
            atualizarDadosTabelaAuxilioSaudeBase(beanAuxilio);
          }
        }

      }

    } catch (Exception e) {
      logger.error("Erro ao atualizar o auxilio-saúde " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void atualizarDadosTabelaAuxilioSaudeBase(AuxilioSaudeRequisicao bean) {
    if (bean.getStatusAprovacao() != null) {
      Funcional funcional = dao.getEntityManager().find(Funcional.class, bean.getFuncional().getId());
      AuxilioSaudeRequisicaoBase auxilioSaudeRequisicaoBase = dao.getAuxilioSaudeBasePorPessoaIdeAtivo(funcional.getPessoal(), FlagAtivo.SIM);

      if (auxilioSaudeRequisicaoBase != null) {
        auxilioSaudeRequisicaoBase.setCustoPlanoBase(bean.getValorTotalSolicitado());
        auxilioSaudeRequisicaoBase.setDataAtualizacao(new Date());
        auxilioSaudeRequisicaoBase.setObservacao("Valor adicionar automaticamente a partir da tela do Auxilio-Saúde");
        dao.atualizar(auxilioSaudeRequisicaoBase);
      } else {

        auxilioSaudeRequisicaoBase = new AuxilioSaudeRequisicaoBase(funcional.getPessoal(), bean.getUsuario(), bean.getValorTotalSolicitado(),
                                  "Registro criado automaticamente a partir da tela do Auxilio-Saúde", new Date(), FlagAtivo.SIM, new Date());

        dao.salvar(auxilioSaudeRequisicaoBase);
      }
    }
  }

  @Override
  public void salvarDependentes(List<AuxilioSaudeRequisicao> beanList) {
    logger.info("Iniciando o salvamento dos dependentes do auxilio saude requisição");

    try {
      for (AuxilioSaudeRequisicao bean : beanList) {
        if (bean.getAuxilioSaudeRequisicaoDependenteList() != null && !bean.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {
          for (AuxilioSaudeRequisicaoDependente beanAuxDep : bean.getAuxilioSaudeRequisicaoDependenteList()) {

            if (beanAuxDep.getId() != null) {
              dao.atualizar(beanAuxDep);
            } else {
              beanAuxDep.setAuxilioSaudeRequisicao(bean);
              dao.salvar(beanAuxDep);
            }

            salvarDocumentosDependente(beanAuxDep);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Erro ao salvar os dependentes do auxilio requisição : " + e.getMessage());
    }
  }


  @Override
  public void salvarDependentes(AuxilioSaudeRequisicao bean) {
    logger.info("Iniciando o salvamento dos dependentes do auxilio saude requisição");

    try {
      if (bean.checkDependenteItemListNotNull()) {
        for (AuxilioSaudeRequisicaoDependente beanAuxDep : bean.getAuxilioSaudeRequisicaoDependenteList()) {

          if (beanAuxDep.getId() != null) {
            dao.atualizar(beanAuxDep);
          } else {
            beanAuxDep.setAuxilioSaudeRequisicao(bean);
            dao.salvar(beanAuxDep);
          }

          salvarDocumentosDependente(beanAuxDep);
        }
      }
    } catch (Exception e) {
      logger.error("Erro ao salvar os dependentes do auxilio requisição : " + e.getMessage());
    }
  }

  @Override
  public void salvarDocumentosBeneficiario(AuxilioSaudeRequisicao bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");
    if (bean.checkBeneficiarioItemListNotNull()) {
      for (AuxilioSaudeRequisicao auxilioSaudeRequisicao : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
        salvarDocumento(bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList(), auxilioSaudeRequisicao);
      }
    } else {
      salvarDocumento(bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList(), bean);
    }
  }

  private void salvarDocumento(List<AuxilioSaudeRequisicaoDocumento> beanList, AuxilioSaudeRequisicao bean) throws IOException {
    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : beanList) {
      beanAuxDoc.setAuxilioSaudeRequisicaoDependente(null);

      if (beanAuxDoc.getId() != null) {
        dao.atualizar(beanAuxDoc);
      } else {
        beanAuxDoc.setAuxilioSaudeRequisicao(bean);
        dao.salvar(beanAuxDoc);
      }

      fazerUploadArquivo(beanAuxDoc);
    }
  }

  @Override
  public void salvarDocumentosDependente(AuxilioSaudeRequisicaoDependente bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição dependente");
    if (bean.getAuxilioSaudeRequisicao().checkDependenteItemListNotNull()) {
      for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicao().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
        beanAuxDoc.setAuxilioSaudeRequisicao(null);

        if (beanAuxDoc.getId() != null) {
          dao.atualizar(beanAuxDoc);
        } else {
          beanAuxDoc.setAuxilioSaudeRequisicaoDependente(bean);
          dao.salvar(beanAuxDoc);
        }

        fazerUploadArquivo(beanAuxDoc);
      }
    }
  }

  /***
   * o sistema deverá salvar os arquivos no
   * caminho\anoAtual\matriculaColaborador\idSalvo_nomeArquvo_versao.pdf exemplo:
   * \svtcenas2\Desenvolvimento\svtcefs2\SRH\comprovanteAuxSaude\2022\1980\1_comprovante_1.pdf
   * 
   * @param bean
   * @throws IOException
   */
  public void fazerUploadArquivo(AuxilioSaudeRequisicaoDocumento bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude");

    Path arquivoSalvo = Paths.get(bean.getCaminhoArquivo() + File.separator + bean.getNomeArquivo());
    String novoNome = bean.getNomeArquivo();
    String[] nomeJaExisteNaPasta = bean.getNomeArquivo().split("_");

    /**
     * verificar se o nome comerça com digitos
     */
    if (!nomeJaExisteNaPasta[0].matches("-?\\d+")) {
      novoNome = bean.getId() + "_" + bean.getNomeArquivo();
      bean.setNomeArquivo(novoNome);
    }
    // renomear o arquivo dentro da pasta
    Files.move(arquivoSalvo, arquivoSalvo.resolveSibling(novoNome));
  }

  @Override
  public boolean isOK(AuxilioSaudeRequisicao bean) {
    Boolean isResultadoOk = Boolean.TRUE;

    if (bean.getFuncional() == null) {
      FacesUtil.addErroMessage("Você precisa selecionar o Beneficiário e clicar no Botão consulta");
      return false;
    }

    if (bean.getDependenteSelecionado() == null) {// não tem dependentes

      if (bean.checkBeneficiarioItemListNotNull()) {
        for (AuxilioSaudeRequisicao beanAux : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
          if (beanAux.getValorGastoPlanoSaude() == null) {
            FacesUtil.addErroMessage("O campo Valor Mensal é obrigatório.");
            return false;
          }

          if (beanAux.getPessoaJuridica() == null) {
            FacesUtil.addErroMessage("O campo Nome da Empresa de Saúde é obrigatório.");
            return false;
          }
        }
      }

      if (bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() != null && bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().isEmpty()) {
        FacesUtil.addErroMessage("O anexo é Obrigatório. Clique no botão Anexar para adicionar-lo antes de salvar");
        return false;
      }

    } else {// tem dependentes
      if (bean.getAuxilioSaudeRequisicaoDependenteList() != null && bean.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {

        FacesUtil.addErroMessage("O valor mensal e Nome da Empresa de Saúde deve ser adicionado");
        return false;
      }
    }

    if (bean.getObservacao() != null && !bean.getObservacao().isEmpty()) {
      if (bean.getObservacao().contains("...") || bean.getObservacao().length() <= 5) {
        FacesUtil.addErroMessage("Digite mais" + bean.getObservacao().length() + "caracteres para a observação");
        isResultadoOk = Boolean.FALSE;
      }
    }

    return isResultadoOk;
  }


  @Override
  public PessoaJuridica getPessoaJuridicaPorId(PessoaJuridica pj, List<PessoaJuridica> comboEmpresasCadastradas) {
    int index = comboEmpresasCadastradas.indexOf(pj);

    return comboEmpresasCadastradas.get(index);
  }

  /*** selecionar o dependente completo na lista e adicionar na entidade ***/
  @Override
  public Dependente getDependentePorId(Dependente dependente, List<Dependente> dependentesComboList) {
    if (dependentesComboList == null || dependentesComboList.isEmpty()) {
      throw new NullPointerException("Ops!. Por favor escolha um dependente para continuar");
    }

    int index = dependentesComboList.indexOf(dependente);
    return dependentesComboList.get(index);
  }


  @Override
  public void executarAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao, Boolean flgAfirmaSerVerdadeiraInformacao) {
    if (entidade.getId() == null) {
      if (entidade.checkBeneficiarioItemListNotNull()) {// um
                                                        // registro
        for (AuxilioSaudeRequisicao beanAux : entidade.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
          beanAux.setStatusFuncional(entidade.getStatusFuncional());
          beanAux.setDataInicioRequisicao(new Date());
          beanAux.setObservacao(entidade.getObservacao());
          beanAux.setFlAfirmaSerVerdadeiraInformacao(entidade.getFlAfirmaSerVerdadeiraInformacao());
          beanAux.setAuxilioSaudeRequisicaoDependenteList(entidade.getAuxilioSaudeRequisicaoDependenteList());
          beanAux.setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(entidade.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList());
          beanAux.setAuxilioSaudeRequisicaoDocumentoDependenteList(entidade.getAuxilioSaudeRequisicaoDocumentoDependenteList());
        }
      } else {// apenas um registro adicionar na lista
        entidade.setValorGastoPlanoSaude(null);
        entidade.setPessoaJuridica(null);
        entidade.setAuxilioSaudeRequisicaoBeneficiarioItemList(new ArrayList<AuxilioSaudeRequisicao>());
        entidade.getAuxilioSaudeRequisicaoBeneficiarioItemList().add(entidade);
      }
    }
  }


  @Override
  public void setDadosIniciaisDaEntidadePorCpf(AuxilioSaudeRequisicao entidade, String cpf) throws UsuarioException {
    try {
      entidade.setDataInicioRequisicao(new Date());
      Funcional funcional = getFuncionalPorCpf(cpf);

      List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());
      entidade.setFuncional(funcional);
      entidade.setDependentesComboList(dependenteList);
    } catch (Exception e) {
      logger.error("Usuario não encontrado");
      // throw new UsuarioException("Usuario não encontrado");
    }
  }

  @Override
  public Funcional getFuncionalPorCpf(String cpf) {
    String matColaborador = funcionalService.getMatriculaAndNomeByCpfAtiva(cpf).getMatricula();
    Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva(matColaborador);
    return funcional;
  }

  @Override
  public void setDadosIniciaisDaEntidade(AuxilioSaudeRequisicao entidade) {
    if (entidade.getFuncional() == null) {
      throw new NullPointerException("Ops!. Por favor escolha um servidor para continuar");
    }
    Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());

    List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());

    entidade.setDataInicioRequisicao(new Date());
    entidade.setFuncional(funcional);
    entidade.setStatusFuncional(funcional.getStatus() != null && funcional.getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO : AuxilioSaudeRequisicao.INATIVO);
    entidade.setDependentesComboList(dependenteList);
  }


  @Override
  public AuxilioSaudeRequisicao getAuxilioSaudePorId(AuxilioSaudeRequisicao obj) {
    return ((AuxilioSaudeRequisicao) dao.find(obj));
  }

  @Override
  public List<AuxilioSaudeRequisicaoDocumento> getListaArquivosPorIdAuxilio(BeanEntidade bean) {
    return dao.getListaAnexos(bean);
  }

  @Override
  public BigDecimal getValorSalarioComBaseIdadePorPercentual(Double percentual) {
    return dao.getValorSalarioComBaseIdadePorPercentual(percentual);
  }

  @Override
  public void setValorMaximoSolicitadoPorIdade(AuxilioSaudeRequisicao bean) {
    final int TRINTA_ANOS = 30;
    final int QUARENTA_ANOS = 40;
    final int CINQUENTA_ANOS = 50;
    final int SESSENTA_ANOS = 60;
    
    if (bean.getFuncional() != null && bean.getFuncional().getPessoal() != null) {
      int idade = bean.getFuncional().getPessoal().getIdade();

      if (idade <= TRINTA_ANOS) {        
        bean.setValorMaximoAserRestituido(BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_ATE_30ANOS.getValor());
      }
      if (idade > TRINTA_ANOS && idade <= QUARENTA_ANOS) {        
        bean.setValorMaximoAserRestituido(BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_31_ATE_40ANOS.getValor());
      }
      if (idade > QUARENTA_ANOS && idade <= CINQUENTA_ANOS) {        
        bean.setValorMaximoAserRestituido(BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_41_ATE_50ANOS.getValor());
      }
      if (idade > CINQUENTA_ANOS && idade <= SESSENTA_ANOS) {        
        bean.setValorMaximoAserRestituido(BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_51_ATE_60ANOS.getValor());
      }
      else {        
        bean.setValorMaximoAserRestituido(BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_SUPERIOR_60ANOS.getValor());
      }
    }
  }


  @Override
  public int count(AuxilioSaudeRequisicao bean) {
    return dao.count(bean);
  }

  @Override
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao bean, Integer first, Integer rows) {
    List<AuxilioSaudeRequisicao> beanList = dao.search(bean, first, rows);

    for (AuxilioSaudeRequisicao beanBeneficiario : beanList) {

      if (beanList != null && !beanList.isEmpty()) {
        List<AuxilioSaudeRequisicaoDependente> beanDependenteList = dao.getAuxilioSaudeRequisicaoDependentePorId(beanBeneficiario.getId());
        if (beanDependenteList != null && !beanDependenteList.isEmpty()) {
          Double valorPlanoBeneficiario = beanBeneficiario.getValorGastoPlanoSaude();
          Double valorPlanoSaudeDependente = getValorDependente(beanDependenteList);
          Double valorTotalSolicitado = somarValoBeneficiarioMaisDependente(valorPlanoBeneficiario, valorPlanoSaudeDependente);

          beanBeneficiario.setValorTotalSolicitado(valorTotalSolicitado);
        } else {
          beanBeneficiario.setValorTotalSolicitado(beanBeneficiario.getValorGastoPlanoSaude());
        }
      }
    }

    return beanList;
  }

  @Override
  public List<AuxilioSaudeRequisicaoDependente> getAuxilioSaudeDependenteList(Long id) {
    return dao.getAuxilioSaudeRequisicaoDependentePorId(id);
  }

  /***
   * somar o valor do beneficiario com o dependente
   * 
   * @param bean
   */
  public void setValorSolicitado(AuxilioSaudeRequisicao bean) {
    Double valor = 0.0;

    if (bean.checkBeneficiarioItemListNotNull()) {
      for (AuxilioSaudeRequisicao auxBenef : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
        if (bean.getValorTotalSolicitado() == null) {
          valor = auxBenef.getValorGastoPlanoSaude();
        } else {
          valor += auxBenef.getValorGastoPlanoSaude();
        }
      }

      bean.setValorTotalSolicitado(valor);
    }

    if (bean.checkDependenteItemListNotNull()) {
      for (AuxilioSaudeRequisicaoDependente auxDep : bean.getAuxilioSaudeRequisicaoDependenteList()) {
        if (bean.getValorTotalSolicitado() == null) {
          valor = auxDep.getValorGastoPlanoSaude();
        } else {
          valor += auxDep.getValorGastoPlanoSaude();
        }
      }

      bean.setValorTotalSolicitado(valor);
    }
  }

  private Double somarValoBeneficiarioMaisDependente(Double valorBeneficiario, Double valorDependente) {
    double valorBeneficiarioTemp = 0.0 ;
    double valorDependenteTemp = 0.0 ;
    if(valorBeneficiario != null) {
      valorBeneficiarioTemp = valorBeneficiario;
    }
    if(valorDependente != null) {
      valorDependenteTemp = valorDependente;
    }
    return valorBeneficiarioTemp + valorDependenteTemp;
  }


  private Double getValorDependente(List<AuxilioSaudeRequisicaoDependente> beanDepList) {
    Double valor = 0.0;

    for (AuxilioSaudeRequisicaoDependente auxDep : beanDepList) {
      valor += auxDep.getValorGastoPlanoSaude();
    }
    return valor;
  }
}

