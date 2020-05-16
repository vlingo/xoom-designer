// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;

import java.io.File;

public class ValueTemplateData {

    private static final String PROVIDER_NAME_SUFFIX = "Data";

    public final File file;
    public final String providerName;
    public final CodeTemplateParameters templateParameters;


    public ValueTemplateData(final File file,
                             final String providerName,
                             final CodeTemplateParameters templateParameters) {
        this.file = file;
        this.providerName = providerName;
        this.templateParameters = templateParameters;
    }
}
