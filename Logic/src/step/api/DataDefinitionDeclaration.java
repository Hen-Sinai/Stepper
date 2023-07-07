package step.api;

import dd.api.DataDefinition;

import java.io.Serializable;

public interface DataDefinitionDeclaration extends Serializable {
    String getName();
    DataNecessity getNecessity();
    String getUserString();
    DataDefinition getDataDefinition();
}
