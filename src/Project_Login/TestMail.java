package Project_Login;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

// testMail
// Programmer: Prakrit Saetang
// Last Modified: 12/10/16

/**
 * TestMail class - to test if the connection to  an admin email is working
 * @author PRAKRIT
 */
public class TestMail {
    public static void main(String[] args) {
        String username = "psaetang@bhcc.edu";
        String password = "Jung030401898";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
      

        props.put("mail.smtp.host", "smtp.exchange.bhcc.edu");
        props.put("mail.smtp.port", "443");
        

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("junjang_prakit@hotmail.com"));
            message.setSubject("Forgot Password?");
            message.setText("\nHere is your password for AMath: ");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
