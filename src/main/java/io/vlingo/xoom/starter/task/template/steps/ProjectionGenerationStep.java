// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.projections.ProjectionTemplateDataFactory;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.AGGREGATES;
import static io.vlingo.xoom.starter.task.Property.PROJECTIONS;

public class ProjectionGenerationStep extends TemplateProcessingStep {

    @Override
    protected List<TemplateData> buildTemplatesData(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(Property.PACKAGE);
        final ProjectionType projectionType = context.propertyOf(PROJECTIONS, ProjectionType::valueOf);
        return ProjectionTemplateDataFactory.build(basePackage, projectPath,
                        projectionType, context.contents());
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
