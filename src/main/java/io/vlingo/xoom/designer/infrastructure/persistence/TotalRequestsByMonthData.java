// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.persistence;

public class TotalRequestsByMonthData {

  public final String monthOfYear;
  public final long totalRequests;

  public static TotalRequestsByMonthData from(final String monthOfYear) {
    return new TotalRequestsByMonthData(monthOfYear, 1);
  }

  public static TotalRequestsByMonthData from(final String monthOfYear, final long totalRequests) {
    return new TotalRequestsByMonthData(monthOfYear, totalRequests);
  }

  private TotalRequestsByMonthData(final String monthOfYear,
                                   final long totalRequests) {
    this.monthOfYear = monthOfYear;
    this.totalRequests = totalRequests;
  }
}
