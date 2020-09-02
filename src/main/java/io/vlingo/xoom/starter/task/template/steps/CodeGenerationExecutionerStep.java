// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.util.Map;

import static io.vlingo.xoom.starter.Configuration.CODE_GENERATION_STEPS;

public class CodeGenerationExecutionerStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext taskExecutionContext) {
        final Map<Label, String> parameters =
                CodeGenerationParametersMapper.of(taskExecutionContext);

        final CodeGenerationContext context =
                CodeGenerationContext.with(parameters);

        CODE_GENERATION_STEPS.stream()
                .filter(step -> step.shouldProcess(context))
                .forEach(step -> step.process(context));
    }

}
