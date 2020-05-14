// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.Property.*;

public class ModelSettingsData {

    public final String storageType;
    public final boolean useCQRS;
    public final boolean useProjections;
    public final String database;
    public final String commandModelDatabase;
    public final String queryModelDatabase;
    public final List<AggregateData> aggregates = new ArrayList<>();
    public final List<AggregateData> restResources = new ArrayList<>();

    public ModelSettingsData(final String storageType,
                             final boolean useCQRS,
                             final boolean useProjections,
                             final String database,
                             final String commandModelDatabase,
                             final String queryModelDatabase,
                             final List<AggregateData> aggregates,
                             final List<AggregateData> restResources) {
        this.storageType = storageType;
        this.useCQRS = useCQRS;
        this.useProjections = useProjections;
        this.database = database;
        this.commandModelDatabase = commandModelDatabase;
        this.queryModelDatabase = queryModelDatabase;
        this.aggregates.addAll(aggregates);
        this.restResources.addAll(restResources);
    }

    List<String> toArguments() {
        return Arrays.asList(
                STORAGE_TYPE.literal(), storageType,
                AGGREGATES.literal(), flatAggregates(),
                REST_RESOURCES.literal(), flatRestResources(),
                CQRS.literal(), String.valueOf(useCQRS),
                PROJECTIONS.literal(), String.valueOf(useProjections),
                DATABASE.literal(), database,
                COMMAND_MODEL_DATABASE.literal(), commandModelDatabase,
                QUERY_MODEL_DATABASE.literal(), queryModelDatabase
        );
    }

    private String flatAggregates() {
        if(aggregates.isEmpty()) {
            return Property.DEFAULT_VALUE;
        }
        return aggregates.stream()
                .map(aggregate -> aggregate.name + ";" + aggregate.flatEvents())
                .collect(Collectors.joining("|"));
    }

    private String flatRestResources() {
        if(restResources.isEmpty()) {
            return Property.DEFAULT_VALUE;
        }
        return restResources.stream()
                .map(aggregate -> aggregate.name)
                .collect(Collectors.joining(";"));
    }
}
