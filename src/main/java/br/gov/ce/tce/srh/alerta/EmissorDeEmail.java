package br.gov.ce.tce.srh.alerta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.util.ServletContextUtil;

@Component
public class EmissorDeEmail {
	
	@Autowired
	private ServletContextUtil context;
	
	private String smtp = "webmail.tce.ce.gov.br";
	private String fromEmail = "srh@tce.ce.gov.br";

	private String email;	
	private String assunto;
	private String mensagem;
	
	public void enviarEmail() {
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName(this.smtp);
		email.setSubject(this.assunto);
		
		try {
			
			email.setFrom(this.fromEmail);
			
			email.addTo(this.email);
//			email.addBcc("felipe.augusto@tce.ce.gov.br");
						
			URL url = new URL("file:///"+ context.getServerRootUrl() + "img" + File.separator + "logo-srh.svg");
			String cid = email.embed(url, "logoSRH");
			mensagem += "<img src=\"cid:"+ cid +"\" alt=\"SRH - Sistema de Recursos Humanos\" "
					+ " style=\"position: relative; left: -20px; max-width: 70%;\" >";			
			
			email.setHtmlMsg(mensagem);
			email.send();
		
		} catch (EmailException e) {			
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
	}
	
	public String preencherTemplateEmail(String nomeArquivoTemplate, Map<String, String> parametros) {
	
		String conteudo = getTemplateEmail(nomeArquivoTemplate);
		
		for (Map.Entry<String, String> parametro : parametros.entrySet()) {		
			conteudo = conteudo.replaceAll("\\$\\{" + parametro.getKey() + "\\}", parametro.getValue().replaceAll("\\$", "\\\\\\$"));
		}	
		
		this.setMensagem(conteudo);			
		
		return conteudo;
	}
	
	private String getTemplateEmail(String nomeArquivoTemplate){
		
		StringWriter writer = new StringWriter();
		
		try {
			InputStream template = new FileInputStream(context.getServerRootUrl() + "alerta-ferias" + File.separator + nomeArquivoTemplate);			
			IOUtils.copy(template, writer, "UTF-8");
		} catch (IOException e) {		
			e.printStackTrace();
		}		 
		
		return writer.toString();
	}
	
	
	public String getEmail() {
		return email;
	}	
	public void setEmail(String email) {
		this.email = email;
	}	
	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	

}
