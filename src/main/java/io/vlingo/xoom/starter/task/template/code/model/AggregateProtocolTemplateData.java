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

import java.io.File;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.AGGREGATE_PROTOCOL_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.AGGREGATE_PROTOCOL;

public class AggregateProtocolTemplateData extends TemplateData {

    private final String protocolName;
    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public AggregateProtocolTemplateData(final String packageName,
                                         final String protocolName,
                                         final String projectPath) {
        this.protocolName = protocolName;
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.parameters = CodeTemplateParameters.with(PACKAGE_NAME, packageName)
                        .and(AGGREGATE_PROTOCOL_NAME, protocolName);
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
        return AGGREGATE_PROTOCOL;
    }

}
