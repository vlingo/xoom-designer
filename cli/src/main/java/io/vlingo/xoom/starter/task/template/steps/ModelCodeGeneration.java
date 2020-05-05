package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.codegen.AggregateCodeGenerator;
import io.vlingo.xoom.starter.codegen.AggregateProtocolCodeGenerator;
import io.vlingo.xoom.starter.codegen.DomainEventCodeGenerator;
import io.vlingo.xoom.starter.codegen.StateCodeGenerator;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.Arrays;
import java.util.List;

public interface ModelCodeGeneration {

    void generate(final TaskExecutionContext context, final Aggregate aggregate);

    static List<ModelCodeGeneration> all(){
        return Arrays.asList(aggregate(), protocol(), state(), event());
    }

    static ModelCodeGeneration aggregate() {
        return (context, aggregate) -> {
            final String aggregateCode =
                    AggregateCodeGenerator.instance().generate(aggregate.name,
                            aggregate.packageName, aggregate.stateName(), aggregate.storageType);

            context.addOutputResource(aggregate.aggregateFile(), aggregateCode);
        };
    }

    static ModelCodeGeneration protocol() {
        return (context, aggregate) -> {
            final String protocolCode =
                    AggregateProtocolCodeGenerator.instance().generate(aggregate.name, aggregate.packageName);

            context.addOutputResource(aggregate.protocolFile(), protocolCode);
        };
    }

    static ModelCodeGeneration state() {
        return (context, aggregate) -> {
            final String stateCode =
                    StateCodeGenerator.instance().generate(aggregate.stateName(), aggregate.packageName, aggregate.storageType);

            context.addOutputResource(aggregate.stateFile(), stateCode);
        };
    }

    static ModelCodeGeneration event() {
        return (context, aggregate) -> {
            aggregate.events.forEach(event -> {
                final String eventCode =
                        DomainEventCodeGenerator.instance().generate(event.name, event.packageName);

                context.addOutputResource(event.file(), eventCode);
            });

            if (aggregate.dependsOnPlaceholderEvent()) {
                final String eventCode =
                        DomainEventCodeGenerator.instance().generatePlaceholderEvent(aggregate.placeholderEventName(), aggregate.packageName);

                context.addOutputResource(aggregate.placeholderEventFile(), eventCode);
            }
        };
    }

}
