// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.cli.Property;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;
import io.vlingo.xoom.designer.infrastructure.DesignerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CodeGenerationParametersLoadStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.with(loadParameters());
    }

    private CodeGenerationParameters loadParameters() {
        final Properties properties = DesignerProperties.properties();
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
            new HashMap<Property, Label>(){
              private static final long serialVersionUID = 6239027081958513358L;

            {
                put(Property.GROUP_ID, Label.GROUP_ID);
                put(Property.ARTIFACT_ID, Label.ARTIFACT_ID);
                put(Property.VERSION, Label.ARTIFACT_VERSION);
                put(Property.PACKAGE, Label.PACKAGE);
                put(Property.XOOM_VERSION, Label.XOOM_VERSION);
                put(Property.TARGET_FOLDER, Label.TARGET_FOLDER);
                put(Property.STORAGE_TYPE, Label.STORAGE_TYPE);
                put(Property.CQRS, Label.CQRS);
                put(Property.ANNOTATIONS, Label.USE_ANNOTATIONS);
                put(Property.AUTO_DISPATCH, Label.USE_AUTO_DISPATCH);
                put(Property.PROJECTIONS, Label.PROJECTION_TYPE);
                put(Property.DATABASE, Label.DATABASE);
                put(Property.DEPLOYMENT, Label.DEPLOYMENT_SETTINGS);
                put(Property.COMMAND_MODEL_DATABASE, Label.COMMAND_MODEL_DATABASE);
                put(Property.QUERY_MODEL_DATABASE, Label.QUERY_MODEL_DATABASE);
                //REST_RESOURCES, AGGREGATES, DOMAIN_EVENTS
            }};

}
