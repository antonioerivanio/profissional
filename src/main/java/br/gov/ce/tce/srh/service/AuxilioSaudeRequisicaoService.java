package br.gov.ce.tce.srh.service;

import java.io.IOException;
import java.util.List;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.sapjava.domain.Entidade;

public interface AuxilioSaudeRequisicaoService {

  public int count(String nome, String cpf);
  

  
  public void editar(AuxilioSaudeRequisicao bean);
  
  public AuxilioSaudeRequisicao getAuxilioSaudePorId(AuxilioSaudeRequisicao obj);
  public void salvar(AuxilioSaudeRequisicao bean);
  
  public void salvar(List<AuxilioSaudeRequisicao> beanList);
  
  public List<AuxilioSaudeRequisicaoDocumento> getListaArquivosPorIdAuxilio(BeanEntidade bean);
  
  public void salvarAnexo(AuxilioSaudeRequisicao bean) throws IOException;
  
  public List<AuxilioSaudeRequisicao> search(String nomeParam, String cpfParam, Integer first, Integer rows);
  
  
  
  
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
