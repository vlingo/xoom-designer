// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import io.vlingo.xoom.turbo.ComponentRegistry;

public enum Profile {

  PRODUCTION,
  TEST;

  public static void enableTestProfile() {
    ComponentRegistry.register(Profile.class, Profile.TEST);
  }

  public static void disableTestProfile() {
    ComponentRegistry.unregister(Profile.class);
    ComponentRegistry.register(Profile.class, Profile.PRODUCTION);
  }

  public static boolean isTestProfileEnabled() {
    if(!ComponentRegistry.has(Profile.class)) {
      return false;
    }
    return ComponentRegistry.withType(Profile.class).equals(TEST);
  }

  public static String optionName() {
    return "profile";
  }
}
