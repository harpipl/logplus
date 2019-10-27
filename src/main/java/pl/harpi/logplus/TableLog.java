package pl.harpi.logplus;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableLog {
    private String date;
    private String time;
    private String level;
    private String logger;
    private String thread;
    private String message;
}
