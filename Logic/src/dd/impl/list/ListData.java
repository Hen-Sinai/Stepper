package dd.impl.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListData<T> implements Serializable {
    private final List<T> list = new ArrayList<>();
    public void addData(T data) {
        this.list.add(data);
    }
    public List<T> getData() {
        return this.list;
    }

    @Override
    public String toString() {
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        for (T item : list) {
            sb.append(counter).append(". ").append(item.toString()).append("\n");
            counter++;
        }
        return sb.toString();
    }
}
