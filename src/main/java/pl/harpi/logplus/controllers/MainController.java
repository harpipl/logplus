package pl.harpi.logplus.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.time.DateUtils;
import pl.harpi.logplus.*;
import pl.harpi.logplus.services.LogItem;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    public MenuItem miOpenLogFile;

    @FXML
    public MenuItem miEditFilter;

    @FXML
    public Menu mnAddFilter;

    @FXML
    public MenuItem miCloseAllButThis;

    @FXML
    public MenuItem miCloseAll;

    @FXML
    public MenuItem miCloseView;

    @FXML
    public SeparatorMenuItem smiFilter;

    @FXML
    public MenuItem miReloadFile;

    @FXML
    private ContextMenu treeViewCM;

    @FXML
    private TreeView<TreeNode> treeView;

    @FXML
    private TableView<TableLog> tableView;

    @FXML
    private TextArea ttaaDetails;

    @FXML
    private SplitPane splitPane;

    @FXML
    public void initialize() {
        App.getStage().showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    splitPane.setDividerPositions(0.15);
                    observable.removeListener(this);
                }
            }
        });

        if (treeView.getRoot() == null) {
            treeView.setRoot(new TreeItem<>(new TreeNodeRoot()));
        }

        EventHandler<WindowEvent> eventShowing = e -> {
            TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

            if (selectedItem == null || selectedItem.getValue() instanceof TreeNodeRoot) {
                miOpenLogFile.setVisible(true);
                miReloadFile.setVisible(false);
                miEditFilter.setVisible(false);
                smiFilter.setVisible(false);
                mnAddFilter.setVisible(false);
                miCloseAllButThis.setVisible(false);
                miCloseView.setVisible(false);
                return;
            }

            if (selectedItem.getValue() instanceof TreeNodeFile) {
                miOpenLogFile.setVisible(false);
                miReloadFile.setVisible(true);
                miEditFilter.setVisible(false);
                smiFilter.setVisible(true);
                mnAddFilter.setVisible(true);
                miCloseAllButThis.setVisible(true);
                miCloseView.setVisible(true);
                return;
            }
            miOpenLogFile.setVisible(false);
            miReloadFile.setVisible(false);
            miEditFilter.setVisible(true);
            smiFilter.setVisible(true);
            mnAddFilter.setVisible(true);
            miCloseAllButThis.setVisible(true);
            miCloseView.setVisible(true);
        };

        treeViewCM.setOnShowing(eventShowing);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tableView.getItems().clear();

                TreeNode value = newValue.getValue();

                for (LogItem item : value.getItems()) {
                    tableView.getItems().add(
                            TableLog.builder()
                                    .date(item.getDate())
                                    .time(item.getTime())
                                    .level(item.getLevel())
                                    .logger(item.getLogger())
                                    .thread(item.getThread())
                                    .message(item.getMessage())
                                    .details(item.getDetails())
                                    .build());
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, tableLog, t1) -> {
            StringBuilder builder = new StringBuilder();
            if (tableView.getSelectionModel() != null && tableView.getSelectionModel().getSelectedItem() != null) {
                for (String detail : tableView.getSelectionModel().getSelectedItem().getDetails()) {
                    builder.append(detail).append("\n");
                }
            }
            ttaaDetails.setText(builder.toString());
        });
    }

    @FXML
    private void onOpenLogFile() {
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(App.getStage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeFile(file));
                treeView.getRoot().getChildren().add(treeItem);
                treeView.getSelectionModel().select(treeItem);
                tableView.scrollTo(0);
            }
        }
    }

    @FXML
    private void onReloadFile(ActionEvent actionEvent) {
        ((TreeNodeFile) treeView.getSelectionModel().getSelectedItem().getValue()).reload();
        tableView.scrollTo(0);
    }

    @FXML
    private void onCloseAll(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            selectedItem.getChildren().clear();
        }
    }

    @FXML
    private void onCloseAllButThis(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem.getParent() != null) {
            for (int i = selectedItem.getParent().getChildren().size(); i > 0; i--) {
                if (selectedItem != selectedItem.getParent().getChildren().get(i - 1)) {
                    selectedItem.getParent().getChildren().remove(i - 1);
                }
            }
        }
    }

    @FXML
    private void onCloseView(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem.getParent() != null) {
            selectedItem.getParent().getChildren().remove(selectedItem);
        }
    }

    @FXML
    private void onFilterDateTime(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        FilterDateTimeController.Filter input = null;
        if (!selectedItem.getValue().getItems().isEmpty()) {
            LogItem firstLogItem = selectedItem.getValue().getItems().get(0);
            LogItem lastLogItem = selectedItem.getValue().getItems().get(selectedItem.getValue().getItems().size() - 1);

            Date dateFrom = null;
            Date dateTo = null;
            try {
                dateFrom = DateUtils.parseDate(firstLogItem.getDate() + " " + firstLogItem.getTime(), "yyyy-MM-dd HH:mm:ss,SSS");
                dateTo = DateUtils.parseDate(lastLogItem.getDate() + " " + lastLogItem.getTime(), "yyyy-MM-dd HH:mm:ss,SSS");
            } catch (ParseException ex) {
                // NOP...
            }
            input = new FilterDateTimeController.Filter(dateFrom, dateTo, false);
        } else {
            input = new FilterDateTimeController.Filter(null, null, false);
        }

        FilterDateTimeController.Filter output = new FilterDateTimeController().showAndWait(input, "Create Date Time Filter");

        if (output != null) {
            TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeDateTime(selectedItem.getValue(), output));
            selectedItem.getChildren().add(treeItem);
            treeView.getSelectionModel().select(treeItem);
            tableView.scrollTo(0);
        }
    }

    @FXML
    private void onEditFilter(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.getValue() != null) {
            if (selectedItem.getValue() instanceof TreeNodeDateTime) {
                TreeNodeDateTime treeNode = (TreeNodeDateTime) selectedItem.getValue();
                FilterDateTimeController.Filter output = new FilterDateTimeController().showAndWait(treeNode.getParameters(), "Create Date Time Filter");
                if (output != null) {
                    ((TreeNodeDateTime) selectedItem.getValue()).setParameters(output);
                    treeView.getSelectionModel().select(null);
                    treeView.getSelectionModel().select(selectedItem);
                    tableView.scrollTo(0);
                }
            } else if (selectedItem.getValue() instanceof TreeNodeLevel) {
                TreeNodeLevel treeNode = (TreeNodeLevel) selectedItem.getValue();
                FilterLogLevelController.Result output = new FilterLogLevelController().showAndWait(treeNode.getParameters(), "Create Log Level Filter");
                if (output != null) {
                    ((TreeNodeLevel) selectedItem.getValue()).setParameters(output);
                    treeView.getSelectionModel().select(null);
                    treeView.getSelectionModel().select(selectedItem);
                    treeView.refresh();
                    tableView.scrollTo(0);
                }
            } else if (selectedItem.getValue() instanceof TreeNodeLogger) {
                TreeNodeLogger treeNode = (TreeNodeLogger) selectedItem.getValue();
                FilterLoggerController.Result output = new FilterLoggerController().showAndWait(treeNode.getParameters(), "Create Logger Filter");
                if (output != null) {
                    ((TreeNodeLogger) selectedItem.getValue()).setParameters(output);
                    treeView.getSelectionModel().select(null);
                    treeView.getSelectionModel().select(selectedItem);
                    treeView.refresh();
                    tableView.scrollTo(0);
                }
            } else if (selectedItem.getValue() instanceof TreeNodeThread) {
                TreeNodeThread treeNode = (TreeNodeThread) selectedItem.getValue();
                FilterLoggerController.Result output = new FilterLoggerController().showAndWait(treeNode.getParameters(), "Create Thread Filter");
                if (output != null) {
                    ((TreeNodeThread) selectedItem.getValue()).setParameters(output);
                    treeView.getSelectionModel().select(null);
                    treeView.getSelectionModel().select(selectedItem);
                    treeView.refresh();
                    tableView.scrollTo(0);
                }
            } else if (selectedItem.getValue() instanceof TreeNodeText) {
                TreeNodeText treeNode = (TreeNodeText) selectedItem.getValue();
                FilterTextController.Result output = new FilterTextController().showAndWait(treeNode.getParameters(), "Create Text Filter");
                if (output != null) {
                    ((TreeNodeText) selectedItem.getValue()).setParameters(output);
                    treeView.getSelectionModel().select(null);
                    treeView.getSelectionModel().select(selectedItem);
                    treeView.refresh();
                    tableView.scrollTo(0);
                }
            }
        }
    }

    @FXML
    private void onFilterLogLevel(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
        FilterLogLevelController.Result output = new FilterLogLevelController().showAndWait(null, "Create Log Level Filter");

        if (output != null) {
            TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeLevel(selectedItem.getValue(), output));
            selectedItem.getChildren().add(treeItem);
            treeView.getSelectionModel().select(treeItem);
            tableView.scrollTo(0);
        }
    }

    @FXML
    private void onFilterLogger(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        List<LogItem> items = selectedItem.getValue().getItems();
        List<String> stringList = items.stream().map(LogItem::getLogger).collect(Collectors.toList());

        FilterLoggerController.Result input = new FilterLoggerController.Result(stringList, new ArrayList<>(), false);
        FilterLoggerController.Result output = new FilterLoggerController().showAndWait(input, "Create Logger Filter");

        if (output != null) {
            TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeLogger(selectedItem.getValue(), output));
            selectedItem.getChildren().add(treeItem);
            treeView.getSelectionModel().select(treeItem);
            tableView.scrollTo(0);
        }
    }

    @FXML
    private void onFilterThread(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

        List<LogItem> items = selectedItem.getValue().getItems();
        List<String> stringList = items.stream().map(LogItem::getThread).collect(Collectors.toList());

        FilterLoggerController.Result input = new FilterLoggerController.Result(stringList, new ArrayList<>(), false);
        FilterLoggerController.Result output = new FilterLoggerController().showAndWait(input, "Create Thread Filter");

        if (output != null) {
            TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeThread(selectedItem.getValue(), output));
            selectedItem.getChildren().add(treeItem);
            treeView.getSelectionModel().select(treeItem);
            tableView.scrollTo(0);
        }
    }

    @FXML
    private void onFilterText(ActionEvent actionEvent) {
        TreeItem<TreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
        FilterTextController.Result input = new FilterTextController.Result(null, false);
        FilterTextController.Result output = new FilterTextController().showAndWait(input, "Create Text Filter");

        if (output != null) {
            TreeItem<TreeNode> treeItem = new TreeItem<>(new TreeNodeText(selectedItem.getValue(), output));
            selectedItem.getChildren().add(treeItem);
            treeView.getSelectionModel().select(treeItem);
            tableView.scrollTo(0);
        }
    }
}
