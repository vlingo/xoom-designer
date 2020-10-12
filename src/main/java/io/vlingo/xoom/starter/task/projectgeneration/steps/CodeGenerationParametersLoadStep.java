// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CodeGenerationParametersLoadStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.with(loadParameters());
    }

    private CodeGenerationParameters loadParameters() {
        final Properties properties = Configuration.loadProperties();
        final CodeGenerationParameters parameters = CodeGenerationParameters.empty();
        PROPERTY_TRANSLATION.entrySet().forEach(
                entry -> translate(parameters, properties,
                        entry.getKey(), entry.getValue()));
        return parameters;
    }

    private void translate(final CodeGenerationParameters parameters,
                           final Properties properties,
                           final Property property,
                           final Label label) {
        parameters.add(label, properties.getOrDefault(property.literal(), ""));
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.agent().isTerminal();
    }

    private static final Map<Property, Label> PROPERTY_TRANSLATION =
            new HashMap<Property, Label>(){{
                put(Property.GROUP_ID, Label.GROUP_ID);
                put(Property.ARTIFACT_ID, Label.ARTIFACT_ID);
                put(Property.VERSION, Label.VERSION);
                put(Property.PACKAGE, Label.PACKAGE);
                put(Property.XOOM_SERVER_VERSION, Label.XOOM_SERVER_VERSION);
                put(Property.DOCKER_IMAGE, Label.DOCKER_IMAGE);
                put(Property.KUBERNETES_IMAGE, Label.KUBERNETES_IMAGE);
                put(Property.KUBERNETES_POD_NAME, Label.KUBERNETES_POD_NAME);
                put(Property.TARGET_FOLDER, Label.TARGET_FOLDER);
                put(Property.STORAGE_TYPE, Label.STORAGE_TYPE);
                put(Property.CQRS, Label.CQRS);
                put(Property.ANNOTATIONS, Label.USE_ANNOTATIONS);
                put(Property.AUTO_DISPATCH, Label.AUTO_DISPATCH);
                put(Property.PROJECTIONS, Label.PROJECTION_TYPE);
                put(Property.DATABASE, Label.DATABASE);
                put(Property.DEPLOYMENT, Label.DEPLOYMENT);
                put(Property.COMMAND_MODEL_DATABASE, Label.COMMAND_MODEL_DATABASE);
                put(Property.QUERY_MODEL_DATABASE, Label.QUERY_MODEL_DATABASE);
                //REST_RESOURCES, AGGREGATES, DOMAIN_EVENTS
            }};

}
