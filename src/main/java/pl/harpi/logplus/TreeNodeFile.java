package pl.harpi.logplus;

import pl.harpi.logplus.services.Log;
import pl.harpi.logplus.services.LogFileService;
import pl.harpi.logplus.services.LogItem;

import java.io.File;
import java.util.List;

public class TreeNodeFile extends TreeNode {
    private final File file;
    private Log log;

    public TreeNodeFile(File file) {
        this.file = file;
        reload();
    }

    @Override
    public String toString() {
        return (file == null) ? "<<null>>" : file.getName();
    }

    @Override
    public List<LogItem> getItems() {
        return log.getItems();
    }

    @Override
    public void reload() {
        if (file != null) {
            log = new LogFileService().loadFile(file);
        }
    }
}
