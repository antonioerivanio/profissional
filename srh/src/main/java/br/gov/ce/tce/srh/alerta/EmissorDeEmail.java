package br.gov.ce.tce.srh.alerta;

import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmissorDeEmail {

	private String smtp = "webmail.tce.ce.gov.br";
	private String fromEmail = "srh@tce.ce.gov.br";

	private String email;
	private ArrayList<String> emails;
	private String assunto;
	private String mensagem;
	
	public void enviarEmail() {
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName(this.smtp);
		email.setSubject(this.assunto);
		
		try {
			
			email.setFrom(this.fromEmail);
			email.addTo(this.email);
			email.setHtmlMsg(mensagem);
			email.send();
		
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}	
	
	
	public String getEmail() {
		return email;
	}	
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<String> getEmails() {
		return emails;
	}
	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
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
