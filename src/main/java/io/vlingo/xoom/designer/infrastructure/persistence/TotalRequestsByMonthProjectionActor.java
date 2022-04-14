// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.persistence;

import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestHistoryPreserved;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestHistoryState;
import io.vlingo.xoom.designer.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;

public class TotalRequestsByMonthProjectionActor extends StateStoreProjectionActor<TotalRequestsByMonthData> {

  private String becauseOf;
  private final DateTimeFormatter monthOfYearFormat;

  public TotalRequestsByMonthProjectionActor() {
    this(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  public TotalRequestsByMonthProjectionActor(final StateStore stateStore) {
    super(stateStore);
    this.monthOfYearFormat = DateTimeFormatter.ofPattern("yyyyMM").withZone(UTC);
  }

  @Override
  protected TotalRequestsByMonthData merge(final TotalRequestsByMonthData previousData,
                                           final int previousVersion,
                                           final TotalRequestsByMonthData currentData,
                                           final int currentVersion) {
    if (previousVersion == currentVersion) return currentData;

    TotalRequestsByMonthData merged = previousData;

    if(RequestHistoryPreserved.matchName(becauseOf)) {
      if(previousData != null) {
        merged = TotalRequestsByMonthData.from(previousData.monthOfYear, previousData.totalRequests+1);
      } else {
        merged = currentData;
      }
    } else {
      logger().warn("Operation of type " + becauseOf + " was not matched.");
    }

    return merged;
  }

  @Override
  protected TotalRequestsByMonthData currentDataFor(final Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];
    final RequestHistoryState requestHistoryState = projectable.object();
    return TotalRequestsByMonthData.from(requestHistoryState.occurredOn.format(monthOfYearFormat));
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    return ((RequestHistoryState) projectable.object()).occurredOn.format(monthOfYearFormat);
  }

  @Override
  protected boolean alwaysWrite() {
    return false;
  }

}
