package pgv.emailserver.controller.modelos;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Email {

    // Atributos
    private final StringProperty asunto = new SimpleStringProperty();
    private final StringProperty remitente = new SimpleStringProperty();
    private final StringProperty destinatario = new SimpleStringProperty();
    private final StringProperty mensaje = new SimpleStringProperty();
    private final StringProperty fecha = new SimpleStringProperty();

    public Email() {}

    public Email(String asunto, String remitente, String destinatario, String mensaje, String fecha) {
        this.asunto.set(asunto);
        this.remitente.set(remitente);
        this.destinatario.set(destinatario);
        this.mensaje.set(mensaje);
        this.fecha.set(fecha);
    }

    public String getFecha() {
        return fecha.get();
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getAsunto() {
        return asunto.get();
    }

    public StringProperty asuntoProperty() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto.set(asunto);
    }

    public String getRemitente() {
        return remitente.get();
    }

    public StringProperty remitenteProperty() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente.set(remitente);
    }

    public String getMensaje() {
        return mensaje.get();
    }

    public StringProperty mensajeProperty() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje.set(mensaje);
    }

    public String getDestinatario() {
        return destinatario.get();
    }

    public StringProperty destinatarioProperty() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario.set(destinatario);
    }

    @Override
    public String toString() {
        return getRemitente() + "\n" + getFecha();
    }
}
