// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.codegen.*;
import io.vlingo.xoom.designer.codegen.designermodel.DesignerModelFormatter;
import io.vlingo.xoom.designer.codegen.java.ClusterSettings;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.SchemataSettings;
import io.vlingo.xoom.designer.codegen.java.TurboSettings;
import io.vlingo.xoom.designer.codegen.java.exchange.ExchangeRole;
import io.vlingo.xoom.designer.codegen.java.schemata.Schema;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.nio.file.Path;
import java.util.Optional;

import static io.vlingo.xoom.designer.codegen.CodeGenerationProperties.FIELD_TYPE_TRANSLATION;
import static io.vlingo.xoom.designer.codegen.Label.*;

public class CodeGenerationContextMapper {

  private final DesignerModel data;
  private final CodeGenerationContext context;
  private final GenerationTarget generationTarget;
  private final CodeGenerationParameters parameters;
  private final CodeElementFormatter formatter;
  private final Logger logger;

  public static CodeGenerationContext map(final DesignerModel data,
                                         final GenerationTarget generationTarget,
                                         final Logger logger) {
    return new CodeGenerationContextMapper(data, generationTarget, logger).map();
  }

  private CodeGenerationContextMapper(final DesignerModel data,
                                      final GenerationTarget generationTarget,
                                      final Logger logger) {
    this.data = data;
    this.generationTarget = generationTarget;
    final Dialect dialect = data.platformSettings != null ?
            Dialect.withName(data.platformSettings.lang.toUpperCase()) : Dialect.findDefault();
    this.parameters = CodeGenerationParameters.from(DIALECT, dialect);
    this.context = CodeGenerationContextFactory.build(logger, parameters);
    if(dialect.isJava())
      this.formatter = ComponentRegistry.withName("defaultCodeFormatter");
    else
      this.formatter = ComponentRegistry.withName("cSharpCodeFormatter");
    this.logger = logger;

    mapAggregates();
    mapValueObjects();
    mapPersistence();
    mapInfrastructureSettings();
  }

  private CodeGenerationContext map() {
    context.logger(logger).parameters().addAll(parameters);
    return context;
  }

  private void mapAggregates() {
    data.model.aggregateSettings.forEach(aggregate -> {
      final CodeGenerationParameter aggregateParameter =
              CodeGenerationParameter.of(AGGREGATE, formatter.rectifySyntax(aggregate.aggregateName))
                      .relate(URI_ROOT, aggregate.api.rootPath);

      mapStateFields(aggregate, aggregateParameter);
      mapMethods(aggregate, aggregateParameter);
      mapDomainEvents(aggregate, aggregateParameter);
      mapRoutes(aggregate, aggregateParameter);
      mapExchanges(aggregate, aggregateParameter);
      parameters.add(aggregateParameter);
    });
  }

