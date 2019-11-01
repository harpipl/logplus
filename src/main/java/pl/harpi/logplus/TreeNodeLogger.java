package pl.harpi.logplus;

import pl.harpi.logplus.controllers.FilterLoggerController;
import pl.harpi.logplus.services.LogItem;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeLogger extends TreeNode {
    private TreeNode parent;

    private FilterLoggerController.Result parameters;

    public TreeNodeLogger(TreeNode parent, FilterLoggerController.Result parameters) {
        this.parent = parent;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder string = null;
        for (String level : parameters.getSelectedLoggers()) {
            if (string == null) {
                string = parameters.isExclude() ? new StringBuilder("! Logger: ").append(level) : new StringBuilder("Logger: ").append(level);
            } else {
                string.append(" & ").append(level);
            }
        }

        return (string == null) ? "<null>" : string.toString();
    }

    @Override
    public List<LogItem> getItems() {
        if (parameters == null) {
            return parent.getItems();
        }

        List<LogItem> result = new ArrayList<>();

        if (parameters.isExclude()) {
            for (LogItem item: parent.getItems()) {
                boolean fit = true;

                for (String level: parameters.getSelectedLoggers()) {
                    if (item.getLogger().equals(level)) {
                        fit = false;
                        break;
                    }
                }

                if (fit) {
                    result.add(item);
                }
            }
        } else {
            for (LogItem item: parent.getItems()) {
                boolean fit = false;

                for (String level: parameters.getSelectedLoggers()) {
                    if (item.getLogger().equals(level)) {
                        fit = true;
                        break;
                    }
                }
                if (fit) {
                    result.add(item);
                }
            }
        }

        return result;
    }

    public FilterLoggerController.Result getParameters() {
        return parameters;
    }

    public void setParameters(FilterLoggerController.Result parameters) {
        this.parameters = parameters;
    }

    @Override
    public void reload() {

    }
}
