// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.archetype;

import io.vlingo.xoom.starter.task.Property;

import java.util.List;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.DEPLOYMENT;

public class KubernetesArchetypePropertiesValidator extends ArchetypePropertiesValidator {

    private static final String EXPECTED_DEPLOYMENT_VALUE = "K8S";

    public boolean validate(final Properties properties, final List<Property> requiredProperties) {
        return true;
    }

    @Override
    protected boolean checkValues(final Properties properties) {
        return true;
    }

}
