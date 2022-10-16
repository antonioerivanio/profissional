package br.com.votacao.sindagri.email;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	@Value("${username}")
	private String username;
	@Value("${password}")
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		// props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "mail.antonio11566.c41.integrator.host");
		// props.put("mail.smtp.socketFactory.port", "465");
		// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		 Session session = Session.getDefaultInstance(props,
			      new javax.mail.Authenticator() {
			           protected PasswordAuthentication getPasswordAuthentication()
			           {
			                 return new PasswordAuthentication("_mainaccount@antonio11566.c41.integrator.host",
			                 "bbBSBZAEPXeb");
			           }
			      });
		 
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setSession(session);

		props.put("mail.debug", "true");

		return mailSender;
	}

}
