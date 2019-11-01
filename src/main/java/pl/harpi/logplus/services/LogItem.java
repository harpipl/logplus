package pl.harpi.logplus.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class LogItem {
    private Integer id;
    private String date;
    private String time;
    private String level;
    private String logger;
    private String thread;
    private String message;
    private List<String> details;
}
