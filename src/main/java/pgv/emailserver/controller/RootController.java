package pgv.emailserver.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import pgv.emailserver.Conexion.DBManager;
import pgv.emailserver.controller.modelos.Email;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    private static final String SMTP_HOST = "127.0.0.1";
    private static final String SMTP_PORT = "25";
    private static final String IMAP_HOST = "127.0.0.1";
    private static final String IMAP_PORT = "143";

    // Model
    private ListProperty<Email> emails = new SimpleListProperty<>(FXCollections.observableArrayList(
            email -> new Observable[] { email.remitenteProperty(), email.fechaProperty() }
    ));

    private DBManager dbManager = new DBManager();

    private final String usuario;
    private final String password;

    private ObjectProperty<Email> selectedEmail = new SimpleObjectProperty<>();
    private EmailController emailController = new EmailController();

    // View
    @FXML
    private ListView<Email> emailListView;

    @FXML
    private Button enviarButton;

    @FXML
    private BorderPane root;

    @FXML
    private VBox emptyBox;


    @FXML
    void onEnviarAction(ActionEvent event) {
        // Mostrar diálogo y obtener datos
        Optional<String[]> result = mostrarDialogoEnvioCorreo();
        result.ifPresent(datos -> {
            String destinatario = datos[0];
            String asunto = datos[1];
            String mensaje = datos[2];
            System.out.println("destinatario: " + destinatario);
            if (enviarCorreo(usuario, password, destinatario, asunto, mensaje, LocalDate.now())) {
                mostrarAlerta("Éxito", "Correo enviado correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo enviar el correo.");
            }
        });
    }

    private boolean enviarCorreo(String user, String password, String destinatario, String asunto, String mensaje, LocalDate fecha) {
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
            message.setFrom(user + "@localhost.com");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);
            dbManager.AddEmail(new Email(user, destinatario, asunto, mensaje, fecha.toString()));
            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public RootController(String usuario, String password) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.usuario = usuario;
        this.password = password;
        emails.set(GetEmails());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailListView.itemsProperty().bind(emails);
        selectedEmail.bind(emailListView.getSelectionModel().selectedItemProperty());
        selectedEmail.addListener(this::onSelectedEmailChanged);
        emailController.emailProperty().bind(selectedEmail);
    }

    private void onSelectedEmailChanged(ObservableValue<? extends Email> o, Email ov, Email nv) {
        if (nv != null) {
            root.setCenter(emailController.getEmailRoot());
        } else {
            root.setCenter(emptyBox);
        }
    }

    private ObservableList<Email> GetEmails() {
        System.out.println(usuario + " " + password);
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", IMAP_HOST);
        props.put("mail.imap.port", IMAP_PORT);
        ObservableList<Email> emails = FXCollections.observableArrayList();
        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imap");
            store.connect(IMAP_HOST, usuario, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Email email = new Email();
                System.out.println(message.getFrom()[0]);
                String from = ((InternetAddress) message.getFrom()[0]).getAddress();
                email.setRemitente(from);
                email.setFecha(sdf.format(message.getReceivedDate()));
                email.setAsunto(message.getSubject());
                email.setMensaje(message.getContent().toString());
                emails.add(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }

    // Dialog de enviar correo
    private Optional<String[]> mostrarDialogoEnvioCorreo() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Enviar Correo");
        dialog.setHeaderText("Ingrese los detalles del correo:");

        // Crear campos de entrada
        TextField destinatarioField = new TextField();
        destinatarioField.setPromptText("Correo del destinatario");

        TextField asuntoField = new TextField();
        asuntoField.setPromptText("Asunto");

        TextArea mensajeArea = new TextArea();
        mensajeArea.setPromptText("Escriba el mensaje aquí");
        mensajeArea.setWrapText(true);
        mensajeArea.setPrefRowCount(4);

        // Organizar en un GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Para:"), 0, 0);
        grid.add(destinatarioField, 1, 0);
        grid.add(new Label("Asunto:"), 0, 1);
        grid.add(asuntoField, 1, 1);
        grid.add(new Label("Mensaje:"), 0, 2);
        grid.add(mensajeArea, 1, 2);

        // Hacer que las columnas se ajusten al contenido
        grid.getColumnConstraints().addAll(new ColumnConstraints(90), new ColumnConstraints(150));

        dialog.getDialogPane().setContent(grid);

        // Agregar botones
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String destinatario = destinatarioField.getText().trim();
            String asunto = asuntoField.getText().trim();
            String mensaje = mensajeArea.getText().trim();

            if (destinatario.isEmpty() || asunto.isEmpty() || mensaje.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return Optional.empty();
            }
            return Optional.of(new String[]{destinatario, asunto, mensaje});
        }

        return Optional.empty();
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
}
