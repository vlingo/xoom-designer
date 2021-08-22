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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.time.LocalDateTime;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

<#macro textWarbleProjectable domainEventName dataObjectParams version>
  private Projectable create${domainEventName}(${dataName} data) {
    final ${domainEventName} eventData = new ${domainEventName}(${dataObjectParams});

    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(${domainEventName}.class, 1, JsonSerialization.serialized(eventData), ${version}, Metadata.withObject(eventData));

    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put(data.id, projectionId);

    return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
  }
</#macro>

<#macro factoryMethodTestStatements domainEventName>

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);
    projection.projectWith(create${domainEventName}(firstData.to${dataName}()), control);
    projection.projectWith(create${domainEventName}(secondData.to${dataName}()), control);
    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(2, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));
    assertEquals(1, valueOfProjectionIdFor(secondData.id, confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read(firstData.id, ${dataObjectName}.class, interest);
    ${dataObjectName} item = interestAccess.readFrom("item", firstData.id);
</#macro>

<#macro factoryMethodTestAssertion>

    interest = new CountingReadResultInterest();
    interestAccess = interest.afterCompleting(1);
    stateStore.read(secondData.id, ${dataObjectName}.class, interest);
    item = interestAccess.readFrom("item", secondData.id);
    assertEquals(secondData.id, item.id);
</#macro>

<#macro updateTestStatements domainEventName>
    registerExample${aggregateProtocolName}(firstData.to${dataName}(), secondData.to${dataName}());

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(1);
    projection.projectWith(create${domainEventName}(firstData.to${dataName}()), control);
    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(1, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read(firstData.id, ${dataObjectName}.class, interest);
    ${dataObjectName} item = interestAccess.readFrom("item", firstData.id);
</#macro>
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

  private void registerExample${aggregateProtocolName}(${dataName} firstData, ${dataName} secondData) {
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);
    projection.projectWith(create${domainEventName}(firstData), control);
    projection.projectWith(create${domainEventName}(secondData), control);
  }

  <#list testCases as testCase>
  @Test
  public void ${testCase.methodName}() {
    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
    </#list>
    <#if testCase.factoryMethod>
      <@factoryMethodTestStatements testCase.domainEventName />
    <#else>
      <@updateTestStatements testCase.domainEventName />
    </#if>

    <#list testCase.statements as statement>
      <#list statement.assertions as assertion>
    ${assertion}
      </#list>
      <#if testCase.factoryMethod>
        <@factoryMethodTestAssertion />
        <#list statement.secondAssertions as assertion>
    ${assertion}
        </#list>
      </#if>
    </#list>
  }

  </#list>
  private int valueOfProjectionIdFor(final String valueText, final Map<String, Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }

<#list testCases as testCase>
<#if testCase.factoryMethod>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 1/>
<#else>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 2/>
</#if>

</#list>
}
