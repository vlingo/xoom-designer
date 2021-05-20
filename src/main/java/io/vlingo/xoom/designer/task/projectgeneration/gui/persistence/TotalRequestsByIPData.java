// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui.persistence;

import java.time.LocalDateTime;

public class TotalRequestsByIPData {

  public final String ipAddress;
  public final long totalRequests;
  public final LocalDateTime lastOccurredOn;

  public static TotalRequestsByIPData from(final String ipAddress,
                                           final LocalDateTime lastOccurredOn) {
    return new TotalRequestsByIPData(ipAddress, 1, lastOccurredOn);
  }


  public static TotalRequestsByIPData from(final String ipAddress,
                                           final long totalRequests,
                                           final LocalDateTime lastOccurredOn) {
    return new TotalRequestsByIPData(ipAddress, totalRequests, lastOccurredOn);
  }

  private TotalRequestsByIPData(final String ipAddress,
                                final long totalRequests,
                                final LocalDateTime lastOccurredOn) {
    this.ipAddress = ipAddress;
    this.totalRequests = totalRequests;
    this.lastOccurredOn = lastOccurredOn;
  }

}
