package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.infrastructure.restapi.data.*;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CodeGenerationParameterValidationStepTest {

  @Test
  public void testThatParametersAreValidated() {
    final GenerationSettingsData data =
            new GenerationSettingsData(contextSettingsData(), modelSettingsData(),
                    deploymentSettingsData(), "/home/projects", true, false, false, "");

    final TaskExecutionContext context =
            TaskExecutionContextMapper.map(data, GenerationTarget.FILESYSTEM);

    assertDoesNotThrow(() -> new CodeGenerationParameterValidationStep().process(context));
  }

  private ContextSettingsData contextSettingsData() {
    return new ContextSettingsData("io.vlingo", "xoomapp",
            "1.0.0", "io.vlingo.xoomapp");
  }

  private ModelSettingsData modelSettingsData() {
    return new ModelSettingsData(persistenceData(),
            Arrays.asList(personAggregateData(), profileAggregateData()), Collections.emptyList());
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

    private DeploymentSettingsData deploymentSettingsData() {
        return new DeploymentSettingsData("DOCKER", "xoom-app", "", "", 0, 0, 0, 0, false);
    }
}
