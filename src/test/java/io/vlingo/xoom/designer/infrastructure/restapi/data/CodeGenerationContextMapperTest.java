// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.exchange.ExchangeRole;
import io.vlingo.xoom.designer.codegen.java.schemata.Schema;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.codegen.Label.*;

public class CodeGenerationContextMapperTest {

  @Test
  public void testThatJavaTaskExecutionContextIsMapped() {
    final PlatformSettingsData platform = new PlatformSettingsData("JVM", "Java", "1.8");
    final CodeElementFormatter codeElementFormatter =
            CodeElementFormatter.with(Dialect.withName(platform.lang.toUpperCase()), ReservedWordsHandler.usingSuffix("_"));

    ComponentRegistry.register("defaultCodeFormatter", codeElementFormatter);

    final DesignerModel data =
            new DesignerModel(platform, contextSettingsData(), modelSettingsData(),
                    deploymentSettingsData(), schemataSettingsData(), "/home/projects", true, false, false, "");

    final CodeGenerationParameters codeGenerationParameters =
            CodeGenerationContextMapper.map(data, GenerationTarget.FILESYSTEM, Logger.noOpLogger()).parameters();

    assertStructuralOptions(codeGenerationParameters);
    assertPersistenceParameters(codeGenerationParameters);
    assertModelParameters(codeGenerationParameters);
    assertExchangeParameters(codeGenerationParameters);
  }

  @Test
  public void testThatCsharpTaskExecutionContextIsMapped() {
    final PlatformSettingsData platform = new PlatformSettingsData(".NET", "C_SHARP", "net6.0");
    final CodeElementFormatter codeElementFormatter =
        CodeElementFormatter.with(Dialect.withName(platform.lang.toUpperCase()), ReservedWordsHandler.usingSuffix("_"));

    ComponentRegistry.register("defaultCodeFormatter", codeElementFormatter);

    // For now c# model support only platform and context settings
    final DesignerModel data =
        new DesignerModel(platform, contextSettingsData(), modelSettingsData(),
            deploymentSettingsData(), schemataSettingsData(), "/home/projects", true, false, false, "");

    final CodeGenerationParameters codeGenerationParameters =
        CodeGenerationContextMapper.map(data, GenerationTarget.FILESYSTEM, Logger.noOpLogger()).parameters();

    assertCsharpStructuralOptions(codeGenerationParameters);
  }

  private void assertStructuralOptions(final CodeGenerationParameters codeGenerationParameters) {
    final DeploymentSettings deploymentSettings = codeGenerationParameters.retrieveObject(DEPLOYMENT_SETTINGS);
    Assertions.assertEquals("io.vlingo", codeGenerationParameters.retrieveValue(GROUP_ID));
    Assertions.assertEquals("xoomapp", codeGenerationParameters.retrieveValue(ARTIFACT_ID));
    Assertions.assertEquals("1.0", codeGenerationParameters.retrieveValue(ARTIFACT_VERSION));
    Assertions.assertEquals("{{XOOM_VERSION}}", codeGenerationParameters.retrieveValue(XOOM_VERSION));
    Assertions.assertEquals("xoom-app", deploymentSettings.dockerImage);
    Assertions.assertEquals("DOCKER", deploymentSettings.type.name());
    Assertions.assertEquals("", deploymentSettings.kubernetesImage);
    Assertions.assertEquals("", deploymentSettings.kubernetesPod);
    Assertions.assertNotNull(codeGenerationParameters.retrieveValue(DESIGNER_MODEL_JSON));
    Assertions.assertTrue(codeGenerationParameters.retrieveValue(DESIGNER_MODEL_JSON).startsWith("{\n" +
            "  \"platformSettings\": {\n" +
            "    \"platform\": \"JVM\",\n" +
            "    \"lang\": \"Java\",\n" +
            "    \"sdkVersion\": \"1.8\"\n" +
            "  },\n" +
            "  \"context\": {\n" +
            "    \"groupId\": \"io.vlingo\",\n" +
            "    \"artifactId\": \"xoomapp\",\n" +
            "    \"artifactVersion\": \"1.0\",\n" +
            "    \"packageName\": \"io.vlingo.xoomapp\"\n" +
            "  },\n"));
  }

  private void assertCsharpStructuralOptions(final CodeGenerationParameters codeGenerationParameters) {
    Assertions.assertEquals("io.vlingo", codeGenerationParameters.retrieveValue(GROUP_ID));
    Assertions.assertEquals("xoomapp", codeGenerationParameters.retrieveValue(ARTIFACT_ID));
    Assertions.assertEquals("1.0", codeGenerationParameters.retrieveValue(ARTIFACT_VERSION));
    Assertions.assertEquals("{{XOOM_VERSION}}", codeGenerationParameters.retrieveValue(XOOM_VERSION));
    Assertions.assertNotNull(codeGenerationParameters.retrieveValue(DESIGNER_MODEL_JSON));
    Assertions.assertTrue(codeGenerationParameters.retrieveValue(DESIGNER_MODEL_JSON).startsWith("{\n" +
        "  \"platformSettings\": {\n" +
        "    \"platform\": \".NET\",\n" +
        "    \"lang\": \"C_SHARP\",\n" +
        "    \"sdkVersion\": \"net6.0\"\n" +
        "  },\n" +
        "  \"context\": {\n" +
        "    \"groupId\": \"io.vlingo\",\n" +
        "    \"artifactId\": \"xoomapp\",\n" +
        "    \"artifactVersion\": \"1.0\",\n" +
        "    \"packageName\": \"io.vlingo.xoomapp\"\n" +
        "  },\n"));
  }

