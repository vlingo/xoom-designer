// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestHistoryState;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestPreservationFilter;
import io.vlingo.xoom.designer.infrastructure.userinterface.infrastructure.persistence.RequestHistoryStateAdapter;
import io.vlingo.xoom.http.*;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.StateAdapterProvider;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;

import static io.vlingo.xoom.http.Body.Empty;
import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestPreservationFilterTest {

  private World world;
  private StateStore store;
  private MockDispatcher dispatcher;

  @BeforeEach
  private void setUp() {
    dispatcher = new MockDispatcher();
    world = World.startWithDefaults("request-preservation-filter-test");
    new StateAdapterProvider(world).registerAdapter(RequestHistoryState.class, new RequestHistoryStateAdapter());
    store = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    new StatefulTypeRegistry(world).register(new StatefulTypeRegistry.Info(store, RequestHistoryState.class, RequestHistoryState.class.getSimpleName()));
  }

  @Test
  public void testThatRequestIsFilteredAndPreserved() {
    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final Header.Headers requestHeader =
            Header.Headers.of(RequestHeader.of(XForwardedFor, "127.0.0.1"));

    final Request request =
            Request.from(Method.POST, URI.create("/resources"), Version.Http1_1, requestHeader, Empty);

    RequestPreservationFilter.with(world.stage()).filter(request);

    assertEquals(1, (int) dispatcherAccess.readFrom("dispatched"));
  }

}
