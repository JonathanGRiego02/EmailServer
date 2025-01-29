package pgv.emailserver;

import atlantafx.base.theme.NordDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pgv.emailserver.controller.LogginController;

public class EmailApp extends Application {

    private LogginController loginController = new LogginController();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(loginController.getRoot());
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

}
