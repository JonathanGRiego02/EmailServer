package pgv.emailserver.controller;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.mindrot.jbcrypt.*;
import pgv.emailserver.Conexion.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class LogginController implements Initializable {

    // Model
    private RootController rootController;


    // View

    @FXML
    private Button CreateUser;

    @FXML
    private Button clearButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private GridPane root;

    @FXML
    private TextField usuario;

    @FXML
    void onClearAction(ActionEvent event) {
            usuario.clear();
            passwordField.clear();
    }

    @FXML
    void onCreateUserAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Crear Usuario");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogginButtonAction(ActionEvent event) {
        String username = usuario.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Por favor, introducir un nombre de usuario o una contraseña");
            alert.showAndWait();
        }
        if (validate(username, password)) {
            System.out.println("Usuario autenticado");
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            rootController = new RootController(username, password);
            Scene scene = new Scene(rootController.getRoot());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Email");
            primaryStage.show();
        }

    }

    // Checkea que tus credenciales son correctas
    private boolean validate(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE Nombre_Usuario = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Asigna el valor del parámetro
            statement.setString(1, username);

            // Ejecuta la consulta
            ResultSet resultSet = statement.executeQuery();

            // Verifica si existe un resultado
            if (resultSet.next()) {
                String contrasenaCifrada = resultSet.getString("contrasena");
                // Compara la contraseña ingresada con la cifrada en la base de datos
                if (contrasenaCifrada != null && !contrasenaCifrada.isEmpty()) {
                    return BCrypt.checkpw(password, contrasenaCifrada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Hash inválido: " + e.getMessage());
        }
        return false;
    }

    public LogginController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Loggin.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public Button getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(Button createUser) {
        CreateUser = createUser;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public void setClearButton(Button clearButton) {
        this.clearButton = clearButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public GridPane getRoot() {
        return root;
    }

    public void setRoot(GridPane root) {
        this.root = root;
    }

    public TextField getUsuario() {
        return usuario;
    }

    public void setUsuario(TextField usuario) {
        this.usuario = usuario;
    }
}
