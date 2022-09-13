package br.com.gerarusuarios.email;

import br.com.gerarusuarios.Main;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class Email {

    public static void enviarEmail(String email, String user, String pass, boolean jenkins, boolean oracle) throws MessagingException, URISyntaxException, IOException, MessagingException {
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.host", "smtp.dbccompany.com.br");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.connectiontimeout", "10000");
        prop.put("mail.smtp.timeout", "10000");
        prop.put("mail.smtp.ssl.trust", "*");

        String usuarioEmail = System.getProperty("mail.username");
        String senhaEmail = System.getProperty("mail.pass");
//        System.out.println(usuarioEmail + " " + senhaEmail);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(usuarioEmail, senhaEmail);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(System.getProperty("mail.username"), "Vem Ser DBC"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Credenciais DBC");

        String htmlContent = readFileToString("template.html");
        if (oracle) {
            htmlContent = htmlContent.replace("{{ORACLE}}", readFileToString("oracle.html"));
        } else {
            htmlContent = htmlContent.replace("{{ORACLE}}", "");
        }
        if (jenkins) {
            htmlContent = htmlContent.replace("{{JENKINS}}", readFileToString("jenkins.html"));
        } else {
            htmlContent = htmlContent.replace("{{JENKINS}}", "");
        }
        htmlContent = htmlContent.replace("{{USUARIO}}", user);
        htmlContent = htmlContent.replace("{{SENHA}}", pass);

        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }

    private static String readFileToString(String file) throws IOException {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(file)) {
            return new String(inputStream.readAllBytes());
        }
    }
}
