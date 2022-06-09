package br.gov.ce.tce.srh.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;

@Repository
public class AuxilioSaudeRequisicaoDependenteDAO extends DAO<AuxilioSaudeRequisicaoDependente>  {
  
  public AuxilioSaudeRequisicaoDependenteDAO() {
    this.setmodelClass(AuxilioSaudeRequisicaoDependente.class);
  }
  
  @Override
  public List<AuxilioSaudeRequisicaoDependente> search(AuxilioSaudeRequisicaoDependente bean) {
    // TODO Auto-generated method stub
    return null;
  }
}
