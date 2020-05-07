package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.StorageType;

import java.util.List;

public class ModelCodeGenerationStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        final String targetDirectory = context.projectPath();
        final String packageName = context.propertyOf(Property.PACKAGE);
        final String aggregatesData = context.propertyOf(Property.AGGREGATES);
        final StorageType storageType = StorageType.of(context.propertyOf(Property.STORAGE_TYPE));

        final List<AggregateGenerationData> aggregates =
                AggregateGenerationDataFactory.build(packageName, targetDirectory,
                        aggregatesData, storageType);

        aggregates.forEach(generationData -> {
            ModelCodeGeneration.all().forEach(generator -> {
                generator.generate(context, generationData);
            });
        });
    }

}
