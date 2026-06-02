package client.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import server.network.ServerMain;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginController loginController = new LoginController(primaryStage);
        loginController.show();
    }

    public static void main(String[] args) {
//        ServerMain.main(args);
        launch(args);
    }
}