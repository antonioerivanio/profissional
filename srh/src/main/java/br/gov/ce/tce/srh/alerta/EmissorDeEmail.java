package br.gov.ce.tce.srh.alerta;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmissorDeEmail {

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
						
			URL url = new URL("file://"+ this.getClass().getResource("logo-srh.png").getPath());
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
		
		InputStream arquivoIS = this.getClass().getResourceAsStream(nomeArquivoTemplate);		
		StringWriter writer = new StringWriter();
		
		try {
			IOUtils.copy(arquivoIS, writer, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
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
