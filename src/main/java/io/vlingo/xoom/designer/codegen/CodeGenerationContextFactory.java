// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.OutputFileInstantiator;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.OFFSET;

public class CodeGenerationContextFactory {

  public static CodeGenerationContext build(final Logger logger,
                                            final CodeGenerationParameters parameters) {
    return CodeGenerationContext.with(parameters).logger(logger)
                    .fileLocationResolver(new ExternalFileLocationResolver())
                    .outputFileInstantiator(outputFileInstantiator());
  }

  private static OutputFileInstantiator outputFileInstantiator() {
    return (context, data, dialect) -> {
      final String absolutePath =
              context.fileLocationResolver().resolve(context, dialect, data);

      final String fileName =
              dialect.formatFilename(data.filename());

      final String offset =
              data.parameters().find(OFFSET);

      return new OutputFile(absolutePath, fileName, offset, data.isPlaceholder());
    };
  }

}
