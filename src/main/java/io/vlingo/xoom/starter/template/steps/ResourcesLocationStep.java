// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionStep;
import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

public class ResourcesLocationStep implements TaskExecutionStep {

    private static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_STARTER_HOME";

    @Override
    public void process(final TaskExecutionContext context) {
        if(!Resource.hasAllPaths()) {
            Resource.rootIn(System.getenv(HOME_ENVIRONMENT_VARIABLE));
        }
    }

}
