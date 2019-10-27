package pl.harpi.logplus.services;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Log {
    private String name;
    private List<LogItem> items = new ArrayList<>();
}
