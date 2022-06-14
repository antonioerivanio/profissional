package br.gov.ce.tce.srh.service;

import java.util.List;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.PessoaJuridica;

public interface AuxilioSaudeRequisicaoService {

  public void salvar(AuxilioSaudeRequisicao bean);

  public void salvarAll(List<AuxilioSaudeRequisicao> beanList);
  
  public void salvarAnexo(AuxilioSaudeRequisicao bean);
  
  /***
   * valida campos obrigatórios
   * @param bean
   * @return
   */
  public Boolean isOK(AuxilioSaudeRequisicao bean);
  
  public void setDadosIniciaisDaEntidadePorCpf(AuxilioSaudeRequisicao entidade, String cpf);
  
  public void setDadosIniciaisDaEntidade(AuxilioSaudeRequisicao entidade);
  
  public PessoaJuridica getPessoaJuridicaPorId(PessoaJuridica pj, List<PessoaJuridica> comboEmpresasCadastradas);

  public Dependente getDependentePorId(Dependente dep, List<Dependente> dependentesComboList);
  
  /**
   * executa alguma ação que não seja validações, mas que deve ser feito antes de salvar
   * @param entidade
   * @param observacao
   * @param flgAfirmaSerVerdadeiraInformacao
   */
  public void executarAntesSalvar(AuxilioSaudeRequisicao entidade, String observacao, Boolean flgAfirmaSerVerdadeiraInformacao);
    
}
