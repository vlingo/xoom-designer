// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.Resource;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;

public class ResourcesLocationStep implements TemplateGenerationStep {

    private static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_STARTER_HOME";

    @Override
    public void process(final TemplateGenerationContext context) {
        if(!Resource.hasAllPaths()) {
            Resource.rootIn(System.getenv(HOME_ENVIRONMENT_VARIABLE));
        }
    }

}
