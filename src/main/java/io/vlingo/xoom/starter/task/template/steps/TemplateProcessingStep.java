// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.util.List;

public abstract class TemplateProcessingStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        buildTemplates(context).forEach(templateData -> {
            final String code = CodeTemplateProcessor.instance().process(templateData);
            context.addContent(templateData.standard(), templateData.file(), code);
        });
    }

    protected abstract List<TemplateData> buildTemplates(final TaskExecutionContext context);

}
