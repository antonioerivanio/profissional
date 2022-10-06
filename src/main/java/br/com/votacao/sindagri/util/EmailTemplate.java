package br.com.votacao.sindagri.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.VelocityException;

import net.sf.jasperreports.util.Base64Util;

public class EmailTemplate {
  protected String templateName;
  
  private String headerImageContent;
  
  private String footerImageContent;
  
  private String key;
  
  public EmailTemplate(String keyParametro, String templateName) {
    this.templateName = templateName;
    this.key = keyParametro;
    URL headerResource = EmailTemplate.class.getResource("/resources/images/e-tce-logo-topo-email.jpeg");
    if (headerResource != null) {
      File header = new File(headerResource.getFile());
      //this.headerImageContent = Base64Util.encoder(header);
    } 
    URL footerResource = EmailTemplate.class.getResource("/resources/images/logo_tce_transparente.png");
    if (footerResource != null) {
      File footer = new File(footerResource.getFile());
      //this.footerImageContent = Base64Util.encoder(footer);
    } 
  }
  
  public String getHeaderImageContent() {
    return this.headerImageContent;
  }
  
  public void setHeaderImageContent(String headerImageContent) {
    this.headerImageContent = headerImageContent;
  }
  
  public String getFooterImageContent() {
    return this.footerImageContent;
  }
  
  public void setFooterImageContent(String footerImageContent) {
    this.footerImageContent = footerImageContent;
  }
  
  public String processar() throws VelocityException, IOException {
    VelocityEngine templateEngine = TemplateEngineFactory.velocityEngine();
    Template template = templateEngine.getTemplate("./velocity_templates/" + this.templateName, "UTF-8");
    StringWriter stringWriter = new StringWriter();
    template.merge((Context)construirContexto(), stringWriter);
    return stringWriter.toString();
  }
  
  protected VelocityContext construirContexto() {
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put(this.key, this);
    return velocityContext;
  }
}
