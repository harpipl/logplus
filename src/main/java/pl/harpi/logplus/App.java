package pl.harpi.logplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
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

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(MainController.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setTitle("Log++");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
