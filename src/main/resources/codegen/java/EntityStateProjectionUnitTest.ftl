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
<#macro textWarbleProjectable version operation>
	private Projectable create${operation}(final String id, final int value, final String operation) {
		final String valueText = Integer.toString(value);
		${testCases?first.dataDeclarations?first}

		final TextState state = new TextState(id, ${dataName}.class, 1, JsonSerialization.serialized(firstData.to${dataName}()), ${version},
		Metadata.with(firstData.to${dataName}(), valueText, operation));
		final String projectionId = UUID.randomUUID().toString();

		valueToProjectionId.put(valueText, projectionId);

		return new TextProjectable(state, Collections.emptyList(), projectionId);
	}
</#macro>

public class ${projectionUnitTestName} {
  private Projection projection;
  private StateStore store;
  private Map<String,String> valueToProjectionId;
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
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(3);

    projection.projectWith(create${testCase.domainEventName}("1", 1, "${testCase.domainEventName}"), control);
    projection.projectWith(create${testCase.domainEventName}("2", 2, "${testCase.domainEventName}"), control);
    projection.projectWith(create${testCase.domainEventName}("3", 3, "${testCase.domainEventName}"), control);

    final Map<String,Integer> confirmations = access.readFrom("confirmations");

    assertEquals(3, confirmations.size());

    assertEquals(1, valueOfProjectionIdFor("1", confirmations));
    assertEquals(1, valueOfProjectionIdFor("2", confirmations));
    assertEquals(1, valueOfProjectionIdFor("3", confirmations));

		assertEquals(3, ((Map) access.readFrom("confirmations")).size());
  }

  <@textWarbleProjectable 1 testCase.domainEventName/>
  <#else>

	@Test
	public void ${testCase.methodName}() {
		final CountingProjectionControl control = new CountingProjectionControl();

		final AccessSafely accessControl = control.afterCompleting(6);

	  projection.projectWith(create${testCases?first.domainEventName}("1", 1, "${testCases?first.domainEventName}"), control);
	  projection.projectWith(create${testCases?first.domainEventName}("2", 2, "${testCases?first.domainEventName}"), control);
	  projection.projectWith(create${testCases?first.domainEventName}("3", 3, "${testCases?first.domainEventName}"), control);

	  projection.projectWith(create${testCase.domainEventName}("1", 4, "${testCase.domainEventName}"), control);
	  projection.projectWith(create${testCase.domainEventName}("2", 5, "${testCase.domainEventName}"), control);
	  projection.projectWith(create${testCase.domainEventName}("3", 6, "${testCase.domainEventName}"), control);

		final Map<String,Integer> confirmations = accessControl.readFrom("confirmations");

		assertEquals(6, confirmations.size());

		assertEquals(1, valueOfProjectionIdFor("1", confirmations));
		assertEquals(1, valueOfProjectionIdFor("2", confirmations));
		assertEquals(1, valueOfProjectionIdFor("3", confirmations));

		assertEquals(6, ((Map) accessControl.readFrom("confirmations")).size());
	}

	<@textWarbleProjectable 2 testCase.domainEventName/>
  </#if>
</#list>

  @AfterEach
  public void tearDown() {
    world.terminate();
  }

  private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }

}
