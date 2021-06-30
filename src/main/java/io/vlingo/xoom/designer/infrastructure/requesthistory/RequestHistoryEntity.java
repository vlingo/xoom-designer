// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;


public class RequestHistoryEntity extends StatefulEntity<RequestHistoryState> implements RequestHistory {

  private RequestHistoryState state;

  public RequestHistoryEntity(final String id) {
    super(id);
    state = RequestHistoryState.identifiedBy(id);
  }

  @Override
  public Completes<RequestHistoryState> preserve(final String uri, final String ipAddress) {
    return apply(state.preserve(uri, ipAddress), RequestHistoryPreserved.name(), () -> state);
  }

  @Override
  protected void state(final RequestHistoryState state) {
    this.state = state;
  }

  @Override
  protected Class<RequestHistoryState> stateType() {
    return RequestHistoryState.class;
  }
}
