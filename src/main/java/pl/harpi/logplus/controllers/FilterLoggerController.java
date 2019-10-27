package pl.harpi.logplus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class FilterLoggerController implements Controllable<FilterLoggerController.Result, FilterLoggerController.Result, FilterLoggerController> {
    private Result result;

    @FXML
    private ListView<String> ltvwLeft;

    @FXML
    private ListView<String> ltvwRight;

    @FXML
    private Button btnRightOne;

    @FXML
    private Button btnRightAll;

    @FXML
    private Button btnLeftOne;

    @FXML
    private Button btnLeftAll;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox chbxExclude;

    @FXML
    public void initialize() {
        btnRightOne.setOnAction(actionEvent -> {
            String selectedItem = ltvwLeft.getSelectionModel().getSelectedItem();
            if (!StringUtils.isEmpty(selectedItem)) {
                ltvwRight.getItems().add(selectedItem);
                ltvwLeft.getItems().remove(selectedItem);
            }
        });

        btnRightAll.setOnAction(actionEvent -> {
            ltvwRight.getItems().addAll(ltvwLeft.getItems());
            ltvwLeft.getItems().clear();
        });

        btnLeftOne.setOnAction(actionEvent -> {
            String selectedItem = ltvwRight.getSelectionModel().getSelectedItem();
            if (!StringUtils.isEmpty(selectedItem)) {
                ltvwLeft.getItems().add(selectedItem);
                ltvwRight.getItems().remove(selectedItem);
            }
        });

        btnLeftAll.setOnAction(actionEvent -> {
            ltvwLeft.getItems().addAll(ltvwRight.getItems());
            ltvwRight.getItems().clear();
        });

        btnApply.setOnAction(actionEvent -> {
            result = new Result(new ArrayList<>(), new ArrayList<>(), chbxExclude.isSelected());
            result.getSelectedLoggers().addAll(ltvwRight.getItems());
            result.getAvailableLoggers().addAll(ltvwLeft.getItems());

            ((Stage) (btnApply.getScene().getWindow())).close();
        });

        btnCancel.setOnAction(actionEvent -> {
            result = null;
            ((Stage) (btnCancel.getScene().getWindow())).close();
        });
    }

    @Override
    public void initialize(Result result) {
        Set<String> loggersLeft = new HashSet<>(result.getAvailableLoggers());
        List<String> loggersListLeft = new ArrayList<>(loggersLeft);
        Collections.sort(loggersListLeft);

        Set<String> loggersRight = new HashSet<>(result.getSelectedLoggers());
        List<String> loggersListRight = new ArrayList<>(loggersRight);
        Collections.sort(loggersListRight);

        ltvwLeft.getItems().addAll(loggersListLeft);
        ltvwRight.getItems().addAll(loggersListRight);

        chbxExclude.setSelected(result.isExclude());
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public String getResource() {
        return "filter_logger.fxml";
    }

    @Getter
    @AllArgsConstructor
    public static class Result {
        private List<String> availableLoggers;
        private List<String> selectedLoggers;
        private boolean exclude;
    }
}
