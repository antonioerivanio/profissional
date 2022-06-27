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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDAO;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.UsuarioException;
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
  public int count(String nome, String cpf) {
    return dao.count(nome, cpf);
  }

  @Override
  @Transactional
  public void salvar(List<AuxilioSaudeRequisicao> beanList) {
    try {
      logger.info("Iniciando o salvamento do auxilio saude requisição");

      if (beanList != null && !beanList.isEmpty()) {
        for (AuxilioSaudeRequisicao beanAuxilio : beanList) {
          dao.getEntityManager().detach(beanAuxilio);
          dao.salvar(beanAuxilio);          
          salvarDependentes(beanAuxilio);
          salvarAnexo(beanAuxilio);
        }
      }

    } catch (Exception e) {
      logger.error("Erro ao salvar o auxilio requisição : " + e.getMessage());
      System.out.println(e.getMessage());
    }
  }


  @Override
  @Transactional
  public void editar(AuxilioSaudeRequisicao bean) {
    try {
      logger.info("Iniciando o Edicao do auxilio saude requisição");

      if (bean.checkBeneficiarioItemLIstNotNull()) {
        for (AuxilioSaudeRequisicao beanAuxilio : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
          if (beanAuxilio.getId() != null) {
            dao.atualizar(beanAuxilio);
          } else {
            dao.salvar(beanAuxilio);
          }

          salvarDependentes(beanAuxilio);
          salvarAnexo(beanAuxilio);
        }
      }

    } catch (Exception e) {
      logger.error("Erro ao salvar o auxilio requisição : " + e.getMessage());
      System.out.println(e.getMessage());
    }
  }

  private void salvarDependentes(AuxilioSaudeRequisicao beanAuxilio) {
    logger.info("Iniciando o salvamento dos dependentes do auxilio saude requisição");

    try {
      if (beanAuxilio.getAuxilioSaudeRequisicaoDependenteList() != null && !beanAuxilio.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {
        for (AuxilioSaudeRequisicaoDependente beanAuxDep : beanAuxilio.getAuxilioSaudeRequisicaoDependenteList()) {
          beanAuxDep.setAuxilioSaudeRequisicao(beanAuxilio);

          if (beanAuxDep.getId() != null) {
            dao.atualizar(beanAuxDep);
          } else {
            dao.salvar(beanAuxilio);
          }

          salvarAnexoDependente(beanAuxDep);

        }
      }

    } catch (Exception e) {
      logger.error("Erro ao salvar os dependentes do auxilio requisição : " + e.getMessage());
    }
  }


  @Override
  public void salvarAnexo(AuxilioSaudeRequisicao bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");

    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      if (beanAuxDoc.getId() != null) {
        dao.atualizar(beanAuxDoc);
      } else {
        beanAuxDoc.setAuxilioSaudeRequisicao(bean);
        dao.salvar((BeanEntidade) beanAuxDoc);
      }

      fazerUploadArquivo(beanAuxDoc);
    }
  }


  public void salvarAnexoDependente(AuxilioSaudeRequisicaoDependente bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição dependente");

    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicao().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
      if (beanAuxDoc.getId() != null) {
        beanAuxDoc.setAuxilioSaudeRequisicaoDependente(bean);

        dao.atualizar(beanAuxDoc);
      } else {
        dao.salvar(beanAuxDoc);
      }
      fazerUploadArquivo(beanAuxDoc);
    }
  }


  public void fazerUploadArquivo(AuxilioSaudeRequisicaoDocumento bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");

    String nomeArquivo = bean.getArquivoVO() != null && bean.getArquivoVO().getNomeTemp() != null ? bean.getArquivoVO().getNomeTemp() : bean.getNomeArquivo();
    Path arquivoSalvo = Paths.get(bean.getCaminhoArquivo() + File.separator + nomeArquivo);
    String novoNome = bean.getId() + "_" + bean.getNomeArquivo() + ArquivoVO.PDF;
    // \\svtcenas2\Desenvolvimento\svtcefs2\SRH\comprovanteAuxSaude\2022\15541
    // renomear o arquivo dentro da pasta
    Files.move(arquivoSalvo, arquivoSalvo.resolveSibling(novoNome));
    bean.setNomeArquivo(novoNome);
  }

  @Override
  public boolean isOK(AuxilioSaudeRequisicao bean) {
    Boolean isResultadoOk = Boolean.TRUE;

    if (bean.getFuncional() == null) {
      FacesUtil.addErroMessage("Você precisa selecionar o Beneficiário e clicar no Botão consulta");
      return false;
    }

    if (bean.getDependenteSelecionado() == null) {// não tem dependentes

      if (bean.checkBeneficiarioItemLIstNotNull()) {
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

      if (bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null || !bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().isEmpty()) {
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

    PessoaJuridica pessoaJuridicaEncontrada = comboEmpresasCadastradas.get(index);

    return pessoaJuridicaEncontrada;
  }

  /*** selecionar o dependente completo na lista e adicionar na entidade ***/
  @Override
  public Dependente getDependentePorId(Dependente dep, List<Dependente> dependentesComboList) {
    int index = dependentesComboList.indexOf(dep);
    Dependente dependente = dependentesComboList.get(index);

    return dependente;
  }


  @Override
  public void executarAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao, Boolean flgAfirmaSerVerdadeiraInformacao) {
    if (entidade.getId() == null) {
      if (entidade.checkBeneficiarioItemLIstNotNull()) {// um
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
      String matColaborador = funcionalService.getMatriculaAndNomeByCpfAtiva(cpf).getMatricula();
      Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva(matColaborador);
      List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());
      entidade.setFuncional(funcional);
      entidade.setDependentesComboList(dependenteList);      
    } catch (Exception e) {
      logger.error("Usuario não encontrado");
      throw new UsuarioException("Usuario não encontrado");
    }
  }

  @Override
  public void setDadosIniciaisDaEntidade(AuxilioSaudeRequisicao entidade) {
    Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());

    List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());

    entidade.setDataInicioRequisicao(new Date());
    entidade.setFuncional(funcional);
    entidade.setStatusFuncional(funcional.getStatus() != null && funcional.getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO : AuxilioSaudeRequisicao.INATIVO);
    entidade.setDependentesComboList(dependenteList);
  }

  @Override
  public List<AuxilioSaudeRequisicao> search(String nomeParam, String cpfParam, Integer first, Integer rows) {
    // TODO Auto-generated method stub
    return dao.search(nomeParam, cpfParam, first, rows);
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
    int TRINTA_ANOS = 30;
    int QUARENTA_ANOS = 40;
    int CINQUENTA_ANOS = 51;
    int SESSENTA_ANOS = 60;

    int idade = bean.getFuncional().getPessoal().getIdade();

    if (idade <= TRINTA_ANOS) {
      Double valor = getValorSalarioComBaseIdadePorPercentual(AuxilioSaudeRequisicaoDAO.TRES_POR_CENTO).doubleValue();
      bean.setValorMaximoAserRestituido(valor);
    }

    if (idade > TRINTA_ANOS && idade <= QUARENTA_ANOS) {
      Double valor = getValorSalarioComBaseIdadePorPercentual(AuxilioSaudeRequisicaoDAO.TRES_PONTO_CINCO_POR_CENTO).doubleValue();
      bean.setValorMaximoAserRestituido(valor);
    }

    if (idade > QUARENTA_ANOS && idade <= CINQUENTA_ANOS) {
      Double valor = getValorSalarioComBaseIdadePorPercentual(AuxilioSaudeRequisicaoDAO.QUATRO_POR_CENTO).doubleValue();
      bean.setValorMaximoAserRestituido(valor);
    }

    if (idade > CINQUENTA_ANOS && idade <= SESSENTA_ANOS) {
      Double valor = getValorSalarioComBaseIdadePorPercentual(AuxilioSaudeRequisicaoDAO.QUATRO_PONTO_CINCO_POR_CENTO).doubleValue();
      bean.setValorMaximoAserRestituido(valor);
    }

    if (idade > SESSENTA_ANOS) {
      Double valor = getValorSalarioComBaseIdadePorPercentual(AuxilioSaudeRequisicaoDAO.CINCO_POR_CENTO).doubleValue();
      bean.setValorMaximoAserRestituido(valor);
    }
  }
}

