package io.vlingo.xoom.starter.archetype;

import java.util.Properties;

import static io.vlingo.xoom.starter.archetype.ArchetypeProperties.DEPLOYMENT;

public class KubernetesArchetypePropertiesValidator extends ArchetypePropertiesValidator {

    private static final String EXPECTED_DEPLOYMENT_VALUE = "K8S";

    @Override
    protected boolean checkValues(final Properties properties) {
        if(!properties.containsKey(DEPLOYMENT.literal())) {
            return false;
        }
        final String deployment = properties.get(DEPLOYMENT.literal()).toString().trim();
        return deployment.equalsIgnoreCase(EXPECTED_DEPLOYMENT_VALUE);
    }

}
