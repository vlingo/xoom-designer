// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.requesthistory;

import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;

import io.vlingo.xoom.symbio.store.object.StateObject;

public class RequestHistoryState extends StateObject {
  private static final long serialVersionUID = -2921706611627431942L;

  public final String id;
  public final String uri;
  public final String ipAddress;
  public final LocalDateTime occurredOn;

  public static RequestHistoryState identifiedBy(final String id) {
    return new RequestHistoryState(id);
  }

  private RequestHistoryState(final String id) {
    this(id, null, null);
  }

  private RequestHistoryState(final String id,
                              final String uri,
                              final String ipAddress) {
    this.id = id;
    this.uri = uri;
    this.ipAddress = ipAddress;
    this.occurredOn = LocalDateTime.now(UTC);
  }

  public RequestHistoryState preserve(final String uri,
                                      final String ipAddress) {
    return new RequestHistoryState(this.id, uri, ipAddress);
  }

}
