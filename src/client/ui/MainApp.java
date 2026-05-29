package client.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import server.db.DatabaseInitializer;

import java.sql.SQLException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        WhiteboardController controller = new WhiteboardController();

        // Boosted width from 800 to 1200 so labels never turn into "..."
        Scene scene = new Scene(controller.getRoot(), 1200, 800);

        primaryStage.setTitle("Collaborative Whiteboard");
        primaryStage.setScene(scene);

        // Allowed resizing so users can maximize manually
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
//        System.out.println(DatabaseManager.getConnection());
        DatabaseInitializer.initialize();

        launch(args);
    }
}