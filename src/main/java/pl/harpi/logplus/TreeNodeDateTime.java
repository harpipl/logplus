package pl.harpi.logplus;

import org.apache.commons.lang3.time.DateUtils;
import pl.harpi.logplus.controllers.FilterDateTimeController;
import pl.harpi.logplus.services.LogItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TreeNodeDateTime extends TreeNode {
    private TreeNode parent;

    private FilterDateTimeController.Filter parameters;

    public TreeNodeDateTime(TreeNode parent, FilterDateTimeController.Filter parameters) {
        this.parent = parent;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String dateFrom = simpleDateFormat.format(parameters.getDateFrom());
        String dateTo = simpleDateFormat.format(parameters.getDateTo());

        if (parameters.isExclude()) {
            string.append("! ");
        }

        string.append(dateFrom).append(" - ").append(dateTo);

        return string.toString();
    }

    @Override
    public List<LogItem> getItems() {
        List<LogItem> items = new ArrayList<>();

        for (LogItem item: parent.getItems()) {
            try {
                Date date = DateUtils.parseDate(item.getDate() + " " + item.getTime(), "yyyy-MM-dd HH:mm:ss,SSS");
                if (parameters.isExclude()) {
                    if (date.before(parameters.getDateFrom()) || date.after(parameters.getDateTo())) {
                        items.add(item);
                    }
                } else {
                    if (!date.before(parameters.getDateFrom()) && !date.after(parameters.getDateTo())) {
                        items.add(item);
                    }
                }
            } catch (ParseException e) {
                // NOP...
            }
        }

        return items;
    }

    @Override
    public void reload() {

    }

    public FilterDateTimeController.Filter getParameters() {
        return parameters;
    }

    public void setParameters(FilterDateTimeController.Filter parameters) {
        this.parameters = parameters;
    }
}
