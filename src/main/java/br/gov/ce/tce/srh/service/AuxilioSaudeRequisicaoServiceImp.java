package br.gov.ce.tce.srh.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import br.gov.ce.tce.srh.dao.DAO;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoBase.FlagAtivo;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoItem;
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
import br.gov.ce.tce.srh.util.SRHUtils;

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

  public static final Double TRES_POR_CENTO = 0.03;
  public static final Double TRES_PONTO_CINCO_POR_CENTO = 0.035;
  public static final Double QUATRO_POR_CENTO = 0.04;
  public static final Double QUATRO_PONTO_CINCO_POR_CENTO = 0.045;
  public static final Double CINCO_POR_CENTO = 0.05;


  @Override
  @Transactional
  public void salvar(AuxilioSaudeRequisicao bean) throws IOException {
    dao.salvar(bean);

    salvarOuAtualizarItems(bean.getAuxilioSaudeRequisicaoBeneficiarioItemList(), bean);
    salvarDependentes(bean);

    if (bean.getStatusAprovacao() != null && bean.getStatusAprovacao().equals(AuxilioSaudeRequisicao.DEFERIDO)) {
      atualizarDadosTabelaAuxilioSaudeBase(bean);
    }
  }


  @Override
  @Transactional
  public void salvar(List<AuxilioSaudeRequisicao> beanList) {
    try {
      logger.info("Iniciando o salvamento do auxilio saude requisição");

      if (beanList != null && !beanList.isEmpty()) {
        for (AuxilioSaudeRequisicao bean : beanList) {
          dao.salvar(bean);

          salvarOuAtualizarItems(bean.getAuxilioSaudeRequisicaoBeneficiarioItemList(), bean);
          salvarDependentes(bean);
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

      if (bean.getId() != null) {
        dao.atualizar(bean);
      } else {
        salvar(bean);
      }

      salvarOuAtualizarItems(bean.getAuxilioSaudeRequisicaoBeneficiarioItemList(), bean);
      salvarDependentes(bean);


    } catch (Exception e) {
      logger.error("Erro ao atualizar o auxilio-saúde " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void salvarOuAtualizarItems(List<AuxilioSaudeRequisicaoItem> beanAuxilioSaudeItems, AuxilioSaudeRequisicao bean) throws IOException {
    if (beanAuxilioSaudeItems != null) {
      for (AuxilioSaudeRequisicaoItem beanItem : beanAuxilioSaudeItems) {
        if (beanItem.getId() != null) {
          dao.atualizar(beanItem);
        } else {
          dao.salvar(beanItem);
        }

        salvarDocumentosBeneficiario(beanItem);
      }
    }
  }

  @Override
  public void atualizarDadosTabelaAuxilioSaudeBase(AuxilioSaudeRequisicao bean) {
    if (bean.getStatusAprovacao() != null) {
      Funcional funcional = dao.getEntityManager().find(Funcional.class, bean.getFuncional().getId());
      AuxilioSaudeRequisicaoBase auxilioSaudeRequisicaoBase = dao.getAuxilioSaudeBasePorPessoaIdeAtivo(funcional.getPessoal(), FlagAtivo.SIM);

      if (auxilioSaudeRequisicaoBase != null) {
        auxilioSaudeRequisicaoBase.setCustoPlanoBase(bean.getValorTotalSolicitado());
        auxilioSaudeRequisicaoBase.setCustoAdicional(0.0);
        auxilioSaudeRequisicaoBase.setDataAtualizacao(new Date());
        auxilioSaudeRequisicaoBase.setObservacao("Valor adicionar automaticamente a partir da tela do Auxilio-Saúde");
        dao.atualizar(auxilioSaudeRequisicaoBase);
      } else {

        auxilioSaudeRequisicaoBase = new AuxilioSaudeRequisicaoBase(funcional.getPessoal(), bean.getUsuario(), bean.getValorTotalSolicitado(),
                                  "Registro criado automaticamente a partir da tela do Auxilio-Saúde", new Date(), FlagAtivo.SIM, new Date(), 0.0);
        dao.salvar(auxilioSaudeRequisicaoBase);
        dao.getEntityManager().flush();
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
  public void salvarDocumentosBeneficiario(AuxilioSaudeRequisicaoItem bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");

    if (bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() != null) {
      for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        beanAuxDoc.setAuxilioSaudeRequisicaoDependente(null);

        if (beanAuxDoc.getId() != null) {
          dao.atualizar(beanAuxDoc);
        } else {
          dao.salvar(beanAuxDoc);
        }

        fazerUploadArquivo(beanAuxDoc);
      }

    }
  }

  @Override
  public void salvarDocumentosDependente(AuxilioSaudeRequisicaoDependente bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição dependente");
    if (bean.getAuxilioSaudeRequisicao().checkDependenteItemListNotNull()) {
      for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoList()) {
        beanAuxDoc.setAuxilioSaudeRequisicaoItem(null);

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
      throw new NullPointerException("OPs!.Selecionar o Beneficiário e clicar no botão consulta");
    }

    if (bean.getAuxilioSaudeRequisicaoBeneficiarioItemList() == null && bean.getAuxilioSaudeRequisicaoDependenteList() == null) {
      FacesUtil.addErroMessage("Você deve Clicar no botão adicionar para incluir os dados na lista.");
      throw new NullPointerException("Lista de dados do benficário ou dos dependentes estão vazias.");
    }

    if (!bean.getFlAfirmaSerVerdadeiraInformacao()) {
      FacesUtil.addErroMessage("form:campoConcordo", "Campo obrigatório!");
      throw new NullPointerException("OPs!. O campo Concordo é obrigatório");
    }

    if (bean.getObservacao() != null && !bean.getObservacao().isEmpty()) {
      if (bean.getObservacao().contains("xxx") || bean.getObservacao().contains("...") || bean.getObservacao().length() < 5) {
        throw new NullPointerException("Digite mais" + bean.getObservacao().length() + "caracteres para a observação");
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
      throw new NullPointerException("Vamos iniciar!. Escolha um servidor e clique em consultar para continuar");
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
    AuxilioSaudeRequisicao auxilioSaudeRequisicao = ((AuxilioSaudeRequisicao) dao.find(obj));

    List<AuxilioSaudeRequisicaoItem> itemsList = dao.getListaAuxilioSaudeItems(auxilioSaudeRequisicao);

    if (itemsList != null && !itemsList.isEmpty()) {
      for (AuxilioSaudeRequisicaoItem beanItem : itemsList) {
        List<AuxilioSaudeRequisicaoDocumento> doclist = dao.getListaAnexos(beanItem);
        beanItem.setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(doclist);
      }
    }
    auxilioSaudeRequisicao.setAuxilioSaudeRequisicaoBeneficiarioItemList(itemsList);
    return auxilioSaudeRequisicao;
  }

  @Override
  public List<AuxilioSaudeRequisicaoDocumento> getListaArquivosPorIdAuxilio(BeanEntidade bean) {
    return dao.getListaAnexos(bean);
  }


  @Override
  public BigDecimal getValorSalarioComBaseIdadePorPercentual(Double percentual) {
    return dao.getValorSalarioComBaseIdadePorPercentual(percentual);
  }

  /****
   * valida se o percentual a restitui de acordo com a idade, se o Servidor do TCE fizer aniversario
   * no mesmo mes, no qual ele esta solicitanto o auxilio-saude, o percentual maior será acrescentado
   * no mês posterior ao de seu aniversario. exe: se ele aniversario no mês 07/2022 e fez tem 41 anos,
   * ele vai receber o valor equilente ao a seus 40 anos ou ser 3% no mes 08/2020 ele vai receber o
   * valor equivalente a 3,5%
   */
  @Override
  public void setValorMaximoSolicitadoPorIdade(AuxilioSaudeRequisicao bean) {
    final int TRINTA_ANOS = 30;
    final int QUARENTA_ANOS = 40;
    final int CINQUENTA_ANOS = 50;
    final int SESSENTA_ANOS = 60;

    if (bean.getFuncional() != null && bean.getFuncional().getPessoal() != null) {
      int idade = bean.getFuncional().getPessoal().getIdade();

      LocalDate dataNascimento = SRHUtils.convertToLocalDateViaInstant(bean.getFuncional().getPessoal().getNascimento());
      boolean isMesAniversario = SRHUtils.isMesAniversarioEdataMenorQueDataAtual(dataNascimento.getDayOfMonth(), dataNascimento.getMonthValue(), dataNascimento.getYear());
      double valorMaximoTresPorCento = 0.0;

      if (idade <= TRINTA_ANOS) {
        valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(TRES_POR_CENTO).doubleValue();
        bean.setValorMaximoAserRestituido(valorMaximoTresPorCento);
      } else if ((idade > TRINTA_ANOS && idade <= QUARENTA_ANOS)) {
        if (isMesAniversario) {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(TRES_POR_CENTO).doubleValue();
        } else {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(TRES_PONTO_CINCO_POR_CENTO).doubleValue();
        }

        bean.setValorMaximoAserRestituido(valorMaximoTresPorCento);
      } else if (idade > QUARENTA_ANOS && idade <= CINQUENTA_ANOS) {
        if (isMesAniversario) {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(TRES_PONTO_CINCO_POR_CENTO).doubleValue();
        } else {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(QUATRO_POR_CENTO).doubleValue();
        }

        bean.setValorMaximoAserRestituido(valorMaximoTresPorCento);
      } else if (idade > CINQUENTA_ANOS && idade <= SESSENTA_ANOS) {
        if (isMesAniversario) {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(QUATRO_POR_CENTO).doubleValue();

        } else {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(QUATRO_PONTO_CINCO_POR_CENTO).doubleValue();
        }

        bean.setValorMaximoAserRestituido(valorMaximoTresPorCento);
      } else {
        if (isMesAniversario) {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(QUATRO_PONTO_CINCO_POR_CENTO).doubleValue();
        } else {
          valorMaximoTresPorCento = dao.getValorSalarioComBaseIdadePorPercentual(CINCO_POR_CENTO).doubleValue();
        }

        bean.setValorMaximoAserRestituido(valorMaximoTresPorCento);
      }
    }
  }


  @Override
  public int count(AuxilioSaudeRequisicao bean) {
    return dao.count(bean);
  }

  @Override
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao bean, Integer first, Integer rows) {

    List<AuxilioSaudeRequisicao> beanAuxilioSaudeList = dao.search(bean, first, rows);
    Double valorPlanoBeneficiario = 0.0;

    if (beanAuxilioSaudeList != null && !beanAuxilioSaudeList.isEmpty()) {
      for (AuxilioSaudeRequisicao beanAuxilioSaude : beanAuxilioSaudeList) {
        List<AuxilioSaudeRequisicaoItem> auxilioSaudeRequisicaoItemList = dao.getListaAuxilioSaudeItems(beanAuxilioSaude);
        for (AuxilioSaudeRequisicaoItem beanItem : auxilioSaudeRequisicaoItemList) {
          List<AuxilioSaudeRequisicaoDependente> beanDependenteList = dao.getAuxilioSaudeRequisicaoDependentePorId(beanItem.getId());

          if (beanDependenteList != null && !beanDependenteList.isEmpty()) {
            valorPlanoBeneficiario = beanItem.getValorGastoPlanoSaude();
            Double valorPlanoSaudeDependente = getValorDependente(beanDependenteList);
            Double valorTotalSolicitado = somarValoBeneficiarioMaisDependente(valorPlanoBeneficiario, valorPlanoSaudeDependente);

            bean.setValorTotalSolicitado(valorTotalSolicitado);
          } else {
            valorPlanoBeneficiario = beanItem.getValorGastoPlanoSaude();
            bean.setValorTotalSolicitado(valorPlanoBeneficiario);
          }
        }
      }
    }

    return beanAuxilioSaudeList;
  }

  @Override
  public List<AuxilioSaudeRequisicaoDependente> getAuxilioSaudeDependenteList(Long id) {
    List<AuxilioSaudeRequisicaoDependente> dependenteList = dao.getAuxilioSaudeRequisicaoDependentePorId(id);

    if (dependenteList != null) {
      // adicionar os anexos
      for (AuxilioSaudeRequisicaoDependente beanDep : dependenteList) {
        List<AuxilioSaudeRequisicaoDocumento> doclist = dao.getListaAnexos(beanDep);
        beanDep.setAuxilioSaudeRequisicaoDocumentoList(doclist);
      }
    }

    return dependenteList;
  }

  /***
   * somar o valor do beneficiario com o dependente
   * 
   * @param bean
   */
  public void setValorSolicitado(AuxilioSaudeRequisicao bean) {
    Double valor = 0.0;
    if (bean.getAuxilioSaudeRequisicaoBeneficiarioItemList() != null) {
      for (AuxilioSaudeRequisicaoItem auxBenef : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
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
    double valorBeneficiarioTemp = 0.0;
    double valorDependenteTemp = 0.0;
    if (valorBeneficiario != null) {
      valorBeneficiarioTemp = valorBeneficiario;
    }
    if (valorDependente != null) {
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

