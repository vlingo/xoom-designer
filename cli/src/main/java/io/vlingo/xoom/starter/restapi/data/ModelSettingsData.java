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
                             final String queryModelDatabase,
                             final List<AggregateData> aggregates,
                             final List<AggregateData> restResources) {
        this.storageType = storageType;
        this.useProjections = useProjections;
        this.commandModelDatabase = commandModelDatabase;
        this.queryModelDatabase = queryModelDatabase;
        this.aggregates.addAll(aggregates);
        this.restResources.addAll(restResources);
    }

    List<String> toArguments() {
        return Arrays.asList(
                Property.STORAGE_TYPE.literal(), storageType,
                Property.PROJECTIONS.literal(), String.valueOf(useProjections),
                Property.AGGREGATES.literal(), flatAggregates(),
                Property.REST_RESOURCES.literal(), flatRestResources(),
                Property.COMMAND_MODEL_DATABASE.literal(), commandModelDatabase,
                Property.QUERY_MODEL_DATABASE.literal(), queryModelDatabase
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
