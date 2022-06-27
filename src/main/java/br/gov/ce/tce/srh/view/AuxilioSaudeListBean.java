package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("auxilioSaudeListBean")
@Scope("view")
public class AuxilioSaudeListBean extends ControllerViewBase<AuxilioSaudeRequisicao> implements Serializable {

  static Logger logger = Logger.getLogger(AuxilioSaudeListBean.class);


  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;
  
  private AuxilioSaudeRequisicao entidadeEditar;


  @PostConstruct
  private void init() {
    getEntidade().setFuncional(new Funcional());
    getEntidade().getFuncional().setPessoal(new Pessoal());
  }

  public void consultar() {

    try {

      limparListas();

      setPagedList(entidadeService.search(getEntidade().getFuncional().getPessoal().getNome(),
                                getEntidade().getFuncional().getPessoal().getCpf(), getDataTable().getFirst(),
                                getDataTable().getRows()));

      count = entidadeService.count(getEntidade().getFuncional().getPessoal().getNome(),
                                getEntidade().getFuncional().getPessoal().getCpf());

      if (count == 0) {
        FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
        logger.info("Nenhum registro foi encontrado.");
      }

      flagRegistroInicial = -1;

    } catch (Exception e) {
      limparListas();
      FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }


  public void salvar(boolean finalizar) {


  }


  public String editar() {
    FacesUtil.setFlashParameter("entidade", getEntidadeEditar());    
    return "incluirAlterar";
  }


  @Override
  public void salvar() {
    // TODO Auto-generated method stub

  }

  public AuxilioSaudeRequisicao getEntidadeEditar() {
    return entidadeEditar;
  }

  public void setEntidadeEditar(AuxilioSaudeRequisicao entidadeEditar) {
    this.entidadeEditar = entidadeEditar;
  }
  
  

  // FIM PAGINAÇÃO

}
