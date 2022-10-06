package br.com.votacao.sindagri.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.springframework.web.context.WebApplicationContext;

public class FacesUtil {
  public static final String MENSAGEM_SUCESSO = "Registro salvar com sucesso!";
  
  public static final String MENSAGEM_ALTERACAO_SUCESSO = "Alterafoi gravada com sucesso!";
  
  public static final String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";
  
  public static Object getBean(String bean) {
    Object tmp = getApplicationAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
    if (tmp == null)
      throw new IllegalArgumentException(
          "Nfoi possrecuperar o contexto do spring, verfique se o org.springframework.web.context.ContextLoaderListener estdefinido no web.xml"); 
    WebApplicationContext wctx = (WebApplicationContext)tmp;
    return wctx.getBean(bean);
  }
  
  public static FacesContext getFacesContext() {
    return FacesContext.getCurrentInstance();
  }
  
  public static ExternalContext getExternalContext() {
    return FacesContext.getCurrentInstance().getExternalContext();
  }
  
  public static Object getSessionAttribute(String attribute) {
    return getExternalContext().getSessionMap().get(attribute);
  }
  
  public static void setSessionParameter(String name, Object value) {
    getExternalContext().getSessionMap().put(name, value);
  }
  
  public static Object getApplicationAttribute(String attribute) {
    return getExternalContext().getApplicationMap().get(attribute);
  }
  
  public static void addMessage(String message) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
  }
  
  public static void addInfoMessage(String message) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
  }
  
  public static void addErroMessage(String message) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
  }
  
  public static void addErroMessage(String idComponent, String message) {
    FacesContext.getCurrentInstance().addMessage(idComponent, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
  }
  
  public static void addAvisoMessage(String message) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
  }
  
  public static Object getFlashParameter(String name) {
    return getFacesContext().getExternalContext().getFlash().get(name);
  }
  
  public static void setFlashParameter(String name, Object value) {
    getFacesContext().getExternalContext().getFlash().put(name, value);
  }
}
