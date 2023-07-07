package DTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogDTO {
    private final String data;
    private final String timeStamp;

    public LogDTO(String data, String timeStamp) {
        this.data = data;
        this.timeStamp = timeStamp;
    }

    public String getData() {
        return this.data;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Time: " + this.timeStamp + " - " + this.data;
    }
}
