package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.template.StorageType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateGenerationDataFactory {

    private static final String EVENTS_SEPARATOR = ";";
    private static final String AGGREGATE_SEPARATOR = "\\|";

    public static List<AggregateGenerationData> build(final String basePackage,
                                                      final String projectPath,
                                                      final String aggregatesData,
                                                      final StorageType storageType) {

        return Arrays.asList(aggregatesData.split(AGGREGATE_SEPARATOR))
                .stream().map(aggregateData -> {
                    return new AggregateGenerationData(aggregateData.split(EVENTS_SEPARATOR),
                            basePackage, storageType, projectPath);
                }).collect(Collectors.toList());
    }

}
