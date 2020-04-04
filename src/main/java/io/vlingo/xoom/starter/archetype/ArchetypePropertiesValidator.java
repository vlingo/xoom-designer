package io.vlingo.xoom.starter.archetype;

import java.util.List;
import java.util.Properties;

public abstract class ArchetypePropertiesValidator {

    public boolean validate(final Properties properties, final List<ArchetypeProperties> requiredProperties) {
        if(properties.size() != requiredProperties.size()) {
            return false;
        }
        return checkValues(properties) &&
                requiredProperties.stream().allMatch(key -> properties.containsKey(key.literal()));
    }

    protected abstract boolean checkValues(final Properties properties);

}
