package pl.harpi.logplus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class TableLog {
    private String date;
    private String time;
    private String level;
    private String logger;
    private String thread;
    private String message;
    private List<String> details;
}
