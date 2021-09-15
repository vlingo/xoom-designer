// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.FileLocationResolver;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;

import java.nio.file.Paths;

public class ExternalFileLocationResolver implements FileLocationResolver {

  @Override
  public String resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
    final String targetFolder =
            context.parameterOf(Label.TARGET_FOLDER);

    final String[] relativeSourcePath =
            RelativeSourcePathResolver.resolveWith(context, dialect, templateData);

    return Paths.get(targetFolder, relativeSourcePath).toString();
  }
}
