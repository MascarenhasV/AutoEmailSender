
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
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

/**
 * 
 * @author Vipul Mascarenhas
 * 
 * The code generates emails using the file names for the respective andrew ids. The file names with the shuffle details
 * should be in the format "andrewid_shuffle.html".
 * 
 * The code takes in 3 inputs; the username of the sender, the password of the sender's email and the file path of the shuffle files.
 * 
 * Referred  Tutorialspoint for Javamail API code - http://www.tutorialspoint.com/javamail_api/javamail_api_send_email_with_attachment.htm
 *
 */
public class EmailSender {
   public static void main(String[] args) {
     
     if(args.length != 3){
        System.out.println("The program needs 3 parameters: 1. Username. 2. Password. 3: File path of shuffle files");
        System.exit(0);
     }

      // Sender's email ID 
      String from = args[0].trim();

      final String username = args[0].trim();
      final String password = args[1].trim();

      // We are sending email through Gmail SMTP
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      Map<String, String> emailIdFileMap = new HashMap<String, String>();
      
      final File folder = new File(args[2].trim());
      listEmailIds(folder, emailIdFileMap);
       
      for (String key : emailIdFileMap.keySet()){
        String fileLocation = folder+"/"+emailIdFileMap.get(key);
        createMessages(from, key, session, fileLocation);
      }

   }
   
   public static void createMessages(String from, String to, Session session, String fileName){
      
      try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("11-601 Shuffle for the week");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Build the text for the email
            String text = "<html>Hi "+to.split("@")[0]+ 
                "<p>Please find attached the assignment details for this week's shuffle. </p>"+
                "<p>All the best.</p>"+
                "<br>"+
                "<p>From,"
                + "The 11-601 Instructors & TAs"
                + "</p>"
                + "</html>";
            
            messageBodyPart.setContent(text, "text/html");
           
            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);   
           
            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully to "+to);
     
         } catch (MessagingException e) {
            throw new RuntimeException(e);
         }
   }
   
   /**
    * This folder takes in the path of the folder containing the shuffle files and generates a list of andrew ids 
    * with the corresponding file names
    * 
    * @param folder
    *          Path of the folder containing all the shuffle files
    * @param emailIdFileMap
    *          Map containing the andrew ids and the corresponding file names
    * 
    */
   public static void listEmailIds(final File folder, Map<String, String> emailIdFileMap) {
       
       for (final File fileEntry : folder.listFiles()) {
           if (fileEntry.isDirectory()) {
            listEmailIds(fileEntry, emailIdFileMap);
           } else {
            emailIdFileMap.put(fileEntry.getName().split("_")[0]+"@andrew.cmu.edu", fileEntry.getName());
           }
       }
   }
}
