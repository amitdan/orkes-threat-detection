package io.orkes.samples.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

        public class SendMailUtil {

            private static String SENDER = "amit@swachev.com";

          	private static String CONFIGURATION_SET = "ConfigSet";


          	private static String SUBJECT = "Threat found";



          	private static String BODY_TEXT = "Hello,\r\n"
                                         + "Threat found in your recent mail";


          	// The HTML body of the email.
          	private static String BODY_HTML = "<html>"
                                          + "<head></head>"
                                                  + "<body>"
                                                  + "<h1>Hello!</h1>"
                                                  + "<p>Threat found in your recent mail .</p>"
                                                  + "</body>"
                                                  + "</html>";

              public static void sendEmail(String senderEmail, String fileLocation) throws AddressException, MessagingException, IOException {

             	Session session = Session.getDefaultInstance(new Properties());

                 // Create a new MimeMessage object.
                 MimeMessage message = new MimeMessage(session);
                 // Add subject, from and to lines.
                 message.setSubject(SUBJECT, "UTF-8");
                 message.setFrom(new InternetAddress(SENDER));
                 message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(senderEmail));

                 // Create a multipart/alternative child container.
                 MimeMultipart msg_body = new MimeMultipart("alternative");

                 // Create a wrapper for the HTML and text parts.
                 MimeBodyPart wrap = new MimeBodyPart();

                 // Define the text part.
                 MimeBodyPart textPart = new MimeBodyPart();
                 textPart.setContent(BODY_TEXT, "text/plain; charset=UTF-8");

                 // Define the HTML part.
                 MimeBodyPart htmlPart = new MimeBodyPart();
                 htmlPart.setContent(BODY_HTML,"text/html; charset=UTF-8");

                 // Add the text and HTML parts to the child container.
                 msg_body.addBodyPart(textPart);
                 msg_body.addBodyPart(htmlPart);

                 // Add the child container to the wrapper object.
                 wrap.setContent(msg_body);

                 // Create a multipart/mixed parent container.
                 MimeMultipart msg = new MimeMultipart("mixed");

                // Add the parent container to the message.
                 message.setContent(msg);

                 // Add the multipart/alternative part to the message.
                 msg.addBodyPart(wrap);

                 // Define the attachment
                 MimeBodyPart att = new MimeBodyPart();
                 DataSource fds = new FileDataSource(ATTACHMENT);
                 att.setDataHandler(new DataHandler(fds));
                 att.setFileName(fds.getName());

                 // Try to send the email.
                 try {
                         System.out.println("Attempting to send an email through Amazon SES "
                                           +"using the AWS SDK for Java...");

                         // Instantiate an Amazon SES client, which will make the service
                         // call with the supplied AWS credentials.
                         AmazonSimpleEmailService client =
                                         AmazonSimpleEmailServiceClientBuilder.standard()
                                // Replace US_WEST_2 with the AWS Region you're using for
                                // Amazon SES.
                                 .withRegion(Regions.US_WEST_2).build();

                         // Print the raw email content on the console
                         PrintStream out = System.out;
                         message.writeTo(out);

                         // Send the email.
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                         message.writeTo(outputStream);
                         RawMessage rawMessage =
                                		new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

                         SendRawEmailRequest rawEmailRequest =
                                 		new SendRawEmailRequest(rawMessage)
                         		    .withConfigurationSetName(CONFIGURATION_SET);

                         client.sendRawEmail(rawEmailRequest);
                         System.out.println("Email sent!");
                     // Display an error if something goes wrong.
                     } catch (Exception ex) {
                       System.out.println("Email Failed");
                        System.err.println("Error message: " + ex.getMessage());
                         ex.printStackTrace();
                     }
             }
 }
