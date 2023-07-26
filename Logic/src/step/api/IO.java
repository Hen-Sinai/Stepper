package step.api;

import dd.api.DataDefinition;
import dd.impl.DataDefinitionRegistry;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public enum IO implements Serializable {
    TIME_TO_SPEND("TIME_TO_SPEND", DataDefinitionRegistry.NUMBER, new HashSet<String>() {{
        add("Spend Some Time");
    }}),
    FOLDER_NAME("FOLDER_NAME", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Collect Files In Folder");
    }}),
    FILTER("FILTER", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Collect Files In Folder");
    }}),
    FILES_LIST("FILES_LIST", DataDefinitionRegistry.FILES_LIST, new HashSet<String>() {{
        add("Collect Files In Folder");
        add("Files Deleter");
        add("Files Content Extractor");
    }}),
    TOTAL_FOUND("TOTAL_FOUND", DataDefinitionRegistry.NUMBER, new HashSet<String>() {{
        add("Collect Files In Folder");
    }}),
    DELETED_LIST("DELETED_LIST", DataDefinitionRegistry.STRING_LIST, new HashSet<String>() {{
        add("Files Deleter");
    }}),
    DELETION_STATS("DELETION_STATS", DataDefinitionRegistry.STRING2NUMBER_MAP, new HashSet<String>() {{
        add("Files Deleter");
    }}),
    FILES_TO_RENAME("FILES_TO_RENAME", DataDefinitionRegistry.FILES_LIST, new HashSet<String>() {{
        add("Files Renamer");
    }}),
    PREFIX("PREFIX", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Files Renamer");
    }}),
    SUFFIX("SUFFIX", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Files Renamer");
    }}),
    RENAME_RESULT("RENAME_RESULT", DataDefinitionRegistry.RELATION, new HashSet<String>() {{
        add("Files Renamer");
    }}),
    SOURCE("SOURCE", DataDefinitionRegistry.RELATION, new HashSet<String>() {{
        add("CSV Exporter");
        add("Properties Exporter");
    }}),
    SOURCE_ZIPPER("SOURCE", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Zipper");
    }}),
    RESULT("RESULT", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("CSV Exporter");
        add("Properties Exporter");
        add("File Dumper");
        add("Zipper");
        add("Command Line");
    }}),
    CONTENT("CONTENT", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("File Dumper");
        add("To Json");
    }}),
    LINE("LINE", DataDefinitionRegistry.NUMBER, new HashSet<String>() {{
        add("Files Content Extractor");
    }}),
    DATA("DATA", DataDefinitionRegistry.RELATION, new HashSet<String>() {{
        add("Files Content Extractor");
    }}),
    FILE_NAME("FILE_NAME", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("File Dumper");
    }}),
    OPERATION("OPERATION", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Zipper");
    }}),
    COMMAND("COMMAND", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Command Line");
    }}),
    ARGUMENTS("ARGUMENTS", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Command Line");
    }}),
    ADDRESS("ADDRESS", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    PROTOCOL("PROTOCOL", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    METHOD("METHOD", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    BODY("BODY", DataDefinitionRegistry.JSON, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    CODE("CODE", DataDefinitionRegistry.NUMBER, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    RESPONSE_BODY("RESPONSE_BODY", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    RESOURCE("RESOURCE", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("HTTP Call");
    }}),
    JSON("JSON", DataDefinitionRegistry.JSON, new HashSet<String>() {{
        add("To Json");
        add("Json Data Extractor");
    }}),
    JSON_PATH("JSON_PATH", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Json Data Extractor");
    }}),
    VALUE("VALUE", DataDefinitionRegistry.STRING, new HashSet<String>() {{
        add("Json Data Extractor");
    }});
    private final String name;
    private final DataDefinition dataDefinition;
    private final Set<String> connectedSteps;

    IO (String name, DataDefinition dataDefinition, Set<String> connectedSteps) {
        this.name = name;
        this.dataDefinition = dataDefinition;
        this.connectedSteps = connectedSteps;
    }

    public String getName() {
        return this.name;
    }

    public DataDefinition getDataDefinition() {
        return this.dataDefinition;
    }

    public Set<String> getConnectedSteps() {
        return this.connectedSteps;
    }

    public static IO getIOByNameAndStep(String inputName, String stepName) {
        for (IO io : IO.values()) {
            if (io.getName().equals(inputName)) {
                if (io.getConnectedSteps().contains(stepName))
                    return io;
            }
        }
        return null; // no matching enum was found
    }
}
