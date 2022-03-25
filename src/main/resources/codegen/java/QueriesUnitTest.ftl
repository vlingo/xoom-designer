package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.Outcome;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.StorageException;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ${queriesUnitTestName} {

  private World world;
  private StateStore stateStore;
  private ${queriesName} queries;

  @BeforeEach
  public void setUp(){
    world = World.startWithDefaults("test-state-store-query");
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(new NoOpDispatcher()));
    StatefulTypeRegistry.registerAll(world, stateStore, ${dataObjectName}.class);
    queries = world.actorFor(${queriesName}.class, ${queriesActorName}.class, stateStore);
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
  <#if compositeId?has_content>
  @Test
  public void ${queryByIdMethodName}EmptyResult(){
    final ${dataObjectName} result = queries.${queryByIdMethodName}(${compositeId}"1").await();
    assertEquals("", result.id);
  }
  <#else>
  @Test
  public void ${queryByIdMethodName}EmptyResult(){
    final ${dataObjectName} result = queries.${queryByIdMethodName}("1").await();
    assertEquals("", result.id);
  }
  </#if>

  private static final StateStore.WriteResultInterest NOOP_WRITER = new StateStore.WriteResultInterest() {
    @Override
    public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String s, S s1, int i, List<Source<C>> list, Object o) {

    }
  };

}