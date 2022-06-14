package br.gov.ce.tce.srh.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDAO;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDependenteDAO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;

@Service("auxilioSaudeRequisicaoService")
public class AuxilioSaudeRequisicaoServiceImp implements AuxilioSaudeRequisicaoService {


  static Logger logger = Logger.getLogger(AuxilioSaudeRequisicaoServiceImp.class);

  @Autowired
  private AuxilioSaudeRequisicaoDAO dao;

  @Autowired
  private AuxilioSaudeRequisicaoDependenteDAO daoDep;


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
  public void salvarAll(List<AuxilioSaudeRequisicao> beanList) {
    try {
      logger.info("Iniciando o salvamento do auxilio saude requisição");
      
      if (beanList != null && !beanList.isEmpty()) {
        for (AuxilioSaudeRequisicao beanAuxilio : beanList) {
          // beanAuxilio.setStatusAprovacao(AuxilioSaudeRequisicao.ATIVO);
          dao.salvar(beanAuxilio);

          salvarAnexo(beanAuxilio);
          
          salvarDependentes(beanAuxilio);

        
        }
      }
      
    } catch (Exception e) {
      logger.error("Erro ao salvar o auxilio requisição : " + e.getMessage());
      System.out.println(e.getMessage());
    }

  }


  @Override
  public void salvarAnexo(AuxilioSaudeRequisicao bean) {
    try {
      logger.info("Iniciando o salvamento dos anexos do auxilio saude requisição");
      
      for (AuxilioSaudeRequisicaoDocumento beanAuxilio : bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        beanAuxilio.setAuxilioSaudeRequisicao(bean);
        beanAuxilio.setNomeArquivo(bean.getId() + "_" + beanAuxilio.getNomeArquivo());
        
        dao.getEntityManager().persist(beanAuxilio);

        // gravando em disco
        File file = new File(beanAuxilio.getCaminhoArquivo());
        FileOutputStream fop;
        fop = new FileOutputStream(file);
        fop.write(beanAuxilio.getComprovante());
        fop.flush();
        fop.close();
      }
    } catch (Exception e) {
      logger.error("Erro ao salvar o anexos do auxilio requisição : " + e.getMessage());
    }

  }

  private void salvarDependentes(AuxilioSaudeRequisicao beanAuxilio) {
    logger.info("Iniciando o salvamento dos dependentes do auxilio saude requisição");
    
    try {
      if (beanAuxilio.getAuxilioSaudeRequisicaoDependenteList() != null
                                && !beanAuxilio.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {
        for (AuxilioSaudeRequisicaoDependente beanAuxDep : beanAuxilio.getAuxilioSaudeRequisicaoDependenteList()) {
          beanAuxDep.setAuxilioSaudeRequisicao(beanAuxilio);
          daoDep.salvar(beanAuxDep);
        }
      }
  
    } catch (Exception e) {
      logger.error("Erro ao salvar os dependentes do auxilio requisição : " + e.getMessage());
    }
    
  }


  @Override
  public Boolean isOK(AuxilioSaudeRequisicao bean) {
    Boolean isResultadoOk = Boolean.TRUE;
    
    if (bean.getFuncional() == null) {
      FacesUtil.addErroMessage("Dados do Beneficiário inválido");
      isResultadoOk = false;
    }

    if (bean.getFuncional() != null && bean.getAuxilioSaudeRequisicaoList() != null
                              && !bean.getAuxilioSaudeRequisicaoList().isEmpty()) {
      for (AuxilioSaudeRequisicao beanAux : bean.getAuxilioSaudeRequisicaoList()) {
        if (beanAux.getValorGastoPlanoSaude() == null) {
          FacesUtil.addErroMessage("O Valor Mensal é Obrigatório");
          isResultadoOk = false;
        }

        if (beanAux.getPessoaJuridica() == null) {
          FacesUtil.addErroMessage("Nome da empresa é Obrigatório");
          isResultadoOk = false;
        }
      }

    }

    if (bean.getFlAfirmaSerVerdadeiraInformacao() == Boolean.FALSE) {
      FacesUtil.addErroMessage("Campo Obrigatório Concordo");
      isResultadoOk =  Boolean.FALSE;
    }


    if (bean.getDependenteSelecionado() != null) {
      if (bean.getAuxilioSaudeRequisicaoDependenteList() != null
                                && bean.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {

        FacesUtil.addErroMessage("O valor mensal e Nome da Empresa de Saúde deve ser adicionado");
        return false;
      }
      
      if (bean.getAuxilioSaudeRequisicaoDependenteList() != null
                                && !bean.getAuxilioSaudeRequisicaoDependenteList().isEmpty()) {
        if (bean.getAuxilioSaudeRequisicaoDocumentoDependenteList() == null
                                  || (bean.getAuxilioSaudeRequisicaoDocumentoDependenteList() != null
                                                            && bean.getAuxilioSaudeRequisicaoDocumentoDependenteList()
                                                                                      .isEmpty())) {
          
          FacesUtil.addErroMessage("A inclusão do anexo do Dependente é obrigatório");
          isResultadoOk = false;
        }

      }


    }

    if (bean.getObservacao() != null && !bean.getObservacao().isEmpty()) {
      if (bean.getObservacao().contains("...") || bean.getObservacao().length() <= 5) {
        FacesUtil.addErroMessage("Digite mais" + bean.getObservacao().length() + "caracteres para a observação");
        isResultadoOk =  Boolean.FALSE;
      }
    }

    if (bean.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null) {
      FacesUtil.addErroMessage("A inclusão do anexo do Beneficiario é obrigatório");
      isResultadoOk = Boolean.FALSE;
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
  public void executarAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao,
                            Boolean flgAfirmaSerVerdadeiraInformacao) {
    if (entidade.getAuxilioSaudeRequisicaoList() != null && !entidade.getAuxilioSaudeRequisicaoList().isEmpty()) {
      for (AuxilioSaudeRequisicao beanAux : entidade.getAuxilioSaudeRequisicaoList()) {
        beanAux.setObservacao(entidade.getObservacao());
        beanAux.setFlAfirmaSerVerdadeiraInformacao(entidade.getFlAfirmaSerVerdadeiraInformacao());
        beanAux.setAuxilioSaudeRequisicaoDependenteList(entidade.getAuxilioSaudeRequisicaoDependenteList());
        beanAux.setauxilioSaudeRequisicaoDocumentoBeneficiarioList(entidade.getAuxilioSaudeRequisicaoDocumentoBeneficiarioList());
        beanAux.setAuxilioSaudeRequisicaoDocumentoDependenteList(entidade.getAuxilioSaudeRequisicaoDocumentoDependenteList());
      }
    } else {
      entidade.setValorGastoPlanoSaude(null);
      entidade.setPessoaJuridica(null);

      entidade.setAuxilioSaudeRequisicaoList(new ArrayList<AuxilioSaudeRequisicao>());
      entidade.getAuxilioSaudeRequisicaoList().add(entidade);
    }
  }


  @Override
  public void setDadosIniciaisDaEntidadePorCpf(AuxilioSaudeRequisicao entidade, String cpf) {
    try {
      entidade.setDataInicioRequisicao(new Date());

      String matColaborador = funcionalService.getMatriculaAndNomeByCpfAtiva(cpf).getMatricula();

      Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva(matColaborador);

      List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());

      entidade.setFuncional(funcional);
      entidade.setDependentesComboList(dependenteList);
    } catch (Exception e) {
      logger.error("Usuario não encontrado");
    }
  }

  @Override
  public void setDadosIniciaisDaEntidade(AuxilioSaudeRequisicao entidade) {
    Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());

    List<Dependente> dependenteList = dependenteService.findByResponsavel(funcional.getPessoal().getId());

    entidade.setDataInicioRequisicao(new Date());
    entidade.setFuncional(funcional);
    entidade.setStatusFuncional(
                              funcional.getStatus() != null && funcional.getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO
                                                        : AuxilioSaudeRequisicao.INATIVO);
    entidade.setDependentesComboList(dependenteList);
  }

}
