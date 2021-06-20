package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.projection.TextProjectable;
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
import java.util.concurrent.ConcurrentHashMap;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import static org.junit.jupiter.api.Assertions.assertEquals;

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

  private Projectable create${testCase.domainEventName}(${dataObjectName} data) {
    final ${testCase.domainEventName} eventData = new ${testCase.domainEventName}(${testCase.dataObjectParams});

    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(${testCase.domainEventName}.class, 1,
    JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put(data.id, projectionId);
    return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
  }

  @Test
  public void ${testCase.methodName}() {
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);

    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
    </#list>
    projection.projectWith(create${testCase.domainEventName}(firstData), control);
    projection.projectWith(create${testCase.domainEventName}(secondData), control);
    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(2, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor("1", confirmations));
    assertEquals(1, valueOfProjectionIdFor("2", confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read("1", ${dataObjectName}.class, interest);
    ${dataObjectName} item = interestAccess.readFrom("item", "1");
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

    interest = new CountingReadResultInterest();
    interestAccess = interest.afterCompleting(1);
    stateStore.read("2", ${dataObjectName}.class, interest);
    item = interestAccess.readFrom("item", "2");
    assertEquals("2", item.id);
    <#list testCase.statements as statement>
      <#list statement.assertions as assertion>
    ${assertion}
      </#list>
    </#list>
  }

  </#list>
  private int valueOfProjectionIdFor(final String valueText, final Map<String, Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }
}