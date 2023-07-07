package KeysSet;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractKeysSet<T, K> implements keysSet<T, K>, Serializable {
    private final T mainKey;
    private final K secondaryKey;

    public AbstractKeysSet(T mainKey, K secondaryKey) {
        this.mainKey = mainKey;
        this.secondaryKey = secondaryKey;
    }

    @Override
    public T getMainKey() {
        return this.mainKey;
    }

    @Override
    public K getSecondaryKey() {
        return this.secondaryKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this){
            return true;
        }

        if (!(obj instanceof AbstractKeysSet)) {
            return false;
        }

        final AbstractKeysSet<T, K> other = (AbstractKeysSet<T, K>) obj;
        return (Objects.equals(this.mainKey, other.mainKey)) ||
                (Objects.equals(this.secondaryKey, other.secondaryKey));
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainKey, secondaryKey);
    }
}
