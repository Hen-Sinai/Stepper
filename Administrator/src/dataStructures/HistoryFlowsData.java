package dataStructures;

import java.util.UUID;

public class HistoryFlowsData {
    private final UUID id;
    private final String name;
    private final String time;
    private final String result;
    private final String username;
    private final String isRanByManager;

    public HistoryFlowsData(UUID id, String name, String time, String result, String username, boolean isRanByManager) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.result = result;
        this.username = username;
        this.isRanByManager = String.valueOf(isRanByManager);
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

    public String getUsername() {
        return this.username;
    }

    public String getIsRanByManager() {
        return this.isRanByManager;
    }
}
