package dd.impl.file;

import java.io.File;
import java.io.Serializable;

public class FileData implements Serializable {
    private final File file;
    private String selectedLineData;

    public FileData(String path) {
        this.file = new File(path);
    }

    public File getFile() {
        return this.file;
    }

    public String getSelectedLineData() {
        return selectedLineData;
    }

    public void setSelectedLineData(String data) {
        this.selectedLineData = data;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
