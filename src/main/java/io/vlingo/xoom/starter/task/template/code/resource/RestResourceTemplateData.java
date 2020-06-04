// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.resource;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;

public class RestResourceTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "resource";

    public final String aggregateName;
    public final String packageName;
    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public RestResourceTemplateData(final String aggregateName,
                                    final String basePackage,
                                    final String projectPath) {
        this.aggregateName = aggregateName;
        this.packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(basePackage, projectPath, PARENT_PACKAGE_NAME);
        this.parameters = loadParameters();
    }

    @Override
    public CodeTemplateParameters parameters() {
        return parameters;
    }

    private CodeTemplateParameters loadParameters() {
        return CodeTemplateParameters
                .with(REST_RESOURCE_NAME, REST_RESOURCE.resolveClassname(aggregateName))
                .and(PACKAGE_NAME, packageName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    public File file() {
        return buildFile(absolutePath, aggregateName);
    }

    public CodeTemplateStandard standard() {
        return REST_RESOURCE;
    }

}