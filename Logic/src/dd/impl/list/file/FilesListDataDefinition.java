package dd.impl.list.file;

import dd.api.AbstractDataDefinition;

public class FilesListDataDefinition extends AbstractDataDefinition {
    public FilesListDataDefinition() {
        super("File List", false, FileListData.class);
    }

    @Override
    public String getUserPresentation() {
        return "List of files";
    }
}
