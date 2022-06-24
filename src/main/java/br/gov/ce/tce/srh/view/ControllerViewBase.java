package br.gov.ce.tce.srh.view;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.util.PagedListDataModel;

/**
 * 
 * @author erivanio.cruz
 *
 * @param <T>
 */
@Component
public abstract class ControllerViewBase<T> implements ControllerViewCrudBase {

  
  @Autowired
  AuthenticationService authenticationService;
  
  private T entidade;

  // paginação
  public Integer count = 0;
  public PagedListDataModel dataModel;
  private UIDataTable dataTable;
  public List<AuxilioSaudeRequisicao> pagedList;
  public int flagRegistroInicial = 0;
  public Integer pagina = 1;
  public Boolean isEdicao = Boolean.FALSE;

  protected void createNewInstance() throws InstantiationException, IllegalAccessException {
    entidade = getTypeParameterClass().newInstance();
  }

  @SuppressWarnings("unchecked")
  public Class<T> getTypeParameterClass() {
    Type type = getClass().getGenericSuperclass();
    ParameterizedType paramType = (ParameterizedType) type;
    return (Class<T>) paramType.getActualTypeArguments()[0];
  }


  public T getEntidade() {
    try {
      if (entidade == null) {
        createNewInstance();
      }
    } catch (InstantiationException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return entidade;
  }

  public void setEntidade(T entidade) {
    this.entidade = entidade;
  }

  public PagedListDataModel getDataModel() {
    if(getPagedList() == null) {
      setPagedList(new ArrayList<AuxilioSaudeRequisicao>());
    }
    
    if(getDataTable() == null) {
      setDataTable(new UIDataTable());
    }

    if (flagRegistroInicial != getDataTable().getFirst()) {
      flagRegistroInicial = getDataTable().getFirst();
       
      if (count != 0) {        
                                  
        dataModel = new PagedListDataModel(getPagedList(), count);
      } else {
        limparListas();
      }
    }   


    return dataModel;

  }

  
  public void setPagedList(List<AuxilioSaudeRequisicao> pagedList) {
    this.pagedList = pagedList;
  }

  public UIDataTable getDataTable() {
    return dataTable;
  }

  public void setDataTable(UIDataTable dataTable) {
    this.dataTable = dataTable;
  }

  public List<AuxilioSaudeRequisicao> getPagedList() {
    return pagedList;
  }

  public int getFlagRegistroInicial() {
    return flagRegistroInicial;
  }

  public Integer getPagina() {
    return pagina;
  }

  // PAGINAÇÃO
  public void limparListas() {
    dataModel = new PagedListDataModel();
    pagedList = new ArrayList<AuxilioSaudeRequisicao>();
    pagina = 1;
  }

  public Boolean getIsEdicao() {
    return isEdicao;
  }

  public void setIsEdicao(Boolean isEdicao) {
    this.isEdicao = isEdicao;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public boolean temPermisssaoParaAlterar() {
      return authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_INSERIR")
                                || authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_ALTERAR");
  }

  // Alteração para o perfil consulta ser igual ao do servidor
  public boolean ehServidor() {
    System.out.println("autorização serivdor " + authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR"));
      return authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR");
  }
  
/***
 * Para aprovar o servidor Aprovador da Area de Remuneração e Benefícios pertencente 
 * ao Grupo Operador Ger. Remuneração - SRH no SCA - Sistema de Controle de Acesso 
 * @return
 */
 public boolean ehAprovador() {
     return authenticationService.getUsuarioLogado().hasAuthority("ROLE_APROVADOR_AUXILIO_SAUDE");
 }
  
  
  
}
