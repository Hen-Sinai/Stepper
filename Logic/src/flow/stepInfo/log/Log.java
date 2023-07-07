package flow.stepInfo.log;

import java.io.Serializable;

public interface Log extends Serializable {
    String getData();
    String getTimeStamp();
}
