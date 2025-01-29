package pgv.emailserver.controller.modelos;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Email {

    // Atributos
    private final StringProperty asunto = new SimpleStringProperty();
    private final StringProperty remitente = new SimpleStringProperty();
    private final StringProperty mensaje = new SimpleStringProperty();
    private final StringProperty fecha = new SimpleStringProperty();

    public Email() {}

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

    @Override
    public String toString() {
        return getRemitente() + "\n" + getFecha();
    }
}
