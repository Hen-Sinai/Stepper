package flow.stepInfo.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogImpl implements Log {
    private final static SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.sss");
    private String data;
    private String timeStamp;

    public LogImpl() {}

    public LogImpl(String data) {
        this.data = data;
        this.timeStamp = sdfDate.format(new Date());
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Time: " + this.timeStamp + " - " + this.data;
    }
}
