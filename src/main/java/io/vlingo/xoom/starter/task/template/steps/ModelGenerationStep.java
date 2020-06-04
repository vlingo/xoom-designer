// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.model.ModelTemplateDataFactory;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.*;

public class ModelGenerationStep extends TemplateProcessingStep {

    @Override
    protected List<TemplateData> buildTemplates(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String packageName = context.propertyOf(PACKAGE);
        final String aggregatesData = context.propertyOf(AGGREGATES);
        final StorageType storageType = StorageType.of(context.propertyOf(STORAGE_TYPE));
        return ModelTemplateDataFactory.build(packageName, projectPath,
                aggregatesData, storageType);
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.hasProperty(AGGREGATES);
    }

}
