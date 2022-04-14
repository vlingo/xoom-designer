// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.requesthistory;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestLimiter {

  private final int limit;
  private final Duration countExpiration;
  private final Map<String, RequestCount> counters = new HashMap<>();

  public static RequestLimiter of(final int limit, final Duration expiration) {
    if(expiration.compareTo(Duration.ofSeconds(60)) > 0) {
      throw new IllegalArgumentException("Expiration interval cannot be longer than 60 seconds.");
    }
    return new RequestLimiter(limit, expiration);
  }

  private RequestLimiter(final int limit,
                         final Duration countExpiration) {
    this.limit = limit;
    this.countExpiration = countExpiration;
    RequestCountReset.scheduleFor(this, countExpiration);
  }

  public boolean checkLimit(final String ipAddress) {
    final RequestCount requestCount = retrieveCount(ipAddress);
    if(requestCount.isNew()) {
      counters.put(ipAddress, requestCount);
    }else if(requestCount.isExpired()) {
      requestCount.reset();
    }
    return requestCount.increment();
  }

  private RequestCount retrieveCount(final String ipAddress) {
    return counters.getOrDefault(ipAddress, new RequestCount(limit, countExpiration));
  }

  protected void removeAllExpired() {
    final List<String> ips =
            counters.entrySet().stream()
                    .filter(entry -> entry.getValue().isExpired())
                    .map(entry -> entry.getKey()).collect(Collectors.toList());

    ips.forEach(ip -> counters.remove(ip));
  }

}
