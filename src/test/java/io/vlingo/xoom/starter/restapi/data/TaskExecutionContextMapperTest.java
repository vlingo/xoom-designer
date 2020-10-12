// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.codegen.parameter.Label.*;

public class TaskExecutionContextMapperTest {

    @Test
    public void testThatTaskExecutionContextIsMapped() {
        final GenerationSettingsData data =
                new GenerationSettingsData(contextSettingsData(), modelSettingsData(),
                        deploymentSettingsData(), "/home/projects", true, false);

        final CodeGenerationParameters codeGenerationParameters =
                TaskExecutionContextMapper.from(data).codeGenerationParameters();

        assertStructuralOptions(codeGenerationParameters);
        assertPersistenceParameters(codeGenerationParameters);
        assertModelParameters(codeGenerationParameters);
    }

    private void assertStructuralOptions(final CodeGenerationParameters codeGenerationParameters) {
        Assertions.assertEquals("io.vlingo", codeGenerationParameters.retrieveValue(GROUP_ID));
        Assertions.assertEquals("xoomapp", codeGenerationParameters.retrieveValue(ARTIFACT_ID));
        Assertions.assertEquals("1.0", codeGenerationParameters.retrieveValue(VERSION));
        Assertions.assertEquals("1.3.4-SNAPSHOT", codeGenerationParameters.retrieveValue(XOOM_SERVER_VERSION));
        Assertions.assertEquals("xoom-app", codeGenerationParameters.retrieveValue(DOCKER_IMAGE));
        Assertions.assertEquals("DOCKER", codeGenerationParameters.retrieveValue(DEPLOYMENT));
        Assertions.assertEquals("", codeGenerationParameters.retrieveValue(KUBERNETES_IMAGE));
        Assertions.assertEquals("", codeGenerationParameters.retrieveValue(KUBERNETES_POD_NAME));
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
        final CodeGenerationParameter personAggregateParameter =
                codeGenerationParameters.retrieveAll(AGGREGATE)
                        .filter(aggregate -> aggregate.value.equals("Person"))
                        .findFirst().get();


        Assertions.assertTrue(personAggregateParameter.retrieveAll(STATE_FIELD)
                .anyMatch(field -> field.value.equals("id") && field.relatedParameterValueOf(FIELD_TYPE).equals("Long")));

        Assertions.assertTrue(personAggregateParameter.retrieveAll(STATE_FIELD)
                .anyMatch(field -> field.value.equals("name") && field.relatedParameterValueOf(FIELD_TYPE).equals("String")));

        Assertions.assertTrue(personAggregateParameter.retrieveAll(AGGREGATE_METHOD)
                .anyMatch(method -> method.value.equals("defineWith") &&
                        method.relatedParameterValueOf(FACTORY_METHOD).equals("true") &&
                        method.relatedParameterValueOf(DOMAIN_EVENT).equals("PersonDefined") &&
                        method.retrieveAll(METHOD_PARAMETER).allMatch(param -> param.value.equals("name"))));

        Assertions.assertTrue(personAggregateParameter.retrieveAll(AGGREGATE_METHOD)
                .anyMatch(method -> method.value.equals("changeName") &&
                        method.relatedParameterValueOf(FACTORY_METHOD).equals("false") &&
                        method.relatedParameterValueOf(DOMAIN_EVENT).equals("PersonNameChanged") &&
                        method.retrieveAll(METHOD_PARAMETER).allMatch(param -> param.value.equals("name"))));

        Assertions.assertTrue(personAggregateParameter.retrieveAll(ROUTE_SIGNATURE)
                .anyMatch(routeSignature -> routeSignature.value.equals("defineWith") &&
                        routeSignature.relatedParameterValueOf(ROUTE_METHOD).equals("POST") &&
                        routeSignature.relatedParameterValueOf(ROUTE_PATH).equals("/persons/") &&
                        routeSignature.relatedParameterValueOf(REQUIRE_ENTITY_LOAD).equals("false")));

        Assertions.assertEquals("/persons/", personAggregateParameter.relatedParameterValueOf(URI_ROOT));
    }

    private ContextSettingsData contextSettingsData() {
        return new ContextSettingsData("io.vlingo", "xoomapp",
                "1.0", "io.vlingo.xoomapp", "1.3.4-SNAPSHOT");
    }

    private ModelSettingsData modelSettingsData() {
        return new ModelSettingsData(persistenceData(),
                Arrays.asList(personAggregateData(), profileAggregateData()));
    }

    private PersistenceData persistenceData() {
        return new PersistenceData("STATE_STORE", true, "EVENT_BASED",
                "IN_MEMORY", "POSTGRES", "MYSQL");
    }

    private AggregateData personAggregateData() {
        final List<StateFieldData> statesFields =
                Arrays.asList(new StateFieldData("id", "Long"), new StateFieldData("name", "String"));

        final List<AggregateMethodData> methods =
                Arrays.asList(new AggregateMethodData("defineWith", Arrays.asList("name"), true, "PersonDefined"),
                        new AggregateMethodData("changeName", Arrays.asList("name"), false, "PersonNameChanged"));

        final List<DomainEventData> events =
                Arrays.asList(new DomainEventData("PersonDefined", Arrays.asList("id", "name")),
                        new DomainEventData("PersonNameChanged", Arrays.asList("name")));

        final APIData apiData = new APIData("/persons/",
                Arrays.asList(new RouteData("/persons/", "POST", "defineWith", false),
                        new RouteData("/persons/{id}/name", "PATCH", "defineWith", true)));

        return new AggregateData("Person", apiData, events, statesFields, methods);
    }

    private AggregateData profileAggregateData() {
        final List<StateFieldData> statesFields =
                Arrays.asList(new StateFieldData("id", "Long"), new StateFieldData("status", "String"));

        final List<AggregateMethodData> methods =
                Arrays.asList(new AggregateMethodData("publish", Arrays.asList("status"), true, "ProfilePublished"),
                        new AggregateMethodData("changeStatus", Arrays.asList("status"), false, "ProfileStatusChanged"));

        final List<DomainEventData> events =
                Arrays.asList(new DomainEventData("ProfilePublished", Arrays.asList("id", "status")),
                        new DomainEventData("ProfilePublished", Arrays.asList("status")));

        final APIData apiData = new APIData("/profiles/",
                Arrays.asList(new RouteData("/profiles/", "POST", "defineWith", false),
                        new RouteData("/profiles/{id}/status", "PATCH", "defineWith", true)));

        return new AggregateData("Profile", apiData, events, statesFields, methods);
    }

    private DeploymentSettingsData deploymentSettingsData() {
        return new DeploymentSettingsData(0, "DOCKER", "xoom-app", "", "");
    }

}
