// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.archetype;

import io.vlingo.xoom.starter.task.Property;

import java.util.List;
import java.util.Properties;

public class KubernetesArchetypePropertiesValidator extends ArchetypePropertiesValidator {

    public boolean validate(final Properties properties, final List<Property> requiredProperties) {
        return true;
    }

    @Override
    protected boolean checkValues(final Properties properties) {
        return true;
    }

}
