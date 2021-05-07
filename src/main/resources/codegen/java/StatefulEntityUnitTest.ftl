package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ${entityUnitTestName} {

  private World world;
  private StateStore store;
  private ${dispatcherName} dispatcher;
  private ${aggregateProtocolName} ${aggregateProtocolVariable};

  @BeforeEach
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setUp(){
    dispatcher = new ${dispatcherName}();
    world = World.startWithDefaults("test-stateful");
    new StateAdapterProvider(world).registerAdapter(${stateName}.class, new ${adapterName}());
    store = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    new StatefulTypeRegistry(world).register(new Info(store, ${stateName}.class, ${stateName}.class.getSimpleName()));
    ${aggregateProtocolVariable} = world.actorFor(${aggregateProtocolName}.class, ${entityName}.class, "#1");
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
    ${testCase.resultAssignmentStatement}

    <#list testCase.assertions as assertion>
    ${assertion}
    </#list>
  }

  </#list>
  <#if auxiliaryEntityCreation.required>
  <#list auxiliaryEntityCreation.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  private void ${auxiliaryEntityCreation.methodName}() {
    ${auxiliaryEntityCreation.statement}
  }
  </#if>
}
