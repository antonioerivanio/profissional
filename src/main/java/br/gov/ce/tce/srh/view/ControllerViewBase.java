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
  @Autowired
  protected LoginBean loginBean;
  
  protected static final String MESSAGEM = "mensagem";
  protected static final String ENTIDADE = "entidade";
  protected static final String LISTAR = "listar";
  protected static final String INCLUIR_OU_ALTERAR = "incluirAlterar";
  
  
  private String titulo = "Base de Cálculo: Valor do vencimento correspondente à  referência 23 do cargo de Analista de Controle Externo.";
  private String[] nomeColuna = {"FAIXA DE IDADE DO BENEFICIÁRIO EM ANOS", "PERCENTUAL DO AUXÍLIO-SAÚDE", "VALOR CORRESPONDENTE"};
  
  private T entidade;

  // paginação
  public Integer count = 0;
  public PagedListDataModel dataModel;
  private UIDataTable dataTable;
  public List<AuxilioSaudeRequisicao> pagedList;
  public int flagRegistroInicial = 0;
  public  Integer pagina = 1;
  boolean isEdicao = false;
  
  
  public static final String OCORREU_ERRO = "Ocorreu o seguinte erro: ";


  protected void createInstanceEntidade() throws InstantiationException, IllegalAccessException {
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
        createInstanceEntidade();
      }
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return entidade;
  }

  public void setEntidade(T entidade) {
    this.entidade = entidade;
  }

  public PagedListDataModel getDataModel() {
    if (getPagedList() == null) {
      setPagedList(new ArrayList<AuxilioSaudeRequisicao>());
    }

    if (getDataTable() == null) {
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
    if(dataTable == null) {
      setDataTable(new UIDataTable());
    }
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

  public void setPagina(Integer pagina) {
    this.pagina = pagina;
  }

  // PAGINAÇÃO
  public void limparListas() {
    dataModel = new PagedListDataModel();
    pagedList = new ArrayList<>();
    pagina = 1;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public boolean getIsEdicao() {
    return isEdicao;
  }

  public void setEdicao(boolean isEdicao) {
    this.isEdicao = isEdicao;
  }
  
  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String[] getNomeColuna() {
    return nomeColuna;
  }

  public void setNomeColuna(String[] nomeColuna) {
    this.nomeColuna = nomeColuna;
  }

  public boolean temPermisssaoParaAlterar() {
    return authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_INSERIR") || authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_ALTERAR");
  }

  // Alteração para o perfil consulta ser igual ao do servidor
  public boolean isServidor() {    
    return authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR");
  }

  /***
   * Para aprovar o servidor Aprovador da Area de Remuneração e Benefícios pertencente ao Grupo
   * Operador Ger. Remuneração - SRH no SCA - Sistema de Controle de Acesso
   * 
   * @return
   */
  public boolean isAnalista() {    
    return authenticationService.getUsuarioLogado().hasAuthority("ROLE_APROVADOR_AUXILIO_SAUDE");
  }
    
  
  /**
   * O botão editar vai aparecer quando a solicitação não tiver sido deferido
   * , e quando ela for deferido e o usuario logado é Analista 
   * @param bean
   * @return
   */
  public boolean validarExibicaoBotaoEditar(AuxilioSaudeRequisicao bean) {
    return bean.getDataFimRequisicao() == null ? Boolean.TRUE: Boolean.FALSE;
  }
  
  public boolean validarExibicaoBotaoSalvar() {
    if(isEdicao && isAnalista()) {
      return Boolean.TRUE;
    }    
    else if(!isEdicao && isAnalista() && ((AuxilioSaudeRequisicao)getEntidade()).getDataFimRequisicao() == null) {
      return Boolean.TRUE;
    }
    
    else if(!isEdicao && !isAnalista() && ((AuxilioSaudeRequisicao)getEntidade()).getDataFimRequisicao() == null) {
      return Boolean.TRUE;
    }
    else {
      return Boolean.FALSE;
    }
  }
  

}
