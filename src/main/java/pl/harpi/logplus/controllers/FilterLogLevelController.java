package pl.harpi.logplus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FilterLogLevelController implements Controllable<FilterLogLevelController.Result, FilterLogLevelController.Result, FilterLogLevelController> {
    private Result result;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox chbxFatal;

    @FXML
    private CheckBox chbxError;

    @FXML
    private CheckBox chbxWarn;

    @FXML
    private CheckBox chbxInfo;

    @FXML
    private CheckBox chbxDebug;

    @FXML
    private CheckBox chbxExclude;

    @FXML
    public void initialize() {
        btnApply.setOnAction(actionEvent -> {
            result = new Result(new ArrayList<>(), chbxExclude.isSelected());
            if (chbxFatal.isSelected()) {
                result.getLevels().add("FATAL");
            }

            if (chbxError.isSelected()) {
                result.getLevels().add("ERROR");
            }

            if (chbxWarn.isSelected()) {
                result.getLevels().add("WARN");
            }

            if (chbxInfo.isSelected()) {
                result.getLevels().add("INFO");
            }

            if (chbxDebug.isSelected()) {
                result.getLevels().add("DEBUG");
            }

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

        chbxExclude.setSelected(result.isExclude());

        for (String level : result.getLevels()) {
            switch (level) {
                case "INFO":
                    chbxInfo.setSelected(true);
                    break;
                case "WARN":
                    chbxWarn.setSelected(true);
                    break;
                case "DEBUG":
                    chbxDebug.setSelected(true);
                    break;
                case "ERROR":
                    chbxError.setSelected(true);
                    break;
                case "FATAL":
                    chbxFatal.setSelected(true);
                    break;
            }
        }
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public String getResource() {
        return "filter_log_level.fxml";
    }

    @AllArgsConstructor
    @Getter
    public static class Result {
        private List<String> levels;
        private boolean exclude;
    }
}
