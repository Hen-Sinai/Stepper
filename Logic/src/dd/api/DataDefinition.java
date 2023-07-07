package dd.api;

import java.io.Serializable;

public interface DataDefinition extends Serializable {
    String getName();
    String getUserPresentation();
    boolean isUserFriendly();
    Class<?> getType();
}
