package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.resource.RestResourceTemplateData;
import io.vlingo.xoom.starter.task.template.code.resource.RestResourceTemplateDataFactory;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;

public class RestResourceGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(PACKAGE);
        final String restResourcesData = context.propertyOf(REST_RESOURCES);

        final List<RestResourceTemplateData> templateData =
                RestResourceTemplateDataFactory.build(basePackage, projectPath, restResourcesData);

        templateData.forEach(restResourceData -> {
            final CodeTemplateParameters parameters = restResourceData.templateParameters();
            final String code = CodeTemplateProcessor.instance().process(REST_RESOURCE, parameters);
            context.addContent(REST_RESOURCE, restResourceData.file(), code);
        });
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.hasProperty(AGGREGATES);
    }

}
