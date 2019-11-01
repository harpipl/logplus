package pl.harpi.logplus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class FilterTextController implements Controllable<FilterTextController.Result, FilterTextController.Result, FilterTextController> {
    private Result result;

    @FXML
    private CheckBox chbxExclude;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField ttfdText;

    @FXML
    public void initialize() {
        btnApply.setOnAction(actionEvent -> {
            result = new Result(ttfdText.getText(), chbxExclude.isSelected());
            ((Stage) (btnApply.getScene().getWindow())).close();
        });

        btnCancel.setOnAction(actionEvent -> {
            result = null;
            ((Stage) (btnCancel.getScene().getWindow())).close();
        });
    }

    @Override
    public void initialize(Result result) {
        if (result == null) {
            return;
        }

        ttfdText.setText(result.getText());
        chbxExclude.setSelected(result.isExclude());
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public String getResource() {
        return "filter_text.fxml";
    }

    @AllArgsConstructor
    @Getter
    public static class Result {
        private String text;
        private boolean exclude;
    }
}
