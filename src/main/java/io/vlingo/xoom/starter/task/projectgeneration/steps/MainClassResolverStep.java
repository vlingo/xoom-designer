// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

public class MainClassResolverStep implements TaskExecutionStep {

    private static final String QUALIFIED_PATTERN = "%s.infrastructure.%s";

    @Override
    public void process(final TaskExecutionContext context) {
        final String basePackage =
                context.propertyOf(Property.PACKAGE);

        final Boolean useAnnotations =
                context.propertyOf(Property.ANNOTATIONS, Boolean::valueOf);

        final String mainClass =
                String.format(QUALIFIED_PATTERN, basePackage, resolveClassName(useAnnotations));

        context.addProperty(Property.MAIN_CLASS, mainClass);
    }

    private String resolveClassName(final Boolean useAnnotations) {
        return useAnnotations ? TemplateStandard.XOOM_INITIALIZER.resolveClassname() :
                TemplateStandard.BOOTSTRAP.resolveClassname();
    }

}
