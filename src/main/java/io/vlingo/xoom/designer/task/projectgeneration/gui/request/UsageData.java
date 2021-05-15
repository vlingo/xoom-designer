// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui.request;

import io.vlingo.xoom.http.Request;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.vlingo.xoom.http.RequestHeader.XForwardedFor;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class UsageData {

  public final String id;
  public final String uri;
  public final String ipAddress;
  public final String timestamp;

  public static UsageData from(final Request request) {
    return new UsageData(request.uri.toString(), request.headers.headerOf(XForwardedFor).value);
  }

  private UsageData(final String ipAddress, final String uri) {
    this.id = UUID.randomUUID().toString();
    this.timestamp = LocalDateTime.now(UTC).format(ISO_DATE_TIME);
    this.ipAddress = ipAddress;
    this.uri = uri;
  }
}
