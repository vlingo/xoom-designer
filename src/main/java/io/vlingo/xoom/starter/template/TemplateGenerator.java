// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template;

import io.vlingo.xoom.starter.template.steps.*;

import java.util.Arrays;
import java.util.List;

public class TemplateGenerator {

    private static final List<TemplateGenerationStep> STEPS = Arrays.asList(
            new ResourcesLocationStep(), new PropertiesLoadStep(),
            new CommandExecutorStep(), new GenerationLogStep(),
            new StatusHandlingStep()
    );

    public static void start(final TemplateGenerationContext context) {
        try {
            STEPS.forEach(step -> {
                step.process(context);
            });
        } catch (final TemplateGenerationException exception) {
            exception.printStackTrace();
        }
    }

}