  private void mapValueObjects() {
    data.model.valueObjectSettings.forEach(valueObject -> {
      final CodeGenerationParameter valueObjectParameter =
              CodeGenerationParameter.of(VALUE_OBJECT, formatter.rectifySyntax(valueObject.name));

      valueObject.fields.forEach(field -> {
        final CodeGenerationParameter fieldParameter =
                CodeGenerationParameter.of(VALUE_OBJECT_FIELD, formatter.rectifySyntax(field.name))
                        .relate(FIELD_TYPE, formatter.rectifySyntax((FIELD_TYPE_TRANSLATION.getOrDefault(field.type, field.type))))
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
              CodeGenerationParameter.of(STATE_FIELD, formatter.rectifySyntax(stateField.name))
                      .relate(FIELD_TYPE, formatter.rectifySyntax(FIELD_TYPE_TRANSLATION.getOrDefault(stateField.type, stateField.type)))
                      .relate(COLLECTION_TYPE, stateField.collectionType));
    });
  }

  private void mapMethods(final AggregateData aggregateData,
                          final CodeGenerationParameter aggregateParameter) {
    aggregateData.methods.forEach(methodData -> {
      final CodeGenerationParameter method =
              CodeGenerationParameter.of(AGGREGATE_METHOD, formatter.rectifySyntax(methodData.name))
                      .relate(FACTORY_METHOD, methodData.useFactory)
                      .relate(DOMAIN_EVENT, methodData.event);

      methodData.parameters.forEach(param -> {
        final CollectionMutation collectionMutation =
                CollectionMutation.withSymbol(param.multiplicity);

        final CodeGenerationParameter methodParameter =
                CodeGenerationParameter.of(METHOD_PARAMETER, formatter.rectifySyntax(param.stateField))
                        .relate(ALIAS, formatter.rectifySyntax(param.parameterName))
                        .relate(COLLECTION_MUTATION, collectionMutation);

        method.relate(methodParameter);
      });

      aggregateParameter.relate(method);
    });
  }

  private void mapDomainEvents(final AggregateData aggregateData,
                               final CodeGenerationParameter aggregateParameter) {
    aggregateData.events.forEach(event -> {
      final CodeGenerationParameter eventCodeGenParam =
              CodeGenerationParameter.of(DOMAIN_EVENT, formatter.rectifySyntax(event.name));

      final Optional<CodeGenerationParameter> emitterMethod =
              aggregateParameter.retrieveAllRelated(AGGREGATE_METHOD)
                      .filter(method -> method.hasAny(DOMAIN_EVENT) && method.retrieveRelatedValue(DOMAIN_EVENT).equals(event.name))
                      .findFirst();

      event.fields.stream().map(formatter::rectifySyntax).forEach(field -> {
        final CodeGenerationParameter correspondingStateField =
                aggregateParameter.retrieveAllRelated(STATE_FIELD)
                        .filter(stateField -> stateField.value.equals(field))
                        .findFirst().get();

        final CodeGenerationParameter eventField =
                CodeGenerationParameter.of(STATE_FIELD, field)
                        .relate(FIELD_TYPE, correspondingStateField.retrieveRelatedValue(FIELD_TYPE))
                        .relate(COLLECTION_TYPE, correspondingStateField.retrieveRelatedValue(COLLECTION_TYPE));

        if(emitterMethod.isPresent()) {
          final CodeGenerationParameter placeholderMethodParam =
                  CodeGenerationParameter.of(METHOD_PARAMETER, field)
                          .relate(COLLECTION_MUTATION, CollectionMutation.NONE);

          final CodeGenerationParameter correspondingMethodParam =
                  emitterMethod.get().retrieveAllRelated(METHOD_PARAMETER)
                          .filter(methodParameter -> methodParameter.value.equals(field))
                          .findFirst().orElse(placeholderMethodParam);

          eventField.relate(correspondingMethodParam.retrieveOneRelated(ALIAS))
                  .relate(correspondingMethodParam.retrieveOneRelated(COLLECTION_MUTATION));
        }

        eventCodeGenParam.relate(eventField);
      });

      aggregateParameter.relate(eventCodeGenParam);
    });
  }

  private void mapRoutes(final AggregateData aggregateData,
                         final CodeGenerationParameter aggregateParameter) {
    aggregateData.api.routes.forEach(route -> {
      final CodeGenerationParameter routeParameter =
              CodeGenerationParameter.of(ROUTE_SIGNATURE, formatter.rectifySyntax(route.aggregateMethod))
                      .relate(ROUTE_METHOD, route.httpMethod)
                      .relate(ROUTE_PATH, route.path.replace("*", ""))
                      .relate(REQUIRE_ENTITY_LOADING, route.requireEntityLoad);

      aggregateParameter.relate(routeParameter);
    });
  }

  private void mapExchanges(final AggregateData aggregate,
                            final CodeGenerationParameter aggregateParameter) {
    if(aggregate.hasConsumerExchange()) {
      final CodeGenerationParameter consumerExchange =
              CodeGenerationParameter.of(Label.EXCHANGE, data.defaultExchangeName())
                      .relate(Label.ROLE, ExchangeRole.CONSUMER);

      aggregate.consumerExchange.receivers.forEach(receiver -> {
        consumerExchange.relate(CodeGenerationParameter.of(RECEIVER)
                .relate(MODEL_METHOD, formatter.rectifySyntax(receiver.aggregateMethod))
                .relate(CodeGenerationParameter.ofObject(SCHEMA, new Schema(receiver.schema))));
      });
      aggregateParameter.relate(consumerExchange);
    }
    if(aggregate.hasProducerExchange()) {
      final CodeGenerationParameter producerExchange =
              CodeGenerationParameter.of(Label.EXCHANGE, data.defaultExchangeName())
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

  private void mapInfrastructureSettings() {
    final TurboSettings turboSettings =
            TurboSettings.with(data.deployment.httpServerPort, data.deployment.producerExchangePort);

    final ClusterSettings clusterSettings =
            ClusterSettings.with(data.deployment.clusterPort, data.deployment.clusterTotalNodes);

    final SchemataSettings schemataSettings =
            SchemataSettings.with(data.schemata.host, data.schemata.port, Configuration.resolveSchemataServiceDNS());

    final Path definitiveFolder =
            generationTarget.definitiveFolderFor(context.generationId, data.context.artifactId, data.projectDirectory);

    final DeploymentSettings deploymentSettings =
            DeploymentSettings.with(DeploymentType.of(data.deployment.type),
                    data.deployment.dockerImage, data.deployment.kubernetesImage,
                    data.deployment.kubernetesPod, data.deployment.producerExchangePort);

    parameters.add(APPLICATION_NAME, data.context.artifactId)
            .add(USE_ANNOTATIONS, data.useAnnotations)
            .add(USE_AUTO_DISPATCH, data.useAutoDispatch)
            .add(GROUP_ID, data.context.groupId)
            .add(ARTIFACT_ID, data.context.artifactId)
            .add(ARTIFACT_VERSION, data.context.artifactVersion)
            .add(PACKAGE, formatter.rectifyPackageSyntax(data.context.packageName))
            .add(XOOM_VERSION, Configuration.resolveDefaultXoomVersion())
            .add(DEPLOYMENT_SETTINGS, deploymentSettings)
            .add(CLUSTER_SETTINGS, clusterSettings)
            .add(TURBO_SETTINGS, turboSettings)
            .add(SCHEMATA_SETTINGS, schemataSettings)
            .add(TARGET_FOLDER, definitiveFolder.toString())
            .add(DESIGNER_MODEL_JSON, DesignerModelFormatter.format(data))
            .add(WEB_UI_DIALECT, data.generateUI != null && data.generateUI ? data.generateUIWith : "");
    if(data.platformSettings != null)
      parameters
            .add(SDK_VERSION, data.platformSettings.sdkVersion)
            .add(VLINGO_VERSION, data.platformSettings.vlingoVersion);
  }

}
