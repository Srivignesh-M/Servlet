package servlet.util;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.slf4j.Logger;

public class EmailSender {
		private static final Logger logger=LoggerFactory.getLogger(EmailSender.class);
		private final TemplateEngine templateEngine;
		public EmailSender() {
	        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
	        resolver.setPrefix("/templates/");
	        resolver.setSuffix(".html");
	        resolver.setTemplateMode("HTML");
	        resolver.setCharacterEncoding("UTF-8");

	        this.templateEngine = new TemplateEngine();
	        this.templateEngine.setTemplateResolver(resolver);
	    }
	    public void send(String email, String subject, String userName) {
	        String host = "smtp.gmail.com";
	        final String username = "srivigneshm1609@gmail.com";
	        final String password = "xpqe jokv wunt hbbc";
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.port", "587");

	        Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(username));
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
	            message.setSubject(subject);
	            Context context = new Context();
	            context.setVariable("userName", userName);
	            String htmlContent = templateEngine.process("email-template", context);
	            message.setContent(htmlContent, "text/html; charset=utf-8");
	            Transport.send(message);
	            logger.info("Email sent successfully");
	        } catch (MessagingException e) {
	            logger.info("Email sending Failed "+e.getMessage()+e);
	        }
	    }
	}
