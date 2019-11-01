package pl.harpi.logplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.harpi.logplus.controllers.MainController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        Scene scene = new Scene(new FXMLLoader(MainController.class.getResource("main.fxml")).load(), 1024, 768);
        scene.getStylesheets().add(MainController.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Log++");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
