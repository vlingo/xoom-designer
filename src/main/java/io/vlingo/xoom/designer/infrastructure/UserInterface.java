// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ComponentRegistry;

public class UserInterface {
  private static final String USER_INTERFACE_CONTEXT = "context";
  private final String rootContext;

  static void resolve() {
    if (!ComponentRegistry.has(UserInterface.class)) {
      ComponentRegistry.register(UserInterface.class, new UserInterface());
    }
  }

  public UserInterface() {
    rootContext = String.format("%s/%s", DesignerServer.url(), USER_INTERFACE_CONTEXT);
  }

  public static String rootContext() {
    if (!ComponentRegistry.has(UserInterface.class)) {
      throw new IllegalStateException("Unresolved User Interface");
    }
    return ComponentRegistry.withType(UserInterface.class).rootContext;
  }
}
