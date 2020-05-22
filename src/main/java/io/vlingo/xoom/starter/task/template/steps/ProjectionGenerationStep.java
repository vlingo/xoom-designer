// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.projections.ProjectionTemplateDataFactory;

import java.util.List;
import java.util.Map;

import static io.vlingo.xoom.starter.task.Property.AGGREGATES;
import static io.vlingo.xoom.starter.task.Property.PROJECTIONS;

public class ProjectionGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(Property.PACKAGE);
        final ProjectionType projectionType = context.propertyOf(PROJECTIONS, ProjectionType::valueOf);

        final Map<CodeTemplateStandard, List<TemplateData>> projectionTemplatesData =
                ProjectionTemplateDataFactory.build(basePackage, projectPath,
                        projectionType, context.contents());

        projectionTemplatesData.forEach(((standard, templatesData) -> {
            templatesData.forEach(templateData -> {
                final CodeTemplateParameters parameters = templateData.templateParameters();
                final String code = CodeTemplateProcessor.instance().process(standard, parameters);
                context.addContent(standard, templateData.file(), code);
            });
        }));
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        if(context.hasProperty(AGGREGATES)) {
            final ProjectionType projectionType =
                    context.propertyOf(PROJECTIONS, ProjectionType::valueOf);
            return projectionType.isProjectionEnabled();
        }
        return false;
    }
}