  private void assertPersistenceParameters(final CodeGenerationParameters codeGenerationParameters) {
    Assertions.assertEquals("true", codeGenerationParameters.retrieveValue(CQRS));
    Assertions.assertEquals("IN_MEMORY", codeGenerationParameters.retrieveValue(DATABASE));
    Assertions.assertEquals("EVENT_BASED", codeGenerationParameters.retrieveValue(PROJECTION_TYPE));
    Assertions.assertEquals("STATE_STORE", codeGenerationParameters.retrieveValue(STORAGE_TYPE));
    Assertions.assertEquals("POSTGRES", codeGenerationParameters.retrieveValue(COMMAND_MODEL_DATABASE));
    Assertions.assertEquals("MYSQL", codeGenerationParameters.retrieveValue(QUERY_MODEL_DATABASE));
  }

  private void assertModelParameters(final CodeGenerationParameters codeGenerationParameters) {
    Assertions.assertEquals(2, codeGenerationParameters.retrieveAll(VALUE_OBJECT).count());

    final CodeGenerationParameter classificationValueObject =
            codeGenerationParameters.retrieveAll(VALUE_OBJECT)
                    .filter(vo -> vo.value.equals("Classification")).findFirst().get();

    Assertions.assertTrue(classificationValueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .anyMatch(field -> field.value.equals("name") && field.retrieveRelatedValue(FIELD_TYPE).equals("String")
                    && !field.hasAny(COLLECTION_TYPE)));

    Assertions.assertTrue(classificationValueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .anyMatch(field -> field.value.equals("rank") && field.retrieveRelatedValue(FIELD_TYPE).equals("Rank")
                    && field.retrieveRelatedValue(COLLECTION_TYPE).equals("Set")));

    final CodeGenerationParameter personAggregateParameter =
            codeGenerationParameters.retrieveAll(AGGREGATE)
                    .filter(aggregate -> aggregate.value.equals("Person"))
                    .findFirst().get();

    Assertions.assertTrue(personAggregateParameter.retrieveAllRelated(STATE_FIELD)
            .anyMatch(field -> field.value.equals("id") && field.retrieveRelatedValue(FIELD_TYPE).equals("Long")
                    && !field.hasAny(COLLECTION_TYPE)));

    Assertions.assertTrue(personAggregateParameter.retrieveAllRelated(STATE_FIELD)
            .anyMatch(field -> field.value.equals("name") && field.retrieveRelatedValue(FIELD_TYPE).equals("String")
            && field.retrieveRelatedValue(COLLECTION_TYPE).equals("List")));

    Assertions.assertTrue(personAggregateParameter.retrieveAllRelated(AGGREGATE_METHOD)
            .anyMatch(method -> method.value.equals("defineWith") &&
                    method.retrieveRelatedValue(FACTORY_METHOD).equals("true") &&
                    method.retrieveRelatedValue(DOMAIN_EVENT).equals("PersonDefined") &&
                    method.retrieveAllRelated(METHOD_PARAMETER).allMatch(param -> param.value.equals("name"))));

    Assertions.assertTrue(personAggregateParameter.retrieveAllRelated(AGGREGATE_METHOD)
            .anyMatch(method -> method.value.equals("changeName") &&
                    method.retrieveRelatedValue(FACTORY_METHOD).equals("false") &&
                    method.retrieveRelatedValue(DOMAIN_EVENT).equals("PersonNameChanged") &&
                    method.retrieveAllRelated(METHOD_PARAMETER).allMatch(param -> param.value.equals("name"))));

    Assertions.assertTrue(personAggregateParameter.retrieveAllRelated(ROUTE_SIGNATURE)
            .anyMatch(routeSignature -> routeSignature.value.equals("defineWith") &&
                    routeSignature.retrieveRelatedValue(ROUTE_METHOD).equals("POST") &&
                    routeSignature.retrieveRelatedValue(ROUTE_PATH).equals("/persons/") &&
                    routeSignature.retrieveRelatedValue(REQUIRE_ENTITY_LOADING).equals("false")));

    Assertions.assertEquals("/persons/", personAggregateParameter.retrieveRelatedValue(URI_ROOT));
  }

