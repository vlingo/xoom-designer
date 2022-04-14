// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.http.Request;
import io.vlingo.xoom.http.RequestFilter;

import java.net.URI;

import static io.vlingo.xoom.designer.infrastructure.restapi.ModelProcessingResource.REFUSE_REQUEST_URI;
import static io.vlingo.xoom.http.Body.Empty;
import static io.vlingo.xoom.http.Header.Headers.empty;
import static io.vlingo.xoom.http.Method.GET;
import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;
import static io.vlingo.xoom.http.Version.Http1_1;

public class RequestLimiterFilter extends RequestFilter {

  private final RequestLimiter limiter;
  private final Request escapeRequest;

  public static RequestLimiterFilter with(final RequestLimiter limiter) {
    return new RequestLimiterFilter(limiter);
  }

  private RequestLimiterFilter(final RequestLimiter limiter) {
    this.limiter = limiter;
    this.escapeRequest = Request.from(GET, URI.create(REFUSE_REQUEST_URI), Http1_1, empty(), Empty);
  }

  @Override
  public Tuple2<Request, Boolean> filter(final Request request) {
    final String ipAddress = retrieveIp(request);
    if(limiter.checkLimit(ipAddress)) {
      return Tuple2.from(request, true);
    }
    return Tuple2.from(escapeRequest, false);
  }

  private String retrieveIp(final Request request) {
    return request.headers.headerOf(XForwardedFor).value;
  }

  @Override
  public void stop() {

  }
}
