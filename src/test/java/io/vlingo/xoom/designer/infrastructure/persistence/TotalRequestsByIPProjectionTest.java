// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.persistence;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestHistoryPreserved;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestHistoryState;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.projection.TextProjectable;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TotalRequestsByIPProjectionTest {

  private World world;
  private StateStore stateStore;
  private Projection projection;
  private Map<String, List<String>> valueToProjectionId;

  @BeforeEach
  public void setUp(){
    world = World.startWithDefaults("projection-test");
    NoOpDispatcher dispatcher = new NoOpDispatcher();
    valueToProjectionId = new ConcurrentHashMap<>();
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, TotalRequestsByIPData.class);
    QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
    projection = world.actorFor(Projection.class, TotalRequestsByIPProjectionActor.class, stateStore);
  }

  private Projectable createProjectable(final RequestHistoryState requestHistoryState, final int version) {
    final String projectionId = UUID.randomUUID().toString();

    final Metadata metadata = Metadata.with(requestHistoryState, "", RequestHistoryPreserved.name());

    final State.TextState rawState =
            new RequestHistoryStateAdapter().toRawState(requestHistoryState.id, requestHistoryState, version, metadata);

    valueToProjectionId.computeIfAbsent(requestHistoryState.ipAddress, s -> new ArrayList<>()).add(projectionId);

    return new TextProjectable(rawState, Collections.emptyList(), projectionId);
  }

  private Projectable createProjectable(final RequestHistoryState requestHistoryState) {
    final String projectionId = UUID.randomUUID().toString();

    final Metadata metadata = Metadata.with(requestHistoryState, "", RequestHistoryPreserved.name());

    final State.TextState rawState =
            new RequestHistoryStateAdapter().toRawState(requestHistoryState.id, requestHistoryState, 1, metadata);

    valueToProjectionId.computeIfAbsent(requestHistoryState.ipAddress, s -> new ArrayList<>()).add(projectionId);

    return new TextProjectable(rawState, Collections.emptyList(), projectionId);
  }

  @Test
  public void testThatTotalRequestsByIPDataIsProjected() {
    final String anIpAddress = "177.0.0.2";
    final String otherIpAddress = "177.134.231.171";
    final CountingProjectionControl control = new CountingProjectionControl();

    final AccessSafely access = control.afterCompleting(3);

    final RequestHistoryState firstState =
            RequestHistoryState.identifiedBy("1").preserve("/resources", anIpAddress);

    final RequestHistoryState secondState =
            RequestHistoryState.identifiedBy("2").preserve("/resources", otherIpAddress);

    final RequestHistoryState thirdState =
            RequestHistoryState.identifiedBy("3").preserve("/resources/sub", otherIpAddress);

    projection.projectWith(createProjectable(firstState), control);
    projection.projectWith(createProjectable(secondState), control);
    projection.projectWith(createProjectable(thirdState), control);

    final Map<String,Integer> confirmations = access.readFrom("confirmations");

    assertEquals(3, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor(anIpAddress, confirmations));
    assertEquals(2, valueOfProjectionIdFor(otherIpAddress, confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read(otherIpAddress, TotalRequestsByIPData.class, interest);
    TotalRequestsByIPData data = interestAccess.readFrom("item", otherIpAddress);
    assertEquals(otherIpAddress, data.ipAddress);
    assertEquals(thirdState.occurredOn.withNano(0), data.lastOccurredOn.withNano(0));
    assertEquals(2l, data.totalRequests);
  }

  private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
    final AtomicInteger totalConfirmations = new AtomicInteger();
    valueToProjectionId.get(valueText).forEach(projectionId -> {
      totalConfirmations.addAndGet(confirmations.get(projectionId));
    });
    return totalConfirmations.get();
  }

}
