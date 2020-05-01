package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelSettingsData {

    public final String storageType;
    public final boolean useProjections;
    public final String commandModelDatabase;
    public final String queryModelDatabase;
    public final List<AggregateData> aggregates = new ArrayList<>();
    public final List<AggregateData> restResources = new ArrayList<>();

    public ModelSettingsData(final String storageType,
                             final boolean useProjections,
                             final String commandModelDatabase,
                             final String queryModelDatabase) {
        this.storageType = storageType;
        this.useProjections = useProjections;
        this.commandModelDatabase = commandModelDatabase;
        this.queryModelDatabase = queryModelDatabase;
    }

    List<String> toArguments() {
        return Arrays.asList(
        );
    }
}
