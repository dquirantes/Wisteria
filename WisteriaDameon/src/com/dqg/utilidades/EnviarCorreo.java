package com.dqg.utilidades;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;



public class EnviarCorreo {
	private static final Logger log = Logger.getLogger("Dameon");

	private String from;
	private String username;
	private String password;
	private String host;
	private String puerto;

	public EnviarCorreo(String from, String username, String password, String host, String puerto)
	{
		this.from = from;
		this.username = username;
		this.password = password;
		this.host = host ;
		this.puerto = puerto;
	}

	public boolean enviarCorreo (String para, String asunto, String texto) 
	{
		log.debug ("Enviar correo " + para + " " + asunto + " por el puerto " + puerto);
		// Recipient's email ID needs to be mentioned.


		// Sender's email ID needs to be mentioned
		String from = this.from;
		final String username = this.username;
		final String password = this.password;



		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.port", puerto);

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(para));

			// Set Subject: header field
			message.setSubject(asunto);

			// Now set the actual message
			message.setText(texto);

			// Send message
			Transport.send(message);

			log.debug("Mensaje enviado correctamente");

		} catch (MessagingException e) {
			log.error("Fallo envio corro electronico");
			log.error(e);
			return false;

		}


		return true;
	}
}