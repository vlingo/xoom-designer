package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.codegeneration.AggregateCodeGenerator;
import io.vlingo.xoom.starter.codegeneration.AggregateProtocolCodeGenerator;
import io.vlingo.xoom.starter.codegeneration.DomainEventCodeGenerator;
import io.vlingo.xoom.starter.codegeneration.StateCodeGenerator;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.Arrays;
import java.util.List;

public interface ModelCodeGeneration {

    void generate(final TaskExecutionContext context, final AggregateGenerationData aggregateGenerationData);

    static List<ModelCodeGeneration> all(){
        return Arrays.asList(aggregate(), protocol(), state(), event());
    }

    static ModelCodeGeneration aggregate() {
        return (context, aggregateGenerationData) -> {
            final String aggregateCode =
                    AggregateCodeGenerator.instance().generate(aggregateGenerationData);

            context.addOutputResource(aggregateGenerationData.aggregateFile(), aggregateCode);
        };
    }

    static ModelCodeGeneration protocol() {
        return (context, aggregateGenerationData) -> {
            final String protocolCode =
                    AggregateProtocolCodeGenerator.instance().generate(aggregateGenerationData.name, aggregateGenerationData.packageName);

            context.addOutputResource(aggregateGenerationData.protocolFile(), protocolCode);
        };
    }

    static ModelCodeGeneration state() {
        return (context, aggregateGenerationData) -> {
            final String stateCode =
                    StateCodeGenerator.instance().generate(aggregateGenerationData.stateName(), aggregateGenerationData.packageName, aggregateGenerationData.storageType);

            context.addOutputResource(aggregateGenerationData.stateFile(), stateCode);
        };
    }

    static ModelCodeGeneration event() {
        return (context, aggregateGenerationData) -> {
            aggregateGenerationData.events.forEach(event -> {
                final String eventCode =
                        DomainEventCodeGenerator.instance().generate(event.name, event.packageName);

                context.addOutputResource(event.file(), eventCode);
            });

            final String eventCode =
                    DomainEventCodeGenerator.instance().generatePlaceholderEvent(aggregateGenerationData.placeholderEventName(), aggregateGenerationData.packageName);

            context.addOutputResource(aggregateGenerationData.placeholderEventFile(), eventCode);
        };
    }

}
