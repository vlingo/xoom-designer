// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.ComponentsConfiguration;

public class HomeDirectory {

  public final String path;

  public static HomeDirectory fromEnvironment() {
    return from(ComponentsConfiguration.resolveHomePath());
  }

  public static HomeDirectory from(final String path) {
    return new HomeDirectory(path);
  }

  public HomeDirectory(final String path) {
    this.path = path;
  }

  public boolean isValid() {
    return path != null && path.trim().length() > 1;
  }
}
