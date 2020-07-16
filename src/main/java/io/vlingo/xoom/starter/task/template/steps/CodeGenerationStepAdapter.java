// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.codegen.steps.CodeGenerationStep;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

public class CodeGenerationStepAdapter implements TaskExecutionStep {

    private final CodeGenerationStep codeGenerationStep;

    public static TaskExecutionStep of(final CodeGenerationStep codeGenerationStep) {
        return new CodeGenerationStepAdapter(codeGenerationStep);
    }

    public CodeGenerationStepAdapter(final CodeGenerationStep codeGenerationStep) {
        this.codeGenerationStep = codeGenerationStep;
    }

    @Override
    public void process(final TaskExecutionContext context) {
        codeGenerationStep.process(CodeGenerationContextAdapter.of(context));
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return codeGenerationStep.shouldProcess(CodeGenerationContextAdapter.of(context));
    }

}
