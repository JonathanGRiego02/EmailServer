package pgv.emailserver.controller;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pgv.emailserver.controller.modelos.Email;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailController implements Initializable {

    private ObjectProperty<Email> email = new SimpleObjectProperty<>();

    // View

    @FXML
    private GridPane emailRoot;

    @FXML
    private TextField asuntoTextField;

    @FXML
    private Label deLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private TextArea mensajeTextField;


    public EmailController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmailView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        email.addListener(this::onEmailChanged);

    }

    private void onEmailChanged(ObservableValue<? extends Email> o, Email oldValue, Email newValue) {
        if (oldValue != null) {
            asuntoTextField.textProperty().unbindBidirectional(oldValue.asuntoProperty());
            deLabel.textProperty().unbindBidirectional(oldValue.remitenteProperty());
            fechaLabel.textProperty().unbindBidirectional(oldValue.fechaProperty());
            mensajeTextField.textProperty().unbindBidirectional(oldValue.mensajeProperty());
        }
        if (newValue != null) {
            asuntoTextField.textProperty().bindBidirectional(newValue.asuntoProperty());
            deLabel.textProperty().bindBidirectional(newValue.remitenteProperty());
            fechaLabel.textProperty().bindBidirectional(newValue.fechaProperty());
            mensajeTextField.textProperty().bindBidirectional(newValue.mensajeProperty());
        }
    }

    public GridPane getEmailRoot() {
        return emailRoot;
    }

    public void setEmailRoot(GridPane emailRoot) {
        this.emailRoot = emailRoot;
    }

    public Email getEmail() {
        return email.get();
    }

    public ObjectProperty<Email> emailProperty() {
        return email;
    }

    public void setEmail(Email email) {
        this.email.set(email);
    }
}
