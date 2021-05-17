// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui.request;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.http.*;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Properties;

import static io.vlingo.xoom.http.Body.Empty;
import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;
import static io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType.STATE_STORE;
import static io.vlingo.xoom.turbo.storage.Model.QUERY;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsageDataFilterTest {

  private World world;
  private MockDispatcher dispatcher;

  @BeforeEach
  private void setUp() {
    dispatcher = new MockDispatcher();
    world = World.startWithDefaults("usage-storage-test");
    StateTypeStateStoreMap.stateTypeToStoreName(UsageData.class, UsageData.class.getSimpleName());
  }

  @Test
  public void testThatUsageDataIsStored() {
    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final Header.Headers requestHeader =
            Header.Headers.of(RequestHeader.of(XForwardedFor, "127.0.0.1"));

    final Request request =
            Request.from(Method.POST, URI.create("/resources"), Version.Http1_1, requestHeader, Empty);

    final StateStore stateStore =
            StoreActorBuilder.from(world.stage(), QUERY, singletonList(dispatcher),
                    STATE_STORE, loadDatabaseProperties(), true);

    UsageDataFilter.with(stateStore, world.defaultLogger()).filter(request);

    assertEquals(1, (int) dispatcherAccess.readFrom("dispatched"));
  }

  private Properties loadDatabaseProperties() {
    final Properties properties = new Properties();
    properties.put("query.database", "IN_MEMORY");
    return properties;
  }
}
