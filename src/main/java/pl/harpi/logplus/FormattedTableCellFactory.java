package pl.harpi.logplus;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class FormattedTableCellFactory<S, T> implements Callback<TableColumn<TableLog, T>, TableCell<TableLog, T>> {

    public FormattedTableCellFactory() {
    }

    @Override
    public TableCell<TableLog, T> call(TableColumn<TableLog, T> p) {
        TableCell<TableLog, T> cell = new TableCell<TableLog, T>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                getStyleClass().remove("levelInfo");
                getStyleClass().remove("levelWarn");
                getStyleClass().remove("levelError");
                getStyleClass().remove("levelOther");

                super.updateItem((T) item, empty);

                String style = "levelOther";
                if (getTableRow() != null) {
                    TableLog log = getTableRow().getItem();
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

                setText(getItem() == null ? "" : getItem().toString());
            }
        };
        return cell;
    }
}
