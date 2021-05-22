// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.TaskExecutionStep;
import io.vlingo.xoom.turbo.annotation.codegen.AnnotationBasedTemplateStandard;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

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
        return useAnnotations ? AnnotationBasedTemplateStandard.XOOM_INITIALIZER.resolveClassname() :
                JavaTemplateStandard.BOOTSTRAP.resolveClassname();
    }

}
