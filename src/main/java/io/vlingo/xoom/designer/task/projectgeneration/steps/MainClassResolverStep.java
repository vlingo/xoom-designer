// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import static io.vlingo.xoom.turbo.codegen.parameter.Label.*;

public class MainClassResolverStep implements TaskExecutionStep {

    private static final String QUALIFIED_PATTERN = "%s.infrastructure.%s";

    @Override
    public void process(final TaskExecutionContext context) {
        final String basePackage =
                context.codeGenerationParameters().retrieveValue(PACKAGE);

        final Boolean useAnnotations =
                Boolean.valueOf(context.codeGenerationParameters().retrieveValue(USE_ANNOTATIONS));

        final String mainClass =
                String.format(QUALIFIED_PATTERN, basePackage, resolveClassName(useAnnotations));

        context.codeGenerationParameters().add(MAIN_CLASS, mainClass);
    }

    private String resolveClassName(final Boolean useAnnotations) {
        return useAnnotations ? TemplateStandard.XOOM_INITIALIZER.resolveClassname() :
                TemplateStandard.BOOTSTRAP.resolveClassname();
    }

}
