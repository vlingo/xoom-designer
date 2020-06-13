package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import io.vlingo.xoom.starter.task.template.code.resource.RestResourceTemplateDataFactory;

import java.util.List;

import static io.vlingo.xoom.starter.task.Property.PACKAGE;
import static io.vlingo.xoom.starter.task.Property.REST_RESOURCES;

public class RestResourceGenerationStep extends TemplateProcessingStep {

    @Override
    protected List<TemplateData> buildTemplatesData(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(PACKAGE);
        final String restResourcesData = context.propertyOf(REST_RESOURCES);
        return RestResourceTemplateDataFactory.build(basePackage, projectPath, restResourcesData);
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.hasProperty(REST_RESOURCES);
    }

}
