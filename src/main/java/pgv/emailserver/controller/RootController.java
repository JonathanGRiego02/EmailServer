package pgv.emailserver.controller;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import pgv.emailserver.controller.modelos.Email;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Properties;
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

    private String usuario;
    private String password;

    // View

    @FXML
    private ListView<Email> emailListView;

    @FXML
    private Button enviarButton;

    @FXML
    private BorderPane root;

    @FXML
    void onEnviarAction(ActionEvent event) {
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
                email.setRemitente(message.getFrom()[0].toString());
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

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
}
