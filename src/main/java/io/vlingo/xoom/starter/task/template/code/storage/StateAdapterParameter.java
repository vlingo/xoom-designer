// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.List;
import java.util.stream.Collectors;

public class StateAdapterParameter {

    private final String stateClass;
    private final String stateAdapterClass;

    private StateAdapterParameter(final String stateClass,
                                  final String stateAdapterClass) {
        this.stateClass = stateClass;
        this.stateAdapterClass = stateAdapterClass;
    }

    private StateAdapterParameter(final CodeTemplateParameters parameters) {
        this(parameters.find(CodeTemplateParameter.STATE_NAME), parameters.find(CodeTemplateParameter.STATE_ADAPTER_NAME));
    }

    public static List<StateAdapterParameter> from(final List<TemplateData> adaptersTemplateData) {
        return adaptersTemplateData.stream()
                .map(adapterTemplateData -> new StateAdapterParameter(adapterTemplateData.templateParameters()))
                .collect(Collectors.toList());
    }

    public String getStateClass() {
        return stateClass;
    }

    public String getStateAdapterClass() {
        return stateAdapterClass;
    }

}
