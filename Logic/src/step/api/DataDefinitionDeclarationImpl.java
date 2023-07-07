package step.api;

import dd.api.DataDefinition;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {
    private final String name;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;

    public DataDefinitionDeclarationImpl(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DataNecessity getNecessity() {
        return this.necessity;
    }

    @Override
    public String getUserString() {
        return this.userString;
    }

    @Override
    public DataDefinition getDataDefinition() {
        return this.dataDefinition;
    }
}
