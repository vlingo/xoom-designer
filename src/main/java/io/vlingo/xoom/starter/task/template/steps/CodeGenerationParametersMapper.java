// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerationParametersMapper {

    public static Map<Label, String> of(final TaskExecutionContext context) {
        final Predicate<Property> condition =
                prop -> context.hasProperty(prop) && PROPERTY_TRANSLATION.containsKey(prop);

        return Stream.of(Property.values()).filter(condition)
                .collect(Collectors.toMap(prop -> PROPERTY_TRANSLATION.get(prop),
                        prop -> context.propertyOf(prop)));
    }

    private static final Map<Property, Label> PROPERTY_TRANSLATION =
            new HashMap<Property, Label>(){{
                put(Property.AGGREGATES, Label.AGGREGATES);
                put(Property.ANNOTATIONS, Label.ANNOTATIONS);
                put(Property.ARTIFACT_ID, Label.APPLICATION_NAME);
                put(Property.COMMAND_MODEL_DATABASE, Label.COMMAND_MODEL_DATABASE);
                put(Property.CQRS, Label.CQRS);
                put(Property.DATABASE, Label.DATABASE);
                put(Property.PACKAGE, Label.PACKAGE);
                put(Property.PROJECTIONS, Label.PROJECTION_TYPE);
                put(Property.QUERY_MODEL_DATABASE, Label.QUERY_MODEL_DATABASE);
                put(Property.AUTO_DISPATCH, Label.USE_AUTO_DISPATCH);
                put(Property.REST_RESOURCES, Label.REST_RESOURCES);
                put(Property.STORAGE_TYPE, Label.STORAGE_TYPE);
                put(Property.TARGET_FOLDER, Label.TARGET_FOLDER);
            }};
}
