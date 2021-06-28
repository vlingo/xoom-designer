// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class GenerationSettingsDataTest {

  @Test
  public void testThatGenerationSettingsDataIsValidated() {
    final GenerationSettingsData data =
            new GenerationSettingsData(contextSettingsData(), modelSettingsData(),
                    deploymentSettingsData(), "/home/projects", true, false, false, "");

    Assertions.assertTrue(data.validate().isEmpty());
  }

  @Test
  public void testThatGenerationSettingsDataValidationFailsDueToRecursiveValueObject() {
    final GenerationSettingsData data =
            new GenerationSettingsData(contextSettingsData(), invalidModelSettingsData(),
                    deploymentSettingsData(), "/home/projects", true, false, false, "");

    final List<String> errors = data.validate();
    Assertions.assertEquals(1, errors.size());
    Assertions.assertEquals("Recursive Value Object relationship", errors.get(0));
  }

  private ContextSettingsData contextSettingsData() {
    return new ContextSettingsData("io.vlingo", "xoomapp",
            "1.0.0", "io.vlingo.xoomapp");
  }

  private ModelSettingsData modelSettingsData() {
    return new ModelSettingsData(persistenceData(),
            Arrays.asList(personAggregateData(), profileAggregateData()), Arrays.asList(rankValueObjectData()));
  }

  private ModelSettingsData invalidModelSettingsData() {
    return new ModelSettingsData(persistenceData(),
            Arrays.asList(personAggregateData(), profileAggregateData()), recursiveValueObjectData());
  }

  private PersistenceData persistenceData() {
    return new PersistenceData("STATE_STORE", true, "EVENT_BASED",
            "IN_MEMORY", "POSTGRES", "MYSQL");
  }

  private AggregateData personAggregateData() {
    final List<StateFieldData> statesFields =
            Arrays.asList(new StateFieldData("id", "Long", ""), new StateFieldData("name", "String", ""));

    final List<AggregateMethodData> methods =
            Arrays.asList(new AggregateMethodData("defineWith", Arrays.asList(new MethodParameterData("name")), true, "PersonDefined"),
                    new AggregateMethodData("changeName", Arrays.asList(new MethodParameterData("name")), false, "PersonNameChanged"));

    final List<DomainEventData> events =
            Arrays.asList(new DomainEventData("PersonDefined", Arrays.asList("id", "name")),
                    new DomainEventData("PersonNameChanged", Arrays.asList("name")));

    final APIData apiData = new APIData("/persons/",
            Arrays.asList(new RouteData("/persons/", "POST", "defineWith", false),
                    new RouteData("/persons/{id}/name", "PATCH", "defineWith", true)));

    return new AggregateData("Person", apiData, events, statesFields, methods, new ConsumerExchangeData(""), new ProducerExchangeData("", ""));
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

    return new AggregateData("Profile", apiData, events, statesFields, methods, new ConsumerExchangeData(""), new ProducerExchangeData("", ""));
  }

  private List<ValueObjectData> recursiveValueObjectData() {
    final ValueObjectData classification =
            new ValueObjectData("Classification",
                    Arrays.asList(new ValueObjectFieldData("name", "String", ""), new ValueObjectFieldData("rank", "Rank", "")));

    return Arrays.asList(rankValueObjectData(), classification);
  }

  private ValueObjectData rankValueObjectData() {
    return new ValueObjectData("Rank",
                    Arrays.asList(new ValueObjectFieldData("points", "int", ""), new ValueObjectFieldData("classification", "Classification", "")));
  }

  private DeploymentSettingsData deploymentSettingsData() {
    return new DeploymentSettingsData("DOCKER", "xoom-app", "", "", 0, 0, 0, 0, false);
  }

}
