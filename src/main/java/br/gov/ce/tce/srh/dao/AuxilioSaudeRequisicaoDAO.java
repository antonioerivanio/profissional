package br.gov.ce.tce.srh.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;

@Repository
public class AuxilioSaudeRequisicaoDAO extends DAO<AuxilioSaudeRequisicao>  {
  
  public AuxilioSaudeRequisicaoDAO() {
    this.setmodelClass(AuxilioSaudeRequisicao.class);
  }
  

  @Override
  public List<AuxilioSaudeRequisicao> search(AuxilioSaudeRequisicao criteria) {
    // TODO Auto-generated method stub
    return null;
  }
}
