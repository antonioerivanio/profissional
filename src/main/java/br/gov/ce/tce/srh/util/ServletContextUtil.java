package br.gov.ce.tce.srh.util;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ServletContextUtil implements ServletContextAware  {
	
	private String serverRootUrl;
    	
	public String getServerRootUrl(){ return serverRootUrl; }
    
	@Override
	public void setServletContext(ServletContext servletContext){
        this.serverRootUrl=servletContext.getRealPath("/");
    }

}
