// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.io.File;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.STATE;

public class StateTemplateData extends TemplateData {

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public StateTemplateData(final String packageName,
                             final String protocolName,
                             final StorageType storageType,
                             final String projectPath) {
        this.protocolName = protocolName;
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.parameters =
                CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                        .and(STATE_NAME, STATE.resolveClassname(protocolName))
                        .and(STORAGE_TYPE, storageType);
    }

    @Override
    public File file() {
        return buildFile(absolutePath, protocolName);
    }

    @Override
    public CodeTemplateParameters parameters() {
        return parameters;
    }

    @Override
    public CodeTemplateStandard standard() {
        return STATE;
    }
}
