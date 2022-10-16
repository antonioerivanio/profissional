package br.com.votacao.sindagri.email;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component("emailBean")
@Scope("request")
public class EMailController {

    private final EmailService emailService;
    private final String mailTo;

    public EMailController(EmailService emailService, @Value("${mail.to}") String mailTo) {
        this.emailService = emailService;
        this.mailTo = mailTo;
    }

   
    public void sendTestReport(){
        final Mail mail = new MailBuilder()
                .From("lilianerodpereira@gmail.com") // For gmail, this field is ignored.
                .To(this.mailTo)
                .Template("mail-template.vm")
                .AddContext("subject", "Test Email")
                .AddContext("content", "Hello World!")
                .Subject("Hello")
                .createMail();
       // String responseMessage = request.getRequestURI();
        try {
            this.emailService.sendHTMLEmail(mail);
        }
        catch (Exception e) {
        	e.printStackTrace();
           // responseMessage = "Request Unsuccessful \n" + e.getMessage() + "\n" + responseMessage;
           // return responseMessage;
        }
       // responseMessage = "Request Successful \n" + responseMessage;
       // return responseMessage;
    }

    @RequestMapping(value ="/")
    public String homePage(HttpServletRequest request) {
        String responseMessage = request.getRequestURI();
        responseMessage = "Welcome to mailing service. \n" +
            "Please use /test to send sample report \n" + responseMessage;
        return responseMessage;
    }

}
