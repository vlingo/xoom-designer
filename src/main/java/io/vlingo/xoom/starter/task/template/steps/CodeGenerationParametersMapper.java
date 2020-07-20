// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.codegen.CodeGenerationParameter;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerationParametersMapper {

    public static Map<CodeGenerationParameter, String> of(final TaskExecutionContext context) {
        final Predicate<Property> condition =
                prop -> context.hasProperty(prop) && PROPERTY_TRANSLATION.containsKey(prop);

        return Stream.of(Property.values()).filter(condition)
                .collect(Collectors.toMap(prop -> PROPERTY_TRANSLATION.get(prop),
                        prop -> context.propertyOf(prop)));
    }

    private static final Map<Property, CodeGenerationParameter> PROPERTY_TRANSLATION =
            new HashMap<Property, CodeGenerationParameter>(){{
                put(Property.AGGREGATES, CodeGenerationParameter.AGGREGATES);
                put(Property.ANNOTATIONS, CodeGenerationParameter.ANNOTATIONS);
                put(Property.ARTIFACT_ID, CodeGenerationParameter.APPLICATION_NAME);
                put(Property.COMMAND_MODEL_DATABASE, CodeGenerationParameter.COMMAND_MODEL_DATABASE);
                put(Property.CQRS, CodeGenerationParameter.CQRS);
                put(Property.DATABASE, CodeGenerationParameter.DATABASE);
                put(Property.PACKAGE, CodeGenerationParameter.PACKAGE);
                put(Property.PROJECTIONS, CodeGenerationParameter.PROJECTIONS);
                put(Property.QUERY_MODEL_DATABASE, CodeGenerationParameter.QUERY_MODEL_DATABASE);
                put(Property.REST_RESOURCES, CodeGenerationParameter.REST_RESOURCES);
                put(Property.STORAGE_TYPE, CodeGenerationParameter.STORAGE_TYPE);
                put(Property.TARGET_FOLDER, CodeGenerationParameter.TARGET_FOLDER);
            }};
}
