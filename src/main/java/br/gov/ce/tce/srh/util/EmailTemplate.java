package br.gov.ce.tce.srh.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import br.gov.ce.tce.eTCE.util.Base64Util;

public abstract class EmailTemplate {
	
	protected String templateName;
	
	private String headerImageContent;

	private String footerImageContent;
	
	public EmailTemplate(String templateName) {
		this.templateName = templateName;
		
		URL headerResource = EmailTemplate.class.getResource("/resources/images/e-tce-logo-topo-email.jpeg");
		if(headerResource != null) {
			File header = new File(headerResource.getFile());
			headerImageContent = Base64Util.encoder(header);			
		}
		
		URL footerResource = EmailTemplate.class.getResource("/resources/images/logo_tce_transparente.png");
		if(footerResource != null) {
			File footer = new File(footerResource.getFile());
			footerImageContent = Base64Util.encoder(footer);			
		}
		
		/*String headerImagePath = FacesUtil.getExternalContext().getRealPath("/resources/images/e-tce-logo.png");
		String footerImagePath = FacesUtil.getExternalContext().getRealPath("/resources/images/logo_tce_transparente.png");
		
		headerImageContent = Base64Util.encoder(new File(headerImagePath));
		footerImageContent = Base64Util.encoder(new File(footerImagePath));*/
	}
	
	public String getHeaderImageContent() {
		return headerImageContent;
	}

	public void setHeaderImageContent(String headerImageContent) {
		this.headerImageContent = headerImageContent;
	}

	public String getFooterImageContent() {
		return footerImageContent;
	}

	public void setFooterImageContent(String footerImageContent) {
		this.footerImageContent = footerImageContent;
	}
	
	public String processar() throws VelocityException, IOException {
		VelocityEngine templateEngine = TemplateEngineFactory.velocityEngine();
		Template template = templateEngine.getTemplate("./velocity_templates/" + templateName, "UTF-8");
		
		StringWriter stringWriter = new StringWriter();
		template.merge(construirContexto(), stringWriter);
		
		return stringWriter.toString();
	}
	
	protected abstract VelocityContext construirContexto();
}