  private void assertExchangeParameters(final CodeGenerationParameters codeGenerationParameters) {
    final CodeGenerationParameter personAggregate =
            codeGenerationParameters.retrieveAll(AGGREGATE)
                    .filter(aggregate -> aggregate.value.equals("Person")).findFirst().get();

    final CodeGenerationParameter consumerExchange =
            personAggregate.retrieveAllRelated(EXCHANGE)
                    .filter(param -> param.retrieveRelatedValue(ROLE, ExchangeRole::valueOf).isConsumer())
                    .findFirst().get();

    final CodeGenerationParameter receiver =
            consumerExchange.retrieveAllRelated(RECEIVER).findFirst().get();

    Assertions.assertEquals("xoomapp-exchange", consumerExchange.value);
    Assertions.assertEquals("vlingo:business:io.vlingo.iam:UserAuthorized:1.0.0", receiver.retrieveOneRelated(SCHEMA).<Schema>object().reference);
    Assertions.assertEquals("defineWith", receiver.retrieveRelatedValue(MODEL_METHOD));

    final CodeGenerationParameter producerExchange =
            personAggregate.retrieveAllRelated(EXCHANGE)
                    .filter(param -> param.retrieveRelatedValue(ROLE, ExchangeRole::valueOf).isProducer())
                    .findFirst().get();

    Assertions.assertEquals("xoomapp-exchange", producerExchange.value);
    Assertions.assertEquals("vlingo:business:io.vlingo.registry", producerExchange.retrieveRelatedValue(SCHEMA_GROUP));
    Assertions.assertEquals("PersonDefined", producerExchange.retrieveRelatedValue(DOMAIN_EVENT));
  }

  private ContextSettingsData contextSettingsData() {
    return new ContextSettingsData("io.vlingo", "xoomapp",
            "1.0", "io.vlingo.xoomapp");
  }

  private ModelSettingsData modelSettingsData() {
    return new ModelSettingsData(persistenceData(),
            Arrays.asList(personAggregateData(), profileAggregateData()),
            valueObjects());
  }

  private SchemataSettingsData schemataSettingsData() {
    return new SchemataSettingsData("localhost", 18787);
  }

  private PersistenceData persistenceData() {
    return new PersistenceData("STATE_STORE", true, "EVENT_BASED",
            "IN_MEMORY", "POSTGRES", "MYSQL");
  }

  private AggregateData personAggregateData() {
    final List<StateFieldData> statesFields =
            Arrays.asList(new StateFieldData("id", "Long", ""), new StateFieldData("name", "String", "List"));

    final List<AggregateMethodData> methods =
            Arrays.asList(new AggregateMethodData("defineWith", Arrays.asList(new MethodParameterData("name")), true, "PersonDefined"),
                    new AggregateMethodData("changeName", Arrays.asList(new MethodParameterData("name")), false, "PersonNameChanged"));

    final List<DomainEventData> events =
            Arrays.asList(new DomainEventData("PersonDefined", Arrays.asList("id", "name")),
                    new DomainEventData("PersonNameChanged", Arrays.asList("name")));

    final APIData apiData = new APIData("/persons/",
            Arrays.asList(new RouteData("/persons/", "POST", "defineWith", false),
                    new RouteData("/persons/{id}/name", "PATCH", "defineWith", true)));

    final ConsumerExchangeData consumerExchange = new ConsumerExchangeData();
    consumerExchange.receivers.add(new ReceiverData("vlingo:business:io.vlingo.iam:UserAuthorized:1.0.0", "defineWith"));

    final ProducerExchangeData producerExchange = new ProducerExchangeData("vlingo:business:io.vlingo.registry");
    producerExchange.outgoingEvents.add("PersonDefined");

    return new AggregateData("Person", apiData, events, statesFields, methods, consumerExchange, producerExchange);
  }

  private AggregateData profileAggregateData() {
    final List<StateFieldData> statesFields =
            Arrays.asList(new StateFieldData("id", "Long", ""), new StateFieldData("status", "String", ""));

    final List<AggregateMethodData> methods =
            Arrays.asList(new AggregateMethodData("publish", Arrays.asList(new MethodParameterData("status")), true, "ProfilePublished"),
                    new AggregateMethodData("changeStatus", Arrays.asList(new MethodParameterData("status")), false, "ProfileStatusChanged"));

    final List<DomainEventData> events =
            Arrays.asList(new DomainEventData("ProfilePublished", Arrays.asList("id", "status")),
                    new DomainEventData("ProfilePublished", Arrays.asList("status")));

    final APIData apiData = new APIData("/profiles/",
            Arrays.asList(new RouteData("/profiles/", "POST", "defineWith", false),
                    new RouteData("/profiles/{id}/status", "PATCH", "defineWith", true)));

    return new AggregateData("Profile", apiData, events, statesFields, methods, new ConsumerExchangeData(), new ProducerExchangeData(""));
  }

  private List<ValueObjectData> valueObjects() {
    final ValueObjectData classification =
            new ValueObjectData("Classification",
                    Arrays.asList(new ValueObjectFieldData("name", "String", ""), new ValueObjectFieldData("rank", "Rank", "Set")));

    return Arrays.asList(rankValueObjectData(), classification);
  }

  private ValueObjectData rankValueObjectData() {
    return new ValueObjectData("Rank",
            Arrays.asList(new ValueObjectFieldData("points", "int", "")));
  }

  private DeploymentSettingsData deploymentSettingsData() {
    return new DeploymentSettingsData("DOCKER", "xoom-app", "", "", 0, 0, 0, 0, false);
  }
}
