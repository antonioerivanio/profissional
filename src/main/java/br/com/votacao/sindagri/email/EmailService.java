package br.com.votacao.sindagri.email;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
 
@Service("emailService")
public class EmailService {
	
	
	public void sendHTMLEmail(Mail mail) throws MessagingException {
		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		// props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "mail.antonio11566.c41.integrator.host");
		// props.put("mail.smtp.socketFactory.port", "465");
		// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("_mainaccount@antonio11566.c41.integrator.host", "bbBSBZAEPXeb");
			}
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("appvotacaosindagri@antonio11566.c41.integrator.host"));
		// Remetente

		Address[] toUser = InternetAddress // Destinatário(s)
				.parse(mail.getMailTo());

		
		  MimeMessage emailMessage = new MimeMessage(session); MimeMessageHelper
		  mailBuilder = new MimeMessageHelper(emailMessage, true);
		  mailBuilder.setTo(mail.getMailTo()); mailBuilder.setFrom(mail.getMailFrom());
		  mailBuilder.setText(mail.getMailContent(), true);
		  mailBuilder.setSubject(mail.getMailSubject());
		 

			/*
			 * message.setRecipients(Message.RecipientType.TO, toUser);
			 * message.setSubject(mail.getMailSubject());// Assunto
			 * message.setText(mail.getMailContent());
			 */
		/** Método para enviar a mensagem criada */
		Transport.send(emailMessage);

		System.out.println("Feito!!!");
	}
}