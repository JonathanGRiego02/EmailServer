package pgv.emailserver;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Scanner;

public class Prueba {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String host = "127.0.0.1";
        final String user = "usuario2@localhost.com";
        final String password = "usuario234";

        String to = "usuario1@localhost.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.localhost", "localhost");

        // Crear una sesión autenticada
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Crear el mensaje de correo
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user)); // Remitente
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // Destinatario
            message.setSubject("Correo para usuario1"); // Asunto
            System.out.println("Escribe el mensaje personalizado:");
            String mensajePersonalizado = scanner.nextLine();
            message.setText(mensajePersonalizado); // Cuerpo del mensaje

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Correo enviado exitosamente dentro del área local.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
