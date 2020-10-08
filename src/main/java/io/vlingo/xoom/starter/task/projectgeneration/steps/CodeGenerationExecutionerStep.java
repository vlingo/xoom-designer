// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import static io.vlingo.xoom.starter.Configuration.CODE_GENERATION_STEPS;

public class CodeGenerationExecutionerStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext taskExecutionContext) {
        final CodeGenerationContext context =
                resolveContext(taskExecutionContext);

        CODE_GENERATION_STEPS.stream()
                .filter(step -> step.shouldProcess(context))
                .forEach(step -> step.process(context));
    }

    private CodeGenerationContext resolveContext(final TaskExecutionContext taskExecutionContext) {
        if(taskExecutionContext.hasCodeGenerationParameters()) {
            return CodeGenerationContext.with(taskExecutionContext.codeGenerationParameters());
        }
        return CodeGenerationContext.with(CodeGenerationParametersMapper.of(taskExecutionContext));
    }

}
