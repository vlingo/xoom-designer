package ${packageName};

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.projection.TextProjectable;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State.TextState;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>
<#macro textWarbleProjectable operation>
	private Projectable create${operation}(final ${dataObjectName} data, final int version, final String operation) {

		final TextState state = new TextState(data.id, ${dataName}.class, 1, JsonSerialization.serialized(data.to${dataName}()), version,
		Metadata.with(data.to${dataName}(), data.id, operation));

		final String projectionId = UUID.randomUUID().toString();

		valueToProjectionId.put(data.id, projectionId);

		return new TextProjectable(state, Collections.emptyList(), projectionId);
	}
</#macro>

public class ${projectionUnitTestName} {
  private Projection projection;
  private StateStore store;
  private Map<String, String> valueToProjectionId;
  private World world;

  @BeforeEach
  public void setUp() {
    world = World.startWithDefaults("test-state-store-projection");
    store = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Arrays.asList(new NoOpDispatcher()));
    projection = world.actorFor(Projection.class, ${projectionName}.class, store);
    StatefulTypeRegistry.registerAll(world, store, ${dataObjectName}.class);
    valueToProjectionId = new HashMap<>();
  }

<#list testCases as testCase>
	<#if testCase.factoryMethod>
  @Test
  public void ${testCase.methodName}() {
    <#list testCase.dataDeclarations as dataDeclaration>
		${dataDeclaration}
    </#list>
		final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);

    projection.projectWith(create${testCase.domainEventName}(firstData, 1, "${testCase.domainEventName}"), control);
    projection.projectWith(create${testCase.domainEventName}(secondData, 1, "${testCase.domainEventName}"), control);

    final Map<String,Integer> confirmations = access.readFrom("confirmations");

    assertEquals(2, confirmations.size());

    assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));
    assertEquals(1, valueOfProjectionIdFor(secondData.id, confirmations));

		assertEquals(2, ((Map) access.readFrom("confirmations")).size());
  }
  <#else>

	@Test
	public void ${testCase.methodName}() {
    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
    </#list>
	  final CountingProjectionControl control = new CountingProjectionControl();

		final AccessSafely accessControl = control.afterCompleting(4);

	  projection.projectWith(create${testCases?first.domainEventName}(firstData, 1, "${testCases?first.domainEventName}"), control);
	  projection.projectWith(create${testCases?first.domainEventName}(secondData, 1, "${testCases?first.domainEventName}"), control);

	  projection.projectWith(create${testCase.domainEventName}(firstData, 2, "${testCase.domainEventName}"), control);
	  projection.projectWith(create${testCase.domainEventName}(secondData, 2, "${testCase.domainEventName}"), control);

		final Map<String,Integer> confirmations = accessControl.readFrom("confirmations");

		assertEquals(4, confirmations.size());

		assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));
		assertEquals(1, valueOfProjectionIdFor(secondData.id, confirmations));

		assertEquals(4, ((Map) accessControl.readFrom("confirmations")).size());
	}
	</#if>
</#list>

  @AfterEach
  public void tearDown() {
    world.terminate();
  }

  private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }
<#list testCases as testCase>

  <@textWarbleProjectable testCase.domainEventName/>
</#list>
}
