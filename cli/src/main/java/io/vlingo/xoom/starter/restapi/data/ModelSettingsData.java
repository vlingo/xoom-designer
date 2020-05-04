// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

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
