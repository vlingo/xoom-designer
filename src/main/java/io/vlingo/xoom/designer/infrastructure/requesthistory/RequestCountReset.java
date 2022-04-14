// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.requesthistory;

import io.vlingo.xoom.common.Scheduled;
import io.vlingo.xoom.common.Scheduler;

import java.time.Duration;

import static java.time.Duration.ZERO;

class RequestCountReset implements Scheduled<RequestLimiter> {

  static void scheduleFor(final RequestLimiter limiter, final Duration expiration) {
    new Scheduler().schedule(new RequestCountReset(), limiter, ZERO, expiration);
  }

  @Override
  public void intervalSignal(final Scheduled<RequestLimiter> scheduled, final RequestLimiter requestLimiter) {
    requestLimiter.removeAllExpired();
  }
}
