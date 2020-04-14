// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.archetype;

import java.util.Properties;

import static io.vlingo.xoom.starter.template.archetype.ArchetypeProperties.DEPLOYMENT;

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
