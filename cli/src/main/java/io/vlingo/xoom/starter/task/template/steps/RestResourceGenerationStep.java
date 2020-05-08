package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateProcessor;
import io.vlingo.xoom.starter.task.template.code.RestResourceTemplateData;
import io.vlingo.xoom.starter.task.template.code.RestResourceTemplateDataFactory;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.PACKAGE;
import static io.vlingo.xoom.starter.task.Property.REST_RESOURCES;
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
            final CodeTemplateParameters parameters = restResourceData.parameters;
            final String code = CodeTemplateProcessor.instance().process(REST_RESOURCE, parameters);
            context.addOutputResource(restResourceData.file(), code);
        });
    }

}
