package pl.harpi.logplus.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.harpi.logplus.App;

import java.io.IOException;

public interface Controllable<INPUT, OUTPUT, CTRL extends Controllable<INPUT, OUTPUT, ?>> {
    void initialize(INPUT input);

    OUTPUT getResult();

    String getResource();

    default OUTPUT showAndWait(INPUT input, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource(getResource()));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(App.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        CTRL ctrl = fxmlLoader.getController();

        ctrl.initialize(input);

        stage.setTitle(title);
        stage.setResizable(false);

        stage.showAndWait();

        return ctrl.getResult();
    }
}
