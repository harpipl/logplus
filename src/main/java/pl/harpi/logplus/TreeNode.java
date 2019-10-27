package pl.harpi.logplus;

import pl.harpi.logplus.services.LogItem;

import java.io.Serializable;
import java.util.List;

public abstract class TreeNode implements Serializable {
    public abstract List<LogItem> getItems();

    public abstract void reload();
}
