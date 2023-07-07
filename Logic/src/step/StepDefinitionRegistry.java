package step;

import step.api.StepDefinition;
import step.impl.*;

import java.io.Serializable;

public enum StepDefinitionRegistry implements Serializable {
    SPEND_SOME_TIME(new SpendSomeTime()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolder()),
    FILES_DELETER(new FilesDeleter()),
    FILES_RENAMER(new FilesRenamer()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractor()),
    CSV_EXPORTER(new CSVExporter()),
    PROPERTIES_EXPORTER(new PropertiesExporter()),
    FILE_DUMPER(new FileDumper()),
    ZIPPER(new Zipper()),
    COMMAND_LINE(new CommandLine()),
    ;

    private final String name;
    private final StepDefinition stepDefinition;

    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.name = stepDefinition.getName();
        this.stepDefinition = stepDefinition;
    }

    public String getName() {
        return this.name;
    }

    public StepDefinition getStepDefinition() {
        return this.stepDefinition;
    }

    public static StepDefinitionRegistry getStepDefinitionByName(String name) {
        for (StepDefinitionRegistry stepDefinition : StepDefinitionRegistry.values()) {
            if (stepDefinition.getName().equals(name)) {
                return stepDefinition;
            }
        }
        return null; // no matching enum was found
    }
}
