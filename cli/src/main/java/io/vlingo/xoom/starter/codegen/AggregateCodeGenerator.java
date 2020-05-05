// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import io.vlingo.xoom.starter.ApplicationConfiguration;
import io.vlingo.xoom.starter.task.template.StorageType;

public class AggregateCodeGenerator extends BaseGenerator {

    private static AggregateCodeGenerator instance;

    private AggregateCodeGenerator() {
    }

    public String generate(final String aggregateName,
                           final String packageName,
                           final String stateClass,
                           final StorageType storageType) {
        input.put("aggregateProtocolName", aggregateName);
        input.put("stateClass", stateClass);
        input.put("packageName", packageName);
        return generate(ApplicationConfiguration.AGGREGATE_TEMPLATES.get(storageType));
    }

    public static AggregateCodeGenerator instance() {
        if(instance == null) {
            instance = new AggregateCodeGenerator();
        }
        return instance;
    }
}
