// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.codegen.CodeGenerationException;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.infrastructure.restapi.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ProjectionGenerationReportTest {

  private static final String stacktraceLabel = "<br> **Stacktrace**: <br><pre>";
  private static final String designerModelLabel = "<br>**Designer Model**: <br><pre>";
  private static final String expectedExceptionMessage = "io.vlingo.xoom.codegen.CodeGenerationException: Unable to generate project";
  private static final String expectedModelFirstNode = "{\"platformSettings\":{\"platform\":\"JVM\",\"lang\":\"Java\",\"sdkVersion\":\"1.8\",\"xoomVersion\":\"1.0.0\"},\"context\":{\"groupId\":\"io.vlingo\",\"artifactId\":\"xoomapp\",\"artifactVersion\":\"1.0\",\"packageName\":\"io.vlingo.xoomapp\",\"solutionName\":\"io.vlingo\",\"projectName\":\"xoomapp\",\"projectVersion\":\"1.0\",\"namespace\":\"io.vlingo.xoomapp\"}";
  private static final String expectedAction = "**Action**: Project Generation";

  @Test
  public void testThatCodeGenFailureReportIsCreated() {
    final DesignerModel data =
            new DesignerModel(platformSettingsData(), contextSettingsData(), modelSettingsData(),
                    deploymentSettingsData(), schemataSettingsData(), "/home/projects", true, false, false, "");

    final ModelProcessingReport report =
            ModelProcessingReport.onContextMappingFail(GenerationTarget.FILESYSTEM, data, new CodeGenerationException("Unable to generate project"));

    final int designerModelIndex =
            report.details.indexOf(designerModelLabel) + designerModelLabel.length() + 1;

    final int stacktraceIndex =
            report.details.indexOf(stacktraceLabel) + stacktraceLabel.length();

    Assertions.assertFalse(report.details.isEmpty());
    Assertions.assertTrue(report.details.startsWith(expectedAction));
    Assertions.assertTrue(report.details.substring(designerModelIndex).startsWith(expectedModelFirstNode));
    Assertions.assertTrue(report.details.substring(stacktraceIndex).startsWith(expectedExceptionMessage));
  }

  private PlatformSettingsData platformSettingsData() {
    return new PlatformSettingsData("JVM", "Java", "1.8", "1.0.0");
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

    return new AggregateData("Person", apiData, events, statesFields, methods, new ConsumerExchangeData(), new ProducerExchangeData(""));
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
