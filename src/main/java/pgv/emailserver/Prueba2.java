package pgv.emailserver;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Prueba2 {
    public static void main(String[] args) {
        // Configuración del servidor POP3
        String host = "127.0.0.1"; // Dirección IP o nombre del servidor
        String mailStoreType = "pop3"; // Protocolo POP3
        String user = "usuario2@localhost.com"; // Usuario del buzón
        String password = "usuario234"; // Contraseña del usuario

        try {
            // Configuración de propiedades para POP3
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "110"); // Puerto predeterminado de POP3
            properties.put("mail.pop3.auth", "true");

            // Obtener la sesión
            Session emailSession = Session.getDefaultInstance(properties);

            // Crear un objeto Store y conectarse al servidor
            Store store = emailSession.getStore(mailStoreType);
            store.connect(host, user, password);

            // Obtener la carpeta "INBOX"
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Obtener mensajes
            Message[] messages = emailFolder.getMessages();

            System.out.println("Cantidad de correos: " + messages.length);

            // Iterar sobre los correos y mostrar información
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("Correo #" + (i + 1));
                System.out.println("Asunto: " + message.getSubject());
                System.out.println("Remitente: " + message.getFrom()[0]);
                System.out.println("Contenido:");

                // Obtener el contenido del mensaje
                Object content = message.getContent();
                if (content instanceof String) {
                    System.out.println(content); // Texto simple
                } else if (content instanceof MimeMultipart) {
                    MimeMultipart mimeMultipart = (MimeMultipart) content;
                    System.out.println(getTextFromMimeMultipart(mimeMultipart));
                }
                System.out.println("---------------------------------");
            }

            // Cerrar las conexiones
            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para extraer texto de un MimeMultipart
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}