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
import io.vlingo.xoom.starter.task.template.code.storage.StorageTemplateDataFactory;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.util.List;

public class StorageGenerationStep extends TemplateProcessingStep {

    @Override
    protected List<TemplateData> buildTemplates(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(Property.PACKAGE);
        final Boolean enableCQRS = context.propertyOf(Property.CQRS, Boolean::valueOf);
        final StorageType storageType = context.propertyOf(Property.STORAGE_TYPE, StorageType::of);
        final ProjectionType projectionType = context.propertyOf(Property.PROJECTIONS, ProjectionType::valueOf);
        return StorageTemplateDataFactory.build(basePackage, projectPath, enableCQRS,
                        context.contents(), storageType, context.databases(), projectionType);
    }

}
