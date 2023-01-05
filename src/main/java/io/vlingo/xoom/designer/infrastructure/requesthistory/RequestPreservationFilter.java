// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.http.Request;
import io.vlingo.xoom.http.RequestFilter;

import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;

public class RequestPreservationFilter extends RequestFilter {

  private final Stage stage;

  public static RequestPreservationFilter with(final Stage stage) {
    return new RequestPreservationFilter(stage);
  }

  private RequestPreservationFilter(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public Tuple2<Request, Boolean> filter(final Request request) {
    final String uri = request.uri.toString();
    final String ipAddress = request.headers.headerOf(XForwardedFor).value;
    RequestHistory.preserve(stage, uri, ipAddress);
    return Tuple2.from(request, true);
  }

  @Override
  public void stop() {
  }
}
