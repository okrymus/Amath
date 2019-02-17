package Project_Login;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Mail
// Programmer: Prakrit Saetang
// Last Modified: 12/8/16

/**
 * Mail Class - to handle sending mail for the forgetting password event
 * @author Prakrit
 */
public class Mail {

    // Class Instances
    private ArrayList<UserID> user;
    private String username;
    private String password;
    private Properties props;
    private Session session;


    /**
     * Constructor
     * @param user
     * @param email
     * @param emailPassword
     */
    public Mail(ArrayList<UserID> user, String email, String emailPassword) {
        this.user = user;
        this.username = email;
        this.password = emailPassword;

        // Connecting to Gmail server
        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * To send email (Password) to a user
     */
    public void sendEmail() {

        try {

            Message message = new MimeMessage(session);

            // Set sender email address
            message.setFrom(new InternetAddress(username));

            // Set Recipient email address
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.get(0).getEmail().trim()));

            // Set email subject
            message.setSubject("Forgot Password?");

            // Set actual text
            String text = "";
            for (UserID userID: user) {
                text += "your password for " + userID.getUsername() + ": " + userID.getPassword() + "\n";
            }

            message.setText(text);


            // Send the email
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean testEmail() {

        boolean isWorking = false;

        try {

            Message message = new MimeMessage(session);

            // Set sender email address
            message.setFrom(new InternetAddress(username));

            // Set Recipient email address
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));

            // Set email subject
            message.setSubject("Admin Email for AMath");

            // Set actual text
            String text = "Your email is used as an admin email for AMath Game";

            message.setText(text);


            // Send the email
            Transport.send(message);

            System.out.println("Done");

            isWorking = true;
        } catch (MessagingException e) {

            isWorking = false;
        }

        return isWorking;
    }
}
