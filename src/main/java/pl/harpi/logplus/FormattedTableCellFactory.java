package pl.harpi.logplus;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class FormattedTableCellFactory implements Callback<TableColumn<TableLog, String>, TableCell<TableLog, String>> {

    public FormattedTableCellFactory() {
    }

    @Override
    public TableCell<TableLog, String> call(TableColumn<TableLog, String> p) {
        return new TableCell<TableLog, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                getStyleClass().remove("levelInfo");
                getStyleClass().remove("levelWarn");
                getStyleClass().remove("levelError");
                getStyleClass().remove("levelOther");

                super.updateItem(item, empty);

                String style = "levelOther";
                if (getTableRow() != null) {
                    TableLog log = (TableLog) getTableRow().getItem();
                    if (log != null) {
                        switch (log.getLevel()) {
                            case "INFO":
                                style = "levelInfo";
                                break;
                            case "WARN":
                                style = "levelWarn";
                                break;
                            case "ERROR":
                                style = "levelError";
                                break;
                        }
                    }
                }

                getStyleClass().add(style);

                setText(getItem() == null ? "" : getItem());
            }
        };
    }
}
