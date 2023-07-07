package mySchema;

import jaxb.schema.generated.STCustomMapping;
import jaxb.schema.generated.STCustomMappings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class stCustomMappings implements Serializable {
    private final List<stCustomMapping> customMappings = new ArrayList<>();

    public stCustomMappings(STCustomMappings customMappings) {
        if (customMappings != null) {
            for (STCustomMapping customMapping : customMappings.getSTCustomMapping()) {
                stCustomMapping custom = new stCustomMapping(customMapping);
                this.customMappings.add(custom);
            }
        }
    }

    public List<stCustomMapping> getCustomMappings() {
        return this.customMappings;
    }
}
