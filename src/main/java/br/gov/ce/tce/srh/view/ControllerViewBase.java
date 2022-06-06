package br.gov.ce.tce.srh.view;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

/**
 * 
 * @author erivanio.cruz
 *
 * @param <T>
 */
@Component
public abstract class ControllerViewBase<T> implements ControllerViewCrudBase {  

  private T entidade;
 
  protected void createNewInstance() throws InstantiationException, IllegalAccessException {
    entidade = getTypeParameterClass().newInstance();
  }
  
  @SuppressWarnings ("unchecked")
  public Class<T> getTypeParameterClass()
  {
      Type type = getClass().getGenericSuperclass();
      ParameterizedType paramType = (ParameterizedType) type;
      return (Class<T>) paramType.getActualTypeArguments()[0];
  }


  public T getEntidade() throws InstantiationException, IllegalAccessException {
    if(entidade == null) {
        createNewInstance();
    }
    
    return entidade ;
  }
  
  // paginação
  public int count = 0;
  public PagedListDataModel dataModel = new PagedListDataModel();
  public List<T> pagedList = new ArrayList<T>();
  public int flagRegistroInicial = 0;
  public Integer pagina = 1;

  public PagedListDataModel getDataModel() {
    return dataModel;
  }

  public List<T> getPagedList() {
    return pagedList;
  }

  public int getFlagRegistroInicial() {
    return flagRegistroInicial;
  }

  public Integer getPagina() {
    return pagina;
  }

}
