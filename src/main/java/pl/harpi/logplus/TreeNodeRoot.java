package pl.harpi.logplus;

import pl.harpi.logplus.services.LogItem;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeRoot extends TreeNode {
    @Override
    public String toString() {
        return "Files";
    }

    @Override
    public List<LogItem> getItems() {
        return new ArrayList<>();
    }

    @Override
    public void reload() {

    }
}
