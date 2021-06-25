// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface RequestHistory {

  static Completes<RequestHistoryState> preserve(final Stage stage, final String uri, final String ipAddress) {
    final Address address = stage.addressFactory().unique();

    final Definition definition =
            Definition.has(RequestHistoryEntity.class, Definition.parameters(address.idString()));

    final RequestHistory requestHistory =
            stage.actorFor(RequestHistory.class, definition, address);

    return requestHistory.preserve(uri, ipAddress);
  }

  Completes<RequestHistoryState> preserve(final String uri, final String ipAddress);
}
