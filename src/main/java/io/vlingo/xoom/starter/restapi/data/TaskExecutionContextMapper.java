// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.Properties;

import static io.vlingo.xoom.codegen.parameter.Label.*;
import static io.vlingo.xoom.starter.task.Agent.WEB;

public class TaskExecutionContextMapper {

    public static TaskExecutionContext from(final GenerationSettingsData data) {
        return new TaskExecutionContextMapper().map(data);
    }

    private TaskExecutionContextMapper() {}

    private TaskExecutionContext map(final GenerationSettingsData data) {
        return TaskExecutionContext.executedFrom(WEB)
                .with(mapCodeGenerationParameters(data))
                .onProperties(mapProperties(data));
    }

    private CodeGenerationParameters mapCodeGenerationParameters(final GenerationSettingsData data) {
        final CodeGenerationParameters parameters =
                CodeGenerationParameters.from(APPLICATION_NAME, data.context.artifactId);
        mapAggregates(parameters, data);
        mapPersistence(parameters, data);
        mapGeneration(parameters, data);
        return parameters;
    }

    private void mapAggregates(final CodeGenerationParameters parameters,
                               final GenerationSettingsData data) {
        data.model.aggregateSettings.forEach(aggregate -> {
            final CodeGenerationParameter aggregateParameter =
                    CodeGenerationParameter.of(AGGREGATE, aggregate.aggregateName)
                            .relate(URI_ROOT, aggregate.api.rootPath);

            mapStateFields(aggregate, aggregateParameter);
            mapDomainEvents(aggregate, aggregateParameter);
            mapMethods(aggregate, aggregateParameter);
            mapRoutes(aggregate, aggregateParameter);
            parameters.add(aggregateParameter);
        });
    }

    private void mapStateFields(final AggregateData aggregateData,
                                final CodeGenerationParameter aggregateParameter) {
        aggregateData.statesFields.forEach(stateField -> {
            aggregateParameter.relate(
                    CodeGenerationParameter.of(STATE_FIELD, stateField.name)
                            .relate(FIELD_TYPE, stateField.type));
        });
    }

    private void mapMethods(final AggregateData aggregateData,
                            final CodeGenerationParameter aggregateParameter) {
        aggregateData.methods.forEach(method -> {
            final CodeGenerationParameter methodParameter =
                    CodeGenerationParameter.of(AGGREGATE_METHOD, method.name)
                            .relate(FACTORY_METHOD, method.factory)
                            .relate(DOMAIN_EVENT, method.event);

            method.parameters.forEach(param -> methodParameter.relate(METHOD_PARAMETER, param));

            aggregateParameter.relate(methodParameter);
        });
    }

    private void mapDomainEvents(final AggregateData aggregateData,
                                 final CodeGenerationParameter aggregateParameter) {
        aggregateData.events.forEach(event -> {
            final CodeGenerationParameter eventParameter =
                    CodeGenerationParameter.of(DOMAIN_EVENT, event.name);

            event.fields.forEach(field -> {
                eventParameter.relate(STATE_FIELD, field);
            });

            aggregateParameter.relate(eventParameter);
        });
    }

    private void mapRoutes(final AggregateData aggregateData,
                           final CodeGenerationParameter aggregateParameter) {
        aggregateData.api.routes.forEach(route -> {
            final CodeGenerationParameter routeParameter =
                    CodeGenerationParameter.of(ROUTE_SIGNATURE, route.aggregateMethod)
                            .relate(ROUTE_METHOD, route.httpMethod)
                            .relate(ROUTE_PATH, route.route)
                            .relate(REQUIRE_ENTITY_LOAD, route.requireEntityLoad);

            aggregateParameter.relate(routeParameter);
        });
    }

    private void mapPersistence(final CodeGenerationParameters parameters,
                                final GenerationSettingsData data) {
        parameters.add(CQRS, data.model.persistence.useCQRS)
                .add(DATABASE, data.model.persistence.database)
                .add(PROJECTION_TYPE, data.model.persistence.projections)
                .add(STORAGE_TYPE, data.model.persistence.storageType)
                .add(COMMAND_MODEL_DATABASE, data.model.persistence.commandModelDatabase)
                .add(QUERY_MODEL_DATABASE, data.model.persistence.queryModelDatabase);
    }

    private void mapGeneration(final CodeGenerationParameters parameters,
                               final GenerationSettingsData data) {
        parameters.add(TARGET_FOLDER, data.projectDirectory)
                .add(USE_ANNOTATIONS, data.useAnnotations)
                .add(USE_AUTO_DISPATCH, data.useAutoDispatch);
    }

    private Properties mapProperties(final GenerationSettingsData data) {
        final Properties properties = new Properties();
        properties.put(Property.GROUP_ID.literal(), data.context.groupId);
        properties.put(Property.ARTIFACT_ID.literal(), data.context.artifactId);
        properties.put(Property.VERSION.literal(), data.context.artifactVersion);
        properties.put(Property.PACKAGE.literal(), data.context.packageName);
        properties.put(Property.XOOM_SERVER_VERSION.literal(), data.context.xoomVersion);
        properties.put(Property.DOCKER_IMAGE.literal(), data.deployment.dockerImage);
        properties.put(Property.KUBERNETES_IMAGE.literal(), data.deployment.kubernetesImage);
        properties.put(Property.KUBERNETES_POD_NAME.literal(), data.deployment.kubernetesPod);
        return properties;
    }

}
