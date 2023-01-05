// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.requesthistory;

public class RequestHistoryPreserved {

  public static String name() {
    return RequestHistoryPreserved.class.getSimpleName();
  }

  public static boolean matchName(final String name) {
    return name.equals(name());
  }
}
