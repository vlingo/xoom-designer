// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

public enum Profile {

  PRODUCTION,
  TEST;

  private static Profile active = PRODUCTION;

  public static void enableTestProfile() {
    active = TEST;
  }

  public static void disableTestProfile() {
    active = PRODUCTION;
  }

  public static boolean isTestProfileEnabled() {
    return active.equals(TEST);
  }

}
