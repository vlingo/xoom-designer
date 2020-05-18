// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.STORE_PROVIDER_NAME;

public class StoreProviderParameter {

    public final String name;

    public StoreProviderParameter(final CodeTemplateParameters parameters) {
        this.name = parameters.find(STORE_PROVIDER_NAME);
    }

    public static List<StoreProviderParameter> from(final List<TemplateData> templateData) {
        return templateData.stream()
                .map(data -> new StoreProviderParameter(data.templateParameters()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

}
