// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

public class SchemataSettingsData {

  public final String host;
  public final int port;

  public SchemataSettingsData(final String host, final int port) {
    this.host = host;
    this.port = port;
  }

}
