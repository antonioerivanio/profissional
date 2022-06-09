package br.gov.ce.tce.srh.service;

import java.util.List;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.PessoaJuridica;

public interface AuxilioSaudeRequisicaoService {

  public void salvar(AuxilioSaudeRequisicao bean);
  

  public void salvarAll(List<AuxilioSaudeRequisicao> beanList);
  
  /***
   * valida campos obrigatórios
   * @param bean
   * @return
   */
  public Boolean isOK(AuxilioSaudeRequisicao bean);
  

  public PessoaJuridica getPessoaJuridicaPorId(PessoaJuridica pj, List<PessoaJuridica> comboEmpresasCadastradas);

  public Dependente getDependentePorId(Dependente dep, List<Dependente> dependentesComboList);
  
  public void adicionarDadosAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao, Boolean flgAfirmaSerVerdadeiraInformacao);
    
}
