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
          
          salvarArquivos(bean);
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

      if (bean.checkBeneficiarioItemLIstNotNull()) {
        for (AuxilioSaudeRequisicao beanAuxilio : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
          beanAuxilio.setDataAlteracao(new Date());
          
          if (beanAuxilio.getId() != null) {
            dao.atualizar(beanAuxilio);
          } else {
            dao.salvar(beanAuxilio);
          }
          
          salvarDependentes(beanAuxilio);
          salvarDocumentosBeneficiario(beanAuxilio);

          atualizarTabelaAuxilioSaudeBase(beanAuxilio);
        }

      }

    } catch (Exception e) {
      logger.error("Erro ao atualizar o auxilio-saúde " + e.getMessage());
      System.out.println(e.getMessage());
    }
  }

  private void atualizarTabelaAuxilioSaudeBase(AuxilioSaudeRequisicao bean) {
    if (bean.getStatusAprovacao() != null) {
      Funcional funcional = dao.getEntityManager().find(Funcional.class, bean.getFuncional().getId());
      AuxilioSaudeRequisicaoBase auxilioSaudeRequisicaoBase = dao.getAuxilioSaudeBasePorPessoaIdeAtivo(funcional.getPessoal(), FlagAtivo.SIM);

      if (auxilioSaudeRequisicaoBase != null) {
        auxilioSaudeRequisicaoBase.setCustoPlanoBase(bean.getValorTotalSolicitado());
        auxilioSaudeRequisicaoBase.setDataAtualizacao(new Date());
        auxilioSaudeRequisicaoBase.setObservacao("Valor adicionar automaticamente a partir da tela do Auxilio-Saúde");
      } else {

        auxilioSaudeRequisicaoBase = new AuxilioSaudeRequisicaoBase(bean.getFuncional().getPessoal(), bean.getUsuario(), bean.getValorTotalSolicitado(),
                                  "Registro criado automaticamente a partir da tela do Auxilio-Saúde", new Date(), FlagAtivo.SIM, new Date());

        dao.salvar(auxilioSaudeRequisicaoBase);
      }
    }
  }

  private void salvarDependentes(AuxilioSaudeRequisicao bean) {
    logger.info("Iniciando o salvamento dos dependentes do auxilio saude requisição");

    try {
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

    } catch (Exception e) {
      logger.error("Erro ao salvar os dependentes do auxilio requisição : " + e.getMessage());
    }
  }


  @Override  
  public void salvarDocumentosBeneficiario(AuxilioSaudeRequisicao bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");

    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      //se a lista de dependentes estivar é porque somente o restrido do Beneficiario está sendo incuída
      if(bean.getAuxilioSaudeRequisicaoDependenteList() == null) {
        beanAuxDoc.setAuxilioSaudeRequisicaoDependente(null);
      }
      
      if (beanAuxDoc.getId() != null) {
        dao.atualizar(beanAuxDoc);
      } else {
        //dao.getEntityManager().flush();
        AuxilioSaudeRequisicao find = dao.getEntityManager().find(AuxilioSaudeRequisicao.class, bean.getId());
        beanAuxDoc.setAuxilioSaudeRequisicao(find);
        dao.salvar(beanAuxDoc);
      }

      ///fazerUploadArquivo(beanAuxDoc);
    }
  }

  public void salvarDocumentosDependente(AuxilioSaudeRequisicaoDependente bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição dependente");

    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicao().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
      //se estiver nulo é por está sendo incluido somente dados do dependente
      if(bean.getAuxilioSaudeRequisicao().getPessoaJuridica() == null) {
        beanAuxDoc.setAuxilioSaudeRequisicao(null);
      }
      
      if (beanAuxDoc.getId() != null) {
        dao.atualizar(beanAuxDoc);
      } else {
        beanAuxDoc.setAuxilioSaudeRequisicaoDependente(bean);
        dao.salvar(beanAuxDoc);
      }
      
      
    }
  }
  
  private void salvarArquivos(AuxilioSaudeRequisicao bean) throws IOException {
    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      fazerUploadArquivo(beanAuxDoc);  
    }
    
    for (AuxilioSaudeRequisicaoDocumento beanAuxDoc : bean.getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
        fazerUploadArquivo(beanAuxDoc);  
    }
    
  } 

  public void fazerUploadArquivo(AuxilioSaudeRequisicaoDocumento bean) throws IOException {
    logger.info("Iniciando o salvamento dos anexos Beneficiario do auxilio saude requisição");

   
    Path arquivoSalvo = Paths.get(bean.getCaminhoArquivo() + File.separator + bean.getNomeArquivo());
    String novoNome = bean.getId() + "_" + bean.getNomeArquivo();
    bean.setNomeArquivo(novoNome);
    
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
    if(dependentesComboList == null || dependentesComboList.isEmpty()) {
      throw new NullPointerException("Ops!. Por favor escolha um dependente para continuar");
     }
    
    int index = dependentesComboList.indexOf(dependente);    
    return dependentesComboList.get(index);
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
      Funcional funcional = getFuncionalPorCpf(cpf);
      
      List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());
      entidade.setFuncional(funcional);
      entidade.setDependentesComboList(dependenteList);
    } catch (Exception e) {
      logger.error("Usuario não encontrado");
      //throw new UsuarioException("Usuario não encontrado");
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
    if(entidade.getFuncional() == null) {
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


  @Override
  public int count(AuxilioSaudeRequisicao bean) {
    return dao.count(bean);
   }

  @Override
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao bean, Integer first, Integer rows) {
    // TODO Auto-generated method stub
    return dao.search(bean, first, rows);
  }
  
}

