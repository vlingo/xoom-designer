package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ${projectionUnitTestName} {

  private World world;
  private StateStore stateStore;
  private MockDispatcher dispatcher;
  private ${aggregateProtocolName} entity;

  @BeforeEach
  public void setUp() {
    dispatcher = new MockDispatcher();
    world = World.startWithDefaults("test-state-store-projection");;
    new StateAdapterProvider(world).registerAdapter(${aggregateProtocolName}State.class, new ${aggregateProtocolName}StateAdapter());
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    new StatefulTypeRegistry(world).register(new StatefulTypeRegistry.Info(stateStore, ${aggregateProtocolName}State.class, ${aggregateProtocolName}State.class.getSimpleName()));
    entity = world.actorFor(${aggregateProtocolName}.class, ${entityName}.class, "#1");
  }

  <#list testCases as testCase>

  @Test
  public void ${testCase.methodName}() {
    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);
    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
  </#list>
    ${dataName} data = firstData.to${dataName}();
    final ${aggregateProtocolName}State item = entity.${testCase.methodName}(${testCase.dataObjectParams}).await();

  <#list testCase.statements as statement>
  <#list statement.assertions as assertion>
    ${assertion}
  </#list>
  </#list>
  }
  </#list>
}
