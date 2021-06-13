package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

public class ${projectionUnitTestName} {

  private World world;
  private StateStore stateStore;
  private Projection projection;
  private Map<String, String> valueToProjectionId;

  @BeforeEach
  public void setUp() {
    world = World.startWithDefaults("test-state-store-projection");
    NoOpDispatcher dispatcher = new NoOpDispatcher();
    valueToProjectionId = new ConcurrentHashMap<>();
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, ${dataObjectName}.class);
    QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
    projection = world.actorFor(Projection.class, ${projectionName}.class, stateStore);
  }

  <#list testCases as testCase>
  <#list testCase.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  @Test
  public void ${testCase.methodName}() {
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