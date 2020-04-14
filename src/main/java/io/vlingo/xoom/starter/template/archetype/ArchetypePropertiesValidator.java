// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.archetype;

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
