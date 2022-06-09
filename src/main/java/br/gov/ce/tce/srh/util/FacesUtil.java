package br.gov.ce.tce.srh.util;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.context.FacesContext.getCurrentInstance;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.web.context.WebApplicationContext;

/**
 * Classe utilitária para acesso aos atributos do contexto externo.
 * 
* @since   : Sep 23, 2011, 15:27:36 AM
* @author  : robstownholanda@ivia.com.br
 * 
 */
public class FacesUtil {

	public static final String springVar = "org.springframework.web.context.WebApplicationContext.ROOT";

	public static Object getBean(String bean) {
		Object tmp = getApplicationAttribute(springVar);
		if (tmp == null)
			throw new IllegalArgumentException(
					"Não foi possível recuperar o contexto do spring,"
							+ " verfique se o org.springframework.web.context.ContextLoaderListener está definido no web.xml");
		WebApplicationContext wctx = (WebApplicationContext) tmp;
		return wctx.getBean(bean);
	}

	public static FacesContext getFacesContext() {
		return getCurrentInstance();
	}

	public static ExternalContext getExternalContext() {
		return getCurrentInstance().getExternalContext();
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

	public static void addMessage(final String message) {
		getCurrentInstance().addMessage(null, new FacesMessage(message));
	}

	public static void addInfoMessage(String message) {
		getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_INFO, message, message));
	}

	public static void addErroMessage(String message) {
		getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_ERROR, message, message));
	}
	
	public static void addAvisoMessage(String message) {
      getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }
	
	public static Object getFlashParameter(String name) {
        return getFacesContext().getExternalContext().getFlash().get(name);
    }
    public static void setFlashParameter(String name, Object value) {
        getFacesContext().getExternalContext().getFlash().put(name, value);
    }

}

