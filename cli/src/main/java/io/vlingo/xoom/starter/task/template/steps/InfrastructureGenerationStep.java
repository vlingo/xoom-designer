// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.infrastructure.InfrastructureTemplateData;

import java.util.List;
import java.util.Map;

public class InfrastructureGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(Property.PACKAGE);
        final Boolean enableCQRS = context.propertyOf(Property.CQRS, Boolean::valueOf);
        final StorageType storageType = context.propertyOf(Property.STORAGE_TYPE, StorageType::of);
        final DatabaseType databaseType = context.propertyOf(Property.DATABASE, DatabaseType::valueOf);

        final Map<CodeTemplateStandard, List<TemplateData>> infraTemplatesData =
                InfrastructureTemplateData.with(basePackage, projectPath, enableCQRS,
                        storageType, databaseType, context.contents()).collectAll();

        infraTemplatesData.forEach(((standard, templatesData) -> {
            templatesData.forEach(templateData -> {
                final CodeTemplateParameters parameters = templateData.templateParameters();
                final String code = CodeTemplateProcessor.instance().process(standard, parameters);
                context.addContent(standard, templateData.file(), code);
            });
        }));
    }

}
