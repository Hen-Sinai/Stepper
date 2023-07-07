package KeysSet;

import java.io.Serializable;

public interface keysSet<T,K> extends Serializable {
    T getMainKey();
    K getSecondaryKey();
    boolean equals(Object obj);
    int hashCode();
}
