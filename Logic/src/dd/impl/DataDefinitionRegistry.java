package dd.impl;

import dd.api.DataDefinition;
import dd.impl.decimal.DoubleDataDefinition;
import dd.impl.enumerator.method.MethodEnumeratorDataDefinition;
import dd.impl.enumerator.protocol.ProtocolEnumeratorDataDefinition;
import dd.impl.enumerator.zipper.ZipperEnumeratorDataDefinition;
import dd.impl.file.FileDataDefinition;
import dd.impl.json.JsonDataDefinition;
import dd.impl.list.file.FilesListDataDefinition;
import dd.impl.list.string.StringListDataDefinition;
import dd.impl.mapping.String2Number.String2NumberDataDefinition;
import dd.impl.number.NumberDataDefinition;
import dd.impl.relation.RelationDataDefinition;
import dd.impl.string.StringDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    FILES_LIST(new FilesListDataDefinition()),
    STRING_LIST(new StringListDataDefinition()),
    FILE(new FileDataDefinition()),
    STRING2NUMBER_MAP(new String2NumberDataDefinition()),
    ZIPPER_ENUMERATOR(new ZipperEnumeratorDataDefinition()),
    PROTOCOL_ENUMERATOR(new ProtocolEnumeratorDataDefinition()),
    METHOD_ENUMERATOR(new MethodEnumeratorDataDefinition()),
    JSON(new JsonDataDefinition())
    ;

    DataDefinitionRegistry(DataDefinition dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    private final DataDefinition dataDefinition;

    @Override
    public String getName() {
        return dataDefinition.getName();
    }

    @Override
    public String getUserPresentation() {
        return dataDefinition.getUserPresentation();
    }

    @Override
    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    @Override
    public Class<?> getType() {
        return dataDefinition.getType();
    }
}
