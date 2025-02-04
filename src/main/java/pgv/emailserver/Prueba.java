package pgv.emailserver;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.Scanner;

public class Prueba {

    private static final String SMTP_HOST = "127.0.0.1";
    private static final String SMTP_PORT = "25";
    private static final String IMAP_HOST = "127.0.0.1";
    private static final String IMAP_PORT = "143";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        String user = "usuario1@localhost.com";
        String password = "usuario234";
        String destinatario = "Dani@localhost.com";

        while (true) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Enviar un correo");
            System.out.println("2. Listar correos recibidos");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer del scanner

            switch (opcion) {
                case 1:
                    enviarCorreo(user, password, destinatario, scanner);
                    break;
                case 2:
                    listarCorreos(user, password);
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private static void enviarCorreo(String user, String password, String destinatario, Scanner scanner) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.localhost", "localhost");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Correo para " + destinatario);

            System.out.println("Escribe el mensaje:");
            String mensajePersonalizado = scanner.nextLine();
            message.setText(mensajePersonalizado);

            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void listarCorreos(String user, String password) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", IMAP_HOST);
        props.put("mail.imap.port", IMAP_PORT);

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imap");
            store.connect(IMAP_HOST, user, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("\nCorreos recibidos:");
            for (Message message : messages) {
                System.out.println("-----------------------------");
                System.out.println("De: " + message.getFrom()[0]);
                System.out.println("Asunto: " + message.getSubject());
                System.out.println("Fecha: " + message.getReceivedDate());
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
