// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

import io.vlingo.xoom.starter.task.template.StorageType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateTemplateDataFactory {

    private static final String EVENTS_SEPARATOR = ";";
    private static final String AGGREGATE_SEPARATOR = "\\|";

    public static List<AggregateTemplateData> build(final String basePackage,
                                                    final String projectPath,
                                                    final String aggregatesData,
                                                    final StorageType storageType) {

        return Arrays.asList(aggregatesData.split(AGGREGATE_SEPARATOR))
                .stream().map(aggregateData -> {
                    return new AggregateTemplateData(aggregateData.split(EVENTS_SEPARATOR),
                            basePackage, storageType, projectPath);
                }).collect(Collectors.toList());
    }

}
