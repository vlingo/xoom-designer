// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.nio.file.Paths;

public class DomainEventTemplateData extends TemplateData {

    public final String name;
    public final String packageName;
    public final String absolutePath;

    public DomainEventTemplateData(final String name, final String packageName, final String absolutePath) {
        this.name = name;
        this.packageName = packageName;
        this.absolutePath = absolutePath;
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    public File file() {
        return new File(Paths.get(absolutePath, name + ".java").toString());
    }

}
