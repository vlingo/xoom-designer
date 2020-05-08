// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.AggregateTemplateData;
import io.vlingo.xoom.starter.task.template.code.AggregateTemplateDataFactory;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.*;

public class ModelGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String packageName = context.propertyOf(PACKAGE);
        final String aggregatesData = context.propertyOf(AGGREGATES);
        final StorageType storageType = StorageType.of(context.propertyOf(STORAGE_TYPE));

        final List<AggregateTemplateData> templateData =
                AggregateTemplateDataFactory.build(packageName, projectPath,
                        aggregatesData, storageType);

        templateData.forEach(data -> processTemplateData(context, data));
    }

    private void processTemplateData(final TaskExecutionContext context,
                                     final AggregateTemplateData templateData) {
        templateData.files.forEach((standard, files) -> {
            files.forEach(file -> {
                final CodeTemplateParameters parameters =
                        standard.enrich(templateData.parameters, file);

                final String code =
                        CodeTemplateProcessor.instance().process(standard, parameters);

                context.addOutputResource(file, code);
            });
        });
    }

}
