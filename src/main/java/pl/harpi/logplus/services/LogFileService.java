package pl.harpi.logplus.services;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFileService {
    public Log loadFile(File file) {
        Log logFile = new Log();
        logFile.setName(file.getName());

        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3})\\s([^\\s]*)\\s+\\[(.*)\\]\\s+\\((.*?)\\)(.*)");

        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");

            int i = 0;
            LogItem item = null;
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.lookingAt()) {
                    ArrayList<String> details = new ArrayList<>();
                    details.add(matcher.group(6));
                    item = LogItem.builder()
                            .id(++i)
                            .date(matcher.group(1))
                            .time(matcher.group(2))
                            .level(matcher.group(3))
                            .logger(matcher.group(4))
                            .thread(matcher.group(5))
                            .message(matcher.group(6))
                            .details(details)
                            .build();
                    logFile.getItems().add(item);
                } else {
                    if (item != null) {
                        item.getDetails().add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logFile;
    }

}
