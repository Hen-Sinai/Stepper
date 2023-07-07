package dataStructures;

import java.util.UUID;

public class HistoryFlowsData {
    private final UUID id;
    private final String name;
    private final String time;
    private final String result;

    public HistoryFlowsData(UUID id, String name, String time, String result) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
    public String getResult() {
        return result;
    }
}
