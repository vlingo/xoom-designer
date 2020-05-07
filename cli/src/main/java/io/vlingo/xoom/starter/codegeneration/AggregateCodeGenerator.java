// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.template.steps.AggregateGenerationData;

public class AggregateCodeGenerator extends BaseGenerator {

    private static AggregateCodeGenerator instance;

    private AggregateCodeGenerator() {
    }

    public String generate(final AggregateGenerationData aggregateGenerationData) {
        input.put("aggregateProtocolName", aggregateGenerationData.name);
        input.put("packageName", aggregateGenerationData.packageName);
        input.put("stateClass", aggregateGenerationData.stateName());
        input.put("placeholderDefinedEvent", aggregateGenerationData.placeholderEventName());
        return generate(Configuration.AGGREGATE_TEMPLATES.get(aggregateGenerationData.storageType));
    }

    public static AggregateCodeGenerator instance() {
        if(instance == null) {
            instance = new AggregateCodeGenerator();
        }
        return instance;
    }
}
