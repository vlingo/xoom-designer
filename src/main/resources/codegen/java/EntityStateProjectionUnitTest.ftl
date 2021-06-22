package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

<#list imports as import>
  import ${import.qualifiedClassName};
</#list>

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ${projectionUnitTestName} {

private World world;
private StateStore stateStore;
private ${aggregateProtocolName} entity;

@BeforeEach
public void setUp() {
world = World.startWithDefaults("test-state-store-projection");
NoOpDispatcher dispatcher = new NoOpDispatcher();
stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, ${dataObjectName}.class);
QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
entity = world.actorFor(${aggregateProtocolName}.class, ${entityName}.class, "1");
}

<#list testCases as testCase>
  @Test
  public void ${testCase.methodName}() {

  <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
  </#list>
  final ${testCase.domainEventName} item = entity.${testCase.methodName}(${testCase.dataObjectParams}).await();

  <#list testCase.preliminaryStatements as statement>
    ${statement}
  </#list>
  <#list testCase.statements as statement>
    <#list statement.resultAssignment as resultAssignment>
      ${resultAssignment}
    </#list>
    <#list statement.assertions as assertion>
      ${assertion}
      </#list>
    </#list>
  }

  </#list>
}