// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code;

import java.io.File;
import java.nio.file.Paths;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.AGGREGATE_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.PACKAGE_NAME;

public class RestResourceTemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "resource";

    public final String aggregateName;
    public final String packageName;
    private final String absolutePath;
    public final CodeTemplateParameters parameters;

    public RestResourceTemplateData(final String aggregateName,
                                    final String basePackage,
                                    final String projectPath) {
        this.aggregateName = aggregateName;
        this.packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(basePackage, projectPath);
        this.parameters = loadParameters();
    }

    public File file() {
        return new File(Paths.get(absolutePath, aggregateName + "Resource.java").toString());
    }

    private CodeTemplateParameters loadParameters() {
        return CodeTemplateParameters.with(AGGREGATE_NAME, aggregateName).and(PACKAGE_NAME, packageName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    private String resolveAbsolutePath(final String basePackage, final String projectPath) {
        final String basePackagePath = basePackage.replaceAll("\\.", "\\\\");
        return Paths.get(projectPath, "src", "main", "java", basePackagePath, PARENT_PACKAGE_NAME).toString();
    }

}