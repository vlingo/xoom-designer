// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.ClusterSettings;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.TurboSettings;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole;

import java.nio.file.Path;

import static io.vlingo.xoom.designer.task.Agent.WEB;
import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties.FIELD_TYPE_TRANSLATION;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

public class TaskExecutionContextMapper {

  private final GenerationSettingsData data;
  private final TaskExecutionContext context;
  private final GenerationTarget generationTarget;
  private final CodeGenerationParameters parameters;
  private final TemplateCustomFunctions templateCustomFunctions;

  public static TaskExecutionContext from(final GenerationSettingsData data,
                                          final GenerationTarget generationTarget) {
    return new TaskExecutionContextMapper(data, generationTarget).map();
  }

  private TaskExecutionContextMapper(final GenerationSettingsData data, final GenerationTarget generationTarget) {
    this.data = data;
    this.generationTarget = generationTarget;
    this.context = TaskExecutionContext.executedFrom(WEB);
    this.templateCustomFunctions = TemplateCustomFunctions.instance();
    this.parameters = CodeGenerationParameters.from(DIALECT, Dialect.JAVA);
    mapAggregates(); mapValueObjects(); mapPersistence(); mapStructuralOptions();
  }

  private TaskExecutionContext map() {
    return context.with(parameters);
  }

  private void mapAggregates() {
    data.model.aggregateSettings.forEach(aggregate -> {
      final CodeGenerationParameter aggregateParameter =
              CodeGenerationParameter.of(AGGREGATE, aggregate.aggregateName)
                      .relate(URI_ROOT, aggregate.api.rootPath);

      mapStateFields(aggregate, aggregateParameter);
      mapDomainEvents(aggregate, aggregateParameter);
      mapMethods(aggregate, aggregateParameter);
      mapRoutes(aggregate, aggregateParameter);
      mapExchanges(aggregate, aggregateParameter);
      parameters.add(aggregateParameter);
    });
  }

  private void mapValueObjects() {
    data.model.valueObjectSettings.forEach(valueObject -> {
      final CodeGenerationParameter valueObjectParameter =
              CodeGenerationParameter.of(VALUE_OBJECT, valueObject.name);

      valueObject.fields.forEach(field -> {
        final CodeGenerationParameter fieldParameter =
                CodeGenerationParameter.of(VALUE_OBJECT_FIELD, field.name)
                        .relate(FIELD_TYPE, FIELD_TYPE_TRANSLATION.getOrDefault(field.type, field.type))
                        .relate(COLLECTION_TYPE, field.collectionType);

        valueObjectParameter.relate(fieldParameter);
      });

      parameters.add(valueObjectParameter);
    });
  }

  private void mapStateFields(final AggregateData aggregateData,
                              final CodeGenerationParameter aggregateParameter) {
    aggregateData.stateFields.forEach(stateField -> {
      aggregateParameter.relate(
              CodeGenerationParameter.of(STATE_FIELD, stateField.name)
                      .relate(FIELD_TYPE, FIELD_TYPE_TRANSLATION.getOrDefault(stateField.type, stateField.type))
                      .relate(COLLECTION_TYPE, stateField.collectionType));
    });
  }

  private void mapMethods(final AggregateData aggregateData,
                          final CodeGenerationParameter aggregateParameter) {
    aggregateData.methods.forEach(methodData -> {
      final CodeGenerationParameter method =
              CodeGenerationParameter.of(AGGREGATE_METHOD, methodData.name)
                      .relate(FACTORY_METHOD, methodData.useFactory)
                      .relate(DOMAIN_EVENT, methodData.event);

      methodData.parameters.forEach(param -> {
        final CollectionMutation collectionMutation =
                CollectionMutation.withSymbol(param.multiplicity);

        final CodeGenerationParameter methodParameter =
                CodeGenerationParameter.of(METHOD_PARAMETER, param.stateField)
                        .relate(ALIAS, param.parameterName)
                        .relate(COLLECTION_MUTATION, collectionMutation);

        method.relate(methodParameter);
      });

      aggregateParameter.relate(method);
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
                      .relate(ROUTE_PATH, route.path)
                      .relate(REQUIRE_ENTITY_LOADING, route.requireEntityLoad);

      aggregateParameter.relate(routeParameter);
    });
  }

  private void mapExchanges(final AggregateData aggregate,
                            final CodeGenerationParameter aggregateParameter) {
    if(aggregate.hasConsumerExchange()) {
      final CodeGenerationParameter consumerExchange =
              CodeGenerationParameter.of(Label.EXCHANGE, aggregate.consumerExchange.exchangeName)
                      .relate(Label.ROLE, ExchangeRole.CONSUMER);

      aggregate.consumerExchange.receivers.forEach(receiver -> {
        consumerExchange.relate(CodeGenerationParameter.of(RECEIVER)
                .relate(MODEL_METHOD, receiver.aggregateMethod)
                .relate(SCHEMA, receiver.schema));
      });
      aggregateParameter.relate(consumerExchange);
    }
    if(aggregate.hasProducerExchange()) {
      final CodeGenerationParameter producerExchange =
              CodeGenerationParameter.of(Label.EXCHANGE, aggregate.producerExchange.exchangeName)
                      .relate(Label.SCHEMA_GROUP, aggregate.producerExchange.schemaGroup)
                      .relate(Label.ROLE, ExchangeRole.PRODUCER);

      aggregate.producerExchange.outgoingEvents.forEach(eventName -> producerExchange.relate(DOMAIN_EVENT, eventName));

      aggregateParameter.relate(producerExchange);
    }
  }

  private void mapPersistence() {
    parameters.add(CQRS, data.model.persistenceSettings.useCQRS.toString())
            .add(DATABASE, data.model.persistenceSettings.database)
            .add(PROJECTION_TYPE, data.model.persistenceSettings.projections)
            .add(STORAGE_TYPE, data.model.persistenceSettings.storageType)
            .add(COMMAND_MODEL_DATABASE, data.model.persistenceSettings.commandModelDatabase)
            .add(QUERY_MODEL_DATABASE, data.model.persistenceSettings.queryModelDatabase);
  }

  private void mapStructuralOptions() {
    final TurboSettings turboSettings =
            TurboSettings.with(data.deployment.httpServerPort, data.deployment.producerExchangePort);

    final ClusterSettings clusterSettings =
            ClusterSettings.with(data.deployment.clusterPort, data.deployment.clusterTotalNodes);

    final Path definitiveFolder =
            generationTarget.definitiveFolderFor(context.executionId, data.context.artifactId, data.projectDirectory);

    parameters.add(APPLICATION_NAME, data.context.artifactId)
            .add(USE_ANNOTATIONS, data.useAnnotations)
            .add(USE_AUTO_DISPATCH, data.useAutoDispatch)
            .add(GROUP_ID, data.context.groupId)
            .add(ARTIFACT_ID, data.context.artifactId)
            .add(VERSION, data.context.artifactVersion)
            .add(PACKAGE, data.context.packageName)
            .add(XOOM_VERSION, Configuration.resolveDefaultXoomVersion())
            .add(DEPLOYMENT, data.deployment.type)
            .add(DOCKER_IMAGE, data.deployment.dockerImage)
            .add(KUBERNETES_IMAGE, data.deployment.kubernetesImage)
            .add(PRODUCER_EXCHANGE_PORT, data.deployment.producerExchangePort)
            .add(KUBERNETES_POD_NAME, data.deployment.kubernetesPod)
            .add(WEB_UI_DIALECT, data.generateUI ? data.generateUIWith : "")
            .add(CLUSTER_SETTINGS, clusterSettings)
            .add(TURBO_SETTINGS, turboSettings)
            .add(TARGET_FOLDER, definitiveFolder.toString())
            .add(DESIGNER_MODEL_JSON, JsonSerialization.serialized(data));
  }

}
