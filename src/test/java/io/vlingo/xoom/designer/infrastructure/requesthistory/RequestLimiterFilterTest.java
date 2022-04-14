// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.http.Header.Headers;
import io.vlingo.xoom.http.Request;
import io.vlingo.xoom.http.RequestHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;

import static io.vlingo.xoom.designer.infrastructure.restapi.ModelProcessingResource.REFUSE_REQUEST_URI;
import static io.vlingo.xoom.http.Body.Empty;
import static io.vlingo.xoom.http.Method.POST;
import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;
import static io.vlingo.xoom.http.Version.Http1_1;

@SuppressWarnings("unchecked")
public class RequestLimiterFilterTest {

  @Test
  @SuppressWarnings("rawtypes")
  public void testThatExceededRequestsAreFiltered() {
    final Headers requestHeader =
            Headers.of(RequestHeader.of(XForwardedFor, "127.0.0.1"));

    final Request request =
            Request.from(POST, URI.create("/resources"), Http1_1, requestHeader, Empty);

    final RequestLimiter requestLimiter =
            RequestLimiter.of(2, Duration.ofSeconds(30));

    final RequestLimiterFilter filter =
            RequestLimiterFilter.with(requestLimiter);

    final Tuple2<Request, Boolean> firstFiltering = filter.filter(request);
    final Tuple2<Request, Boolean> secondFiltering = filter.filter(request);
    final Tuple2<Request, Boolean> thirdFiltering = filter.filter(request);
    final Tuple2<Request, Boolean> fourthFiltering = filter.filter(request);

    Assertions.assertEquals("/resources", firstFiltering._1.uri.toString());
    Assertions.assertEquals("/resources", secondFiltering._1.uri.toString());
    Assertions.assertEquals(REFUSE_REQUEST_URI, thirdFiltering._1.uri.toString());
    Assertions.assertEquals(REFUSE_REQUEST_URI, fourthFiltering._1.uri.toString());
    Assertions.assertEquals(true, firstFiltering._2);
    Assertions.assertEquals(true, secondFiltering._2);
    Assertions.assertEquals(false, thirdFiltering._2);
    Assertions.assertEquals(false, fourthFiltering._2);
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void testThatRequestCountIsReset() throws Exception {
    final Headers requestHeader =
            Headers.of(RequestHeader.of(XForwardedFor, "127.0.0.1"));

    final Request request =
            Request.from(POST, URI.create("/resources"), Http1_1, requestHeader, Empty);

    final RequestLimiter requestLimiter =
            RequestLimiter.of(2, Duration.ofSeconds(1));

    final RequestLimiterFilter filter =
            RequestLimiterFilter.with(requestLimiter);

    final Tuple2<Request, Boolean> firstFiltering = filter.filter(request);
    final Tuple2<Request, Boolean> secondFiltering = filter.filter(request);
    Thread.sleep(1001);
    final Tuple2<Request, Boolean> thirdFiltering = filter.filter(request);
    final Tuple2<Request, Boolean> fourthFiltering = filter.filter(request);

    Assertions.assertEquals("/resources", firstFiltering._1.uri.toString());
    Assertions.assertEquals("/resources", secondFiltering._1.uri.toString());
    Assertions.assertEquals("/resources", thirdFiltering._1.uri.toString());
    Assertions.assertEquals("/resources", fourthFiltering._1.uri.toString());
    Assertions.assertEquals(true, firstFiltering._2);
    Assertions.assertEquals(true, secondFiltering._2);
    Assertions.assertEquals(true, thirdFiltering._2);
    Assertions.assertEquals(true, fourthFiltering._2);
  }
}
