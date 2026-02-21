package servlet.util;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EmailSender {
		private static final Logger logger=LoggerFactory.getLogger(EmailSender.class);
	    public void send(String email, String subject, String body) {
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
	            message.setText(body);
	            Transport.send(message);
	            logger.info("Email sent successfully");
	        } catch (MessagingException e) {
	            logger.info("Email sending Failed "+e.getMessage()+e);
	        }
	    }
	}
