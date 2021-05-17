// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.gui.request;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.common.Outcome;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.http.Request;
import io.vlingo.xoom.http.RequestFilter;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.StorageException;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.List;

public class UsageDataFilter extends RequestFilter implements StateStore.WriteResultInterest {

  private final Logger logger;
  private final StateStore stateStore;

  public static UsageDataFilter with(final StateStore stateStore,
                                     final Logger logger) {
    return new UsageDataFilter(stateStore, logger);
  }

  private UsageDataFilter(final StateStore stateStore, final Logger logger) {
    this.logger = logger;
    this.stateStore = stateStore;
  }

  @Override
  public Tuple2<Request, Boolean> filter(final Request request) {
    if(hasStateStore()) {
      final UsageData usageData = UsageData.from(request);
      stateStore.write(usageData.id, usageData, 1, this);
    }
    return Tuple2.from(request, true);
  }

  @Override
  public void stop() {
  }

  private boolean hasStateStore() {
    return stateStore != null;
  }

  @Override
  public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String s, S s1, int i, List<Source<C>> list, Object o) {
    outcome.andThen(result -> result)
            .otherwise(cause -> {
                logger.info("Usage data not written because " + cause.result );
                return cause.result;
            });
  }
}
