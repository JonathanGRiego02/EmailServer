package pgv.emailserver.Conexion;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {

    public void guardarUsuario(String username, String contraseña) {
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

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.guardarUsuario("usuario1", "usuario234");
        dbManager.guardarUsuario("usuario2", "usuario234");
    }
}
