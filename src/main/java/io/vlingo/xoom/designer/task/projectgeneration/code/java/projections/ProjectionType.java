// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.projections;

public enum ProjectionType {

  NONE("None"),
  CUSTOM("Custom"),
  EVENT_BASED("Event"),
  OPERATION_BASED("Operation");

  public final String sourceName;

  ProjectionType(final String sourceName) {
    this.sourceName = sourceName;
  }

  public boolean isEventBased() {
    return equals(EVENT_BASED);
  }

  public boolean isOperationBased() {
    return equals(OPERATION_BASED);
  }

  public boolean isProjectionEnabled() {
    return !equals(NONE);
  }

}
