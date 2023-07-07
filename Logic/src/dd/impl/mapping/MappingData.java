package dd.impl.mapping;

import mySchema.stFlow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MappingData<K,V> implements Serializable {
    private final Map<K, V> data = new HashMap<>();

    public void add(K key, V value) {
        this.data.put(key, value);
    }
    public Map<K, V> get() {
        return this.data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : data.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        }
        return sb.toString();
    }
}
