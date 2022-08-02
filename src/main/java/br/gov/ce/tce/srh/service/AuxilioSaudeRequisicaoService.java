package br.gov.ce.tce.srh.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoItem;
import br.gov.ce.tce.srh.domain.BeanEntidade;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.UsuarioException;

public interface AuxilioSaudeRequisicaoService {
  
  public int count(AuxilioSaudeRequisicao bean);
    
  public void salvar(AuxilioSaudeRequisicao bean) throws IOException;
  public void salvar(List<AuxilioSaudeRequisicao> beanList);  
  public void salvarDependentes(List<AuxilioSaudeRequisicao> beanList);
  public void salvarDependentes(AuxilioSaudeRequisicao bean);
  public void atualizarDadosTabelaAuxilioSaudeBase(AuxilioSaudeRequisicao bean);
  
  public void atualizar(AuxilioSaudeRequisicao bean);
  
  public Funcional getFuncionalPorCpf(String cpf);  
  
  public AuxilioSaudeRequisicao getAuxilioSaudePorId(AuxilioSaudeRequisicao obj);
  
  public List<AuxilioSaudeRequisicaoDocumento> getListaArquivosPorIdAuxilio(BeanEntidade bean);
  
  public void salvarDocumentosBeneficiario(AuxilioSaudeRequisicaoItem bean) throws IOException;
  
  public void salvarDocumentosDependente(AuxilioSaudeRequisicaoDependente bean) throws IOException;
  
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao bean, Integer first, Integer rows);
    
  public BigDecimal getValorSalarioComBaseIdadePorPercentual(Double percentual);
  
  public void setValorMaximoSolicitadoPorIdade(AuxilioSaudeRequisicao bean);
  
  public List<AuxilioSaudeRequisicaoDependente> getAuxilioSaudeDependenteList(Long id);
  
  public void setValorSolicitado(AuxilioSaudeRequisicao bean);
  
  
  /***
   * valida campos obrigatórios
   * @param bean
   * @return
   */
  public boolean isOK(AuxilioSaudeRequisicao bean);
  
  public void setDadosIniciaisDaEntidadePorCpf(AuxilioSaudeRequisicao entidade, String cpf) throws UsuarioException;
  
  public void setDadosIniciaisDaEntidade(AuxilioSaudeRequisicao entidade);
  
  public PessoaJuridica getPessoaJuridicaPorId(PessoaJuridica pj, List<PessoaJuridica> comboEmpresasCadastradas);

  public Dependente getDependentePorId(Dependente dep, List<Dependente> dependentesComboList);    
}
