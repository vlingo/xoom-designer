// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.bootstrap.BootstrapTemplateData;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import static io.vlingo.xoom.starter.task.Property.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.BOOTSTRAP;

public class BootstrapGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(PACKAGE);
        final String artifactId = context.propertyOf(ARTIFACT_ID);
        final Boolean useCQRS = context.propertyOf(CQRS, Boolean::valueOf);
        final StorageType storageType = context.propertyOf(STORAGE_TYPE, StorageType::valueOf);
        final ProjectionType projectionType = context.propertyOf(PROJECTIONS, ProjectionType::valueOf);

        final TemplateData templateData =
                BootstrapTemplateData.from(basePackage, projectPath, artifactId,
                        storageType, useCQRS, projectionType.isProjectionEnabled(),
                        context.contents());

        final String bootstrapCode =
                CodeTemplateProcessor.instance().process(BOOTSTRAP, templateData.templateParameters());

        context.addContent(BOOTSTRAP, templateData.file(), bootstrapCode);
    }
}
