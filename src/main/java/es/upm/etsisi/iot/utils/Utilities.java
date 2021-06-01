package es.upm.etsisi.iot.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import es.upm.etsisi.iot.security.entity.MainUser;
import es.upm.etsisi.iot.security.entity.User;

@Component
public class Utilities {
	
	@Value("${mail.account}")
	private String mailAccount;
	@Value("${mail.password}")
	private String mailPassword;
	
	public MainUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (MainUser) authentication.getPrincipal();
	}
	
	public void sendMail(User user, String unencryptedPassword) throws MessagingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.upm.es");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "smtp.upm.es");
		
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(mailAccount, mailPassword);
		    }
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailAccount));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
		message.setSubject("¡Bienvenid@ a IoT ETSISI!");

		String htmlMessage = "";
		try {
			File file = ResourceUtils.getFile("classpath:mail_template_register.html");

			htmlMessage = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			new MessagingException("Error al recuperar la plantilla de mail", e);
		}
		
		htmlMessage = htmlMessage.replace("{1}", user.getUsername()).replace("{2}", unencryptedPassword);
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(htmlMessage, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);
	} 
}
