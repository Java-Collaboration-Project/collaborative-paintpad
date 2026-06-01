package client.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import server.network.ServerMain;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Initial setup bypassing the immediate load of the Whiteboard
        LoginController loginController = new LoginController(primaryStage);
        loginController.show();
    }

    public static void main(String[] args) {
//        ServerMain.main(args);
        launch(args);
    }
}

//package client.ui;
//
//import javafx.application.Application;
//import javafx.stage.Stage;
//import client.network.ConnectionManager;
//import server.db.DatabaseInitializer;
//
//public class MainApp extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        ConnectionManager connectionManager = new ConnectionManager();
//        LoginController loginController = new LoginController(primaryStage, connectionManager);
//        loginController.show();
//    }
//
//    public static void main(String[] args) {
//        DatabaseInitializer.initialize();
//        launch(args);
//    }
//}

//package client.ui;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import server.db.DatabaseInitializer;
//
//import java.sql.SQLException;
//
//public class MainApp extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//
//        WhiteboardController controller = new WhiteboardController();
//
//        // Boosted width from 800 to 1200 so labels never turn into "..."
//        Scene scene = new Scene(controller.getRoot(), 1200, 800);
//
//        primaryStage.setTitle("Collaborative Whiteboard");
//        primaryStage.setScene(scene);
//
//        // Allowed resizing so users can maximize manually
//        primaryStage.setResizable(true);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) throws SQLException {
////        System.out.println(DatabaseManager.getConnection());
//        DatabaseInitializer.initialize();
//
//        launch(args);
//    }
//}