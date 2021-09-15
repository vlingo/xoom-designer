// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.OutputFileInstantiator;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;

import static io.vlingo.xoom.designer.Configuration.CODE_GENERATION_STEPS;
import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.OFFSET;

public class CodeGenerationExecutionerStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext execution) {
    final CodeGenerationContext generation =
            CodeGenerationContext.with(execution.codeGenerationParameters())
                    .fileLocationResolver(new ExternalFileLocationResolver())
                    .outputFileInstantiator(outputFileInstantiator());

    CODE_GENERATION_STEPS.stream().filter(step -> step.shouldProcess(generation)).forEach(step -> step.process(generation));
  }

  private OutputFileInstantiator outputFileInstantiator() {
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
