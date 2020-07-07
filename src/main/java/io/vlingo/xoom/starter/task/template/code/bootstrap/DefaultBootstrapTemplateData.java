// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.REST_RESOURCES;

public class DefaultBootstrapTemplateData extends BootstrapTemplateData {

    public DefaultBootstrapTemplateData(final String basePackage,
                                        final String projectPath,
                                        final String artifactId,
                                        final StorageType storageType,
                                        final Boolean useCQRS,
                                        final Boolean useProjections,
                                        final Boolean useAnnotations,
                                        final List<Content> contents) {
        super(basePackage, projectPath, artifactId, storageType,
                useCQRS, useProjections, useAnnotations, contents);
    }

    @Override
    protected void enrichParameters(final List<Content> contents) {
        final List<RestResourcesParameter> restResourcesParameters =
                RestResourcesParameter.from(contents);

        parameters().and(REST_RESOURCES, restResourcesParameters);
    }

}
