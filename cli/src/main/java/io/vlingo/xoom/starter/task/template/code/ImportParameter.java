// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

import java.util.List;

public class ImportParameter {

    public final String qualifiedClassName;

    public ImportParameter(final String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public static ImportParameter findImportParameter(final String className, final List<ImportParameter> importParameters) {
        return importParameters.stream()
                .filter(importParameter -> importParameter.qualifiedClassName.endsWith(className))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to find import for " + className));
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

}
