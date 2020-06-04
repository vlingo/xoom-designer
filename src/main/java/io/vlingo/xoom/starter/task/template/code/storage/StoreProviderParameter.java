// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.ProjectionType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public class StoreProviderParameter {

    private final String name;
    private final boolean useProjections;

    private StoreProviderParameter(final CodeTemplateParameters parameters) {
        this.name = parameters.find(STORE_PROVIDER_NAME);
        final ProjectionType projectionType = parameters.find(PROJECTION_TYPE);
        final ModelClassification modelClassification = parameters.find(MODEL_CLASSIFICATION);
        this.useProjections = projectionType.isProjectionEnabled() && modelClassification.isCommandModel();
    }

    public static List<StoreProviderParameter> from(final List<TemplateData> templateData) {
        return templateData.stream()
                .map(data -> new StoreProviderParameter(data.parameters()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public boolean getUseProjections() {
        return useProjections;
    }

}
