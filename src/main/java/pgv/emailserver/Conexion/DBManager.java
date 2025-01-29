package pgv.emailserver.Conexion;

import org.mindrot.jbcrypt.BCrypt;
import pgv.emailserver.controller.modelos.Email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DBManager {

    /*
    Esta clase (quitando que la necesitemos a posteriori para gestionar guardar los emails) tiene el método GuardarUsuario
    Básicamente es la manera de guardar los usuarios con la contraseña encriptada.
     */

    public void GuardarUsuario(String username, String contraseña) {
        if (username == null || username.isEmpty() || contraseña == null || contraseña.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario y la contraseña no pueden estar vacíos.");
        }

        String hash = BCrypt.hashpw(contraseña, BCrypt.gensalt());

        String sql = "INSERT INTO usuarios (Nombre_Usuario, contrasena) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);         // Nombre de usuario
            statement.setBytes(2, hash.getBytes());  // Contraseña cifrada en formato binario

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Usuario guardado correctamente.");
            } else {
                System.out.println("No se pudo guardar el usuario.");
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void AddEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo.");
        }

        String sql = "INSERT INTO emails (emailRemitente, emailDestinatario, asunto, mensaje, fecha) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.getRemitente());
            statement.setString(2, email.getDestinatario());
            statement.setString(3, email.getAsunto());
            statement.setString(4, email.getMensaje());
            // formatear fecha "dd-MM-yyyy"
            statement.setString(5, email.getFecha());

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Email guardado correctamente.");
            } else {
                System.out.println("No se pudo guardar el email.");
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el email: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.GuardarUsuario("usuario1", "usuario234");
        dbManager.GuardarUsuario("usuario2", "usuario234");
    }
}
