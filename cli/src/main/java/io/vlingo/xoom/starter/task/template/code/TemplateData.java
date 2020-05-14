// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TemplateData {

    public abstract File file();

    public abstract CodeTemplateParameters templateParameters();

    protected String resolveAbsolutePath(final String basePackage, final String projectPath, final String ...otherFolders) {
        final String basePackagePath = basePackage.replaceAll("\\.", "\\" + File.separator);
        final Stream<String> folders = Stream.concat(Stream.of("src", "main", "java", basePackagePath), Stream.of(otherFolders));
        return Paths.get(projectPath, folders.collect(Collectors.toList()).toArray(new String[0])).toString();
    }

}
