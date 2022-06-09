package br.gov.ce.tce.srh.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDAO;
import br.gov.ce.tce.srh.dao.AuxilioSaudeRequisicaoDependenteDAO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.util.FacesUtil;

@Service("auxilioSaudeRequisicaoService")
public class AuxilioSaudeRequisicaoServiceImp implements AuxilioSaudeRequisicaoService {

  @Autowired
  private AuxilioSaudeRequisicaoDAO dao;

  @Autowired
  private AuxilioSaudeRequisicaoDependenteDAO daoDep;


  @Override
  public void salvar(AuxilioSaudeRequisicao bean) {
    dao.salvar(bean);
  }


  @Override
  @Transactional
  public void salvarAll(List<AuxilioSaudeRequisicao> beanList) {
    try {
      if (beanList != null && !beanList.isEmpty()) {
        for (AuxilioSaudeRequisicao beanAuxilio : beanList) {
          dao.salvar(beanAuxilio);

          salvarDependentes(beanAuxilio);
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private void salvarDependentes(AuxilioSaudeRequisicao beanAuxilio) {
    for (AuxilioSaudeRequisicaoDependente beanAuxDep : beanAuxilio.getAuxilioSaudeRequisicaoDependenteList()) {
      beanAuxDep.setAuxilioSaudeRequisicao(beanAuxilio);
      daoDep.salvar(beanAuxDep);
    }

  }


  @Override
  public Boolean isOK(AuxilioSaudeRequisicao bean) {
    if (bean.getFlAfirmaSerVerdadeiraInformacao() == Boolean.FALSE) {
      FacesUtil.addErroMessage("Campo Obrigatório Concordo");
      return Boolean.FALSE;
    }


    return Boolean.TRUE;
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
  public void adicionarDadosAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao, Boolean flgAfirmaSerVerdadeiraInformacao) {
    if (entidade.getAuxilioSaudeRequisicaoList() != null
                              && !entidade.getAuxilioSaudeRequisicaoList().isEmpty()) {
      for (AuxilioSaudeRequisicao beanAux : entidade.getAuxilioSaudeRequisicaoList()) {
        beanAux.setObservacao(entidade.getObservacao());
        beanAux.setFlAfirmaSerVerdadeiraInformacao(entidade.getFlAfirmaSerVerdadeiraInformacao());
        beanAux.setAuxilioSaudeRequisicaoDependenteList(entidade.getAuxilioSaudeRequisicaoDependenteList());
      }
    }

  }

}
