package server.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * This class is responsible to send mail to the current user/customer.
 * @author oyomtov
 * @version - Final
 */
public class MailSender {

	public static String sender = "myfuelteam9@gmail.com";
	public static String password ="MyFuelTeam9";
	
	public static void main(String[] args) {
		sendMail("Hey", "THIS IS MY MESSAGE", "oormaman@gmail.com");
	}

	public static boolean sendMail(String subject, String msg, String recipient) {
	      String host = "smtp.gmail.com"; 
	  
	      // Getting system properties 
		  	Properties properties = new Properties();
		  	properties.put("mail.smtp.auth", true);
		  	properties.put("mail.smtp.starttls.enable", true);
		  	properties.put("mail.smtp.host", "smtp.gmail.com");
		  	properties.put("mail.smtp.port", "587");
		  	// Setting up mail server 
		  	properties.setProperty("mail.smtp.host", host); 
	  
		  	// creating session object to get properties 
		  	Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
		  		protected PasswordAuthentication getPasswordAuthentication() {
		  			return new PasswordAuthentication(sender, password);
		  		}
		  	});
	      try 
	      { 
	         // MimeMessage object. 
	         MimeMessage message = new MimeMessage(session); 
	         // Set From Field: adding senders email to from field. 
	         message.setFrom(new InternetAddress(sender)); 
	  
	         // Set To Field: adding recipient's email to from field. 
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); 
	  
	         // Set Subject: subject of the email 
	         message.setSubject(subject); 
	  
	         // set body of the email. 
	         message.setText(msg); 
	  
	         // Send email. 
	         Transport.send(message);
	         return true;
	      } 
	      catch (MessagingException mex)  
	      { 
	         mex.printStackTrace(); 
	      }
	      return false;
	}
}
