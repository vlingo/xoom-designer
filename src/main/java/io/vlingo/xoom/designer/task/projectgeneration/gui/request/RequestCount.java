// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.gui.request;

import java.time.Duration;
import java.time.LocalDateTime;

class RequestCount {

  private int counter;
  private final int limit;
  private final Duration requestCountExpiration;
  private LocalDateTime expiredOn;

  RequestCount(final int limit, final Duration requestCountExpiration) {
    this.counter = 0;
    this.limit = limit;
    this.requestCountExpiration = requestCountExpiration;
    this.expiredOn = calculateExpirationTime();
  }

  public boolean increment() {
    if(counter == limit) {
      return false;
    }
    counter++;
    return true;
  }

  public void reset() {
    counter = 0;
    expiredOn = calculateExpirationTime();
  }

  public boolean isNew() {
    return counter == 0;
  }

  public boolean isExpired() {
    return expiredOn.isBefore(LocalDateTime.now());
  }

  private LocalDateTime calculateExpirationTime() {
    return LocalDateTime.now().plusSeconds(requestCountExpiration.getSeconds());
  }
}
