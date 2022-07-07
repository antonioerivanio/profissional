package br.gov.ce.tce.srh.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.gov.ce.tce.srh.exception.eTCEException;
import br.gov.ce.tce.srh.util.EmailTemplate;

/**
 * Classe usada para enviar emails aos usuarios do sistema.
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService, Serializable {

	private static final long serialVersionUID = 6277806775704899604L;

	private static final String MSG_ERRO_INESPERADO = "emailErro";
	private static final String EMAIL_SMTP_HOST= "SERVIDOR_SMTP";
	private static final String EMAIL_SMTP_PORT= "PORTA_SMTP";
	private static final String EMAIL_USUARIO = "EMAIL_USUARIO";
	//private static final String EMAIL_ORIGEM = "EMAIL_ORIGEM",	
	private static final String EMAIL_SENHA = "EMAIL_SENHA";


	@Autowired
	private AmbienteService ambienteService;
	
	private String emailOrigem;
	private String nomeUsuarioOrigem;
	private String senhaUsuarioOrigem;
	private String smtpHost;
	private int smtpPort;
	private Properties configuracaoEmail;

	public void configurarEmail() throws eTCEException {		

			Properties lProperties = new Properties();
			lProperties.put("mail.smtp.host", smtpHost);
			lProperties.put("mail.smtp.port", smtpPort);
			
			if (senhaUsuarioOrigem != null){ 
				lProperties.put("mail.smtp.auth", "true");
			}else{
				lProperties.put("mail.smtp.auth", "false");
			}
			if (smtpHost.equals("smtp.gmail.com")) {
				lProperties.put("mail.smtp.starttls.enable", "true");
				lProperties.put("mail.smtp.socketFactory.port", "587");
				lProperties.put("mail.smtp.socketFactory.fallback", "false");
				lProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				lProperties.put("mail.smtp.sendpartial", "true");
			}

			configuracaoEmail = lProperties;
		
	}

	private InternetAddress[] criarListaInternetAddress(List<String> destinatarios) throws Exception {
		InternetAddress[] lListaAddress = new InternetAddress[destinatarios.size()];
		for (int i = 0; i < destinatarios.size(); i++) {
			lListaAddress[i] = new InternetAddress(destinatarios.get(i));
		}
		
		if(ambienteService.ambiente().isDesenvolvimento()) {
			lListaAddress = atualizarParaEmailsDEV(lListaAddress);
		}
		
		return lListaAddress;
	}

	public void enviarEmail(List<String> destinatarios, String assunto, String conteudo, Map<String, InputStream> anexos, Integer tipoEmail) throws eTCEException {
		configurarEmail();
		try {
			// E-mail de Origem:
			InternetAddress lAddressOrigem = new InternetAddress(emailOrigem, nomeUsuarioOrigem);
			// E-mail(s) de Destino:
			InternetAddress[] lListaAddressDestino = criarListaInternetAddress(destinatarios);

			Session lSession = Session.getInstance(configuracaoEmail, null);
			MimeMessage lMineMessage = new MimeMessage(lSession);
			lMineMessage.setSentDate(new Date());
			lMineMessage.setFrom(lAddressOrigem);
			lMineMessage.addRecipients(Message.RecipientType.BCC, lListaAddressDestino);
			// Assunto do e-mail:
			lMineMessage.setSubject(assunto);
			lMineMessage.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
			if (anexos == null || anexos.isEmpty()) {
				// Conteudo do e-mail:
				lMineMessage.setContent(conteudo, "text/html; charset=iso-8859-1");
								
			} else {
				MimeMultipart lMultiPartRoot = new MimeMultipart("mixed");

 				// Cria o BodyPart do conteudo do e-mail:
				MimeBodyPart lBodyPartCorpo = new MimeBodyPart();
				lBodyPartCorpo.setContent(conteudo, "text/html; charset=iso-8859-1");
				lMultiPartRoot.addBodyPart(lBodyPartCorpo);

				// Cria os BodyParts para os anexos do e-mail:
				for (String nomeArquivo : anexos.keySet()) {
					// Cria um arquivo temporario para o anexo:
					File lArquivoTemporario = criarArquivoTemporario(anexos.get(nomeArquivo));
					DataSource lFileDataSource = new FileDataSource(lArquivoTemporario);
					// Cria o BodyPart do anexo:
					MimeBodyPart lBodyPartAnexo = new MimeBodyPart();
					lBodyPartAnexo.setDisposition(Part.ATTACHMENT);
					lBodyPartAnexo.setDataHandler(new DataHandler(lFileDataSource));
					lBodyPartAnexo.setFileName(nomeArquivo);
					lMultiPartRoot.addBodyPart(lBodyPartAnexo);
				}
				lMineMessage.setContent(lMultiPartRoot);	
			}
			lMineMessage.saveChanges();

			// Conecta-se ao servidor e envia o e-mail:
			Transport lTranportEnvio = lSession.getTransport("smtp");
			lTranportEnvio.connect(smtpHost, smtpPort, emailOrigem, senhaUsuarioOrigem);
			lTranportEnvio.sendMessage(lMineMessage, lMineMessage.getAllRecipients());
			lTranportEnvio.close();
		} catch (Exception ex) {
		  ex.printStackTrace();
			//throw new eTCEException(ResourceBundle.getMessage(MSG_ERRO_INESPERADO), pEx);
		}
	}

	/**
	 * Cria um arquivo temporario a partir do inputStream
	 * 
	 * @param inputStream
	 * @return Arquivo temporario.
	 * @throws Exception
	 */
	private File criarArquivoTemporario(InputStream inputStream) throws Exception {
		File arquivoTemporario = File.createTempFile("eTCE", null);
		arquivoTemporario.deleteOnExit();
		FileOutputStream lFos = new FileOutputStream(arquivoTemporario);
		int readByte;
		while ((readByte = inputStream.read()) != -1) {
			lFos.write(readByte);
		}
		lFos.close();
		return arquivoTemporario;
	}

	@Override
	public void enviarEmail(List<String> destinatarios, String assunto, EmailTemplate template, Map<String, InputStream> anexos, Integer tipoEmail) throws eTCEException {
		try {
			String conteudo = template.processar();
			this.enviarEmail(destinatarios, assunto, conteudo, anexos, tipoEmail);
		} catch (VelocityException | IOException e) {
			e.printStackTrace();
			throw new eTCEException("Não foi possível processar o template de e-mail", e);
		}
		
	}
	
	/**
	 * @author rafael.castro
	 * 
	 * Quando em DEV os e-mails são sobrescritos para a equipe de desenvolvimento. Caso precise alterar quem recebe em dev, precisa ajustar a lista interna.
	 * @param lListaAddress
	 */
	private InternetAddress[] atualizarParaEmailsDEV(InternetAddress[] lListaAddress) {
		List<String> emailList = new ArrayList<>(); 
		for (int i = 0; i < lListaAddress.length; i++) {
			emailList.add(lListaAddress[i].getAddress());
		}
		System.out.println("E-mails sobrescritos em DEV: " + emailList.toString());
		
		List<InternetAddress> emailsDev = new ArrayList<>();
		try {
			emailsDev.add(new InternetAddress("zacarias@tce.ce.gov.br"));
			emailsDev.add(new InternetAddress("esmayk.alves@tce.ce.gov.br"));
			emailsDev.add(new InternetAddress("rafael.castro@tce.ce.gov.br"));
			
			lListaAddress = emailsDev.toArray(new InternetAddress[emailsDev.size()]);
			
			emailList = new ArrayList<>(); 
			for (int i = 0; i < lListaAddress.length; i++) {
				emailList.add(lListaAddress[i].getAddress());
			}
		} catch (AddressException e) {
			System.out.println("Não foi possível instanciar os endereços de e-mail da Equipe de Desenvolvimento");
			lListaAddress = new InternetAddress[0];
			e.printStackTrace();
		}
		
		System.out.println("E-mails serão enviados para: " + emailList.toString());
		
		return lListaAddress;
	}

}