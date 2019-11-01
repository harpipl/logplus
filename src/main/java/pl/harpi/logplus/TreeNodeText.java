package pl.harpi.logplus;

import org.apache.commons.lang3.StringUtils;
import pl.harpi.logplus.controllers.FilterTextController;
import pl.harpi.logplus.services.LogItem;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeText extends TreeNode {
    private TreeNode parent;

    private FilterTextController.Result parameters;

    public TreeNodeText(TreeNode parent, FilterTextController.Result parameters) {
        this.parent = parent;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return ((parameters.isExclude()) ? "! Text: " + parameters.getText() : "Text: " + parameters.getText());
    }

    @Override
    public List<LogItem> getItems() {
        if (StringUtils.isEmpty(parameters.getText())) {
            return parent.getItems();
        }

        List<LogItem> result = new ArrayList<>();

        if (parameters.isExclude()) {
            for (LogItem item : parent.getItems()) {
                boolean fit = true;

                for (String detail : item.getDetails()) {
                    if (detail.toUpperCase().contains(parameters.getText().toUpperCase())) {
                        fit = false;
                        break;
                    }
                }

                if (fit) {
                    result.add(item);
                }
            }
        } else {
            for (LogItem item : parent.getItems()) {
                boolean fit = false;

                for (String detail : item.getDetails()) {
                    if (detail.toUpperCase().contains(parameters.getText().toUpperCase())) {
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

    public FilterTextController.Result getParameters() {
        return parameters;
    }

    public void setParameters(FilterTextController.Result parameters) {
        this.parameters = parameters;
    }

    @Override
    public void reload() {

    }
}
