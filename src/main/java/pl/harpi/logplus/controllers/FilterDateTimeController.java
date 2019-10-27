package pl.harpi.logplus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FilterDateTimeController implements Controllable<FilterDateTimeController.Filter, FilterDateTimeController.Filter, FilterDateTimeController> {
    private Filter result;

    @FXML
    private DatePicker dtprDateFrom;

    @FXML
    private TextField txfdTimeFrom;

    @FXML
    private DatePicker dtprDateTo;

    @FXML
    private TextField txfdTimeTo;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox chbxExclude;

    @FXML
    public void initialize() {
        btnApply.setOnAction(actionEvent -> {
            LocalDate localDateFrom = dtprDateFrom.getValue();
            String timeFrom = txfdTimeFrom.getText();

            LocalDate localDateTo = dtprDateTo.getValue();
            String timeTo = txfdTimeTo.getText();

            Date dateFrom = null;
            Date dateTo = null;

            boolean valid = true;

            if (localDateFrom == null || localDateTo == null || timeFrom == null || timeTo == null) {
                valid = false;
            } else {
                try {
                    dateFrom = DateUtils.parseDate(localDateFrom + " " + timeFrom, "yyyy-MM-dd HH:mm:ss,SSS");
                    dateTo = DateUtils.parseDate(localDateTo + " " + timeTo, "yyyy-MM-dd HH:mm:ss,SSS");
                } catch (ParseException e) {
                    valid = false;
                }
            }

            if (!valid) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("(I) Information");
                alert.setHeaderText("Please insert valid period.");
                alert.showAndWait();
                return;
            }

            result = new Filter(dateFrom, dateTo, chbxExclude.isSelected());
            ((Stage) (btnApply.getScene().getWindow())).close();
        });

        btnCancel.setOnAction(actionEvent -> {
            result = null;
            ((Stage) (btnCancel.getScene().getWindow())).close();
        });
    }

    @Override
    public Filter getResult() {
        return result;
    }

    @Override
    public String getResource() {
        return "filter_date_time.fxml";
    }

    @Override
    public void initialize(Filter filter) {
        if (filter == null) {
            return;
        }

        if (filter.getDateFrom() != null) {
            dtprDateFrom.setValue(Instant.ofEpochMilli(filter.getDateFrom().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            txfdTimeFrom.setText(Instant.ofEpochMilli(filter.getDateFrom().getTime()).atZone(ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS")));
        }

        if (filter.getDateTo() != null) {
            dtprDateTo.setValue(Instant.ofEpochMilli(filter.getDateTo().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            txfdTimeTo.setText(Instant.ofEpochMilli(filter.getDateTo().getTime()).atZone(ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS")));
        }

        chbxExclude.setSelected(filter.exclude);
    }

    @Getter
    @AllArgsConstructor
    public static class Filter {
        private Date dateFrom;
        private Date dateTo;
        private boolean exclude;
    }
}
