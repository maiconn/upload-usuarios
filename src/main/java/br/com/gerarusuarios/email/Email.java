package br.com.gerarusuarios.email;

import br.com.gerarusuarios.Main;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Email {

    public static void enviarEmail(String email, String user, String pass, boolean jenkins, boolean oracle) throws MessagingException, URISyntaxException, IOException {
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.host", "smtp.dbccompany.com.br");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.connectiontimeout", "10000");
        prop.put("mail.smtp.timeout", "10000");
        prop.put("mail.smtp.ssl.trust", "smtp.dbccompany.com.br");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(System.getProperty("mail.username"), System.getProperty("mail.pass"));
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(System.getProperty("mail.username"), "Vem Ser DBC"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Credenciais DBC");

        byte[] bytes = Files.readAllBytes(Paths.get(Main.class.getClassLoader().getResource("template.html").toURI()));
        String htmlContent = new String(bytes);
        if (oracle) {
            htmlContent = htmlContent.replace("{{ORACLE}}", new String(Files.readAllBytes(Paths.get(Main.class.getClassLoader().getResource("oracle.html").toURI()))));
        } else {
            htmlContent = htmlContent.replace("{{ORACLE}}", "");
        }
        if (jenkins) {
            htmlContent = htmlContent.replace("{{JENKINS}}", new String(Files.readAllBytes(Paths.get(Main.class.getClassLoader().getResource("jenkins.html").toURI()))));
        } else {
            htmlContent = htmlContent.replace("{{JENKINS}}", "");
        }
        htmlContent = htmlContent.replace("{{USUARIO}}", user);
        htmlContent = htmlContent.replace("{{SENHA}}", pass);


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

        message.setContent(new MimeMultipart(mimeBodyPart));

        Transport.send(message);
    }
}
