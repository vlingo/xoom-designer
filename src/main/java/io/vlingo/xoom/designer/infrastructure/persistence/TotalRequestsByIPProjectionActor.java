// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.persistence;

import io.vlingo.xoom.designer.gui.RequestHistoryPreserved;
import io.vlingo.xoom.designer.gui.RequestHistoryState;
import io.vlingo.xoom.designer.gui.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class TotalRequestsByIPProjectionActor extends StateStoreProjectionActor<TotalRequestsByIPData> {

  private String becauseOf;

  public TotalRequestsByIPProjectionActor() {
    this(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  public TotalRequestsByIPProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected TotalRequestsByIPData merge(final TotalRequestsByIPData previousData,
                                        final int previousVersion,
                                        final TotalRequestsByIPData currentData,
                                        final int currentVersion) {
    if (previousVersion == currentVersion) return currentData;

    TotalRequestsByIPData merged = previousData;

    if(RequestHistoryPreserved.matchName(becauseOf)) {
      if(previousData != null) {
        merged = TotalRequestsByIPData.from(previousData.ipAddress, previousData.totalRequests+1, currentData.lastOccurredOn);
      } else {
        merged = currentData;
      }
    } else {
      logger().warn("Operation of type " + becauseOf + " was not matched.");
    }

    return merged;
  }

  @Override
  protected TotalRequestsByIPData currentDataFor(final Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];
    final RequestHistoryState requestHistoryState = projectable.object();
    return TotalRequestsByIPData.from(requestHistoryState.ipAddress, requestHistoryState.occurredOn);
  }

  @Override
  protected String dataIdFor(Projectable projectable) {
    return ((RequestHistoryState) projectable.object()).ipAddress;
  }

  @Override
  protected boolean alwaysWrite() {
    return false;
  }

}
