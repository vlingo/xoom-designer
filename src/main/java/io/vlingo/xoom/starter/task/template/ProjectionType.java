// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

public enum ProjectionType {

    NONE("None"),
    EVENT_BASED("Event"),
    OPERATION_BASED("Operation");

    public final String sourceName;

    ProjectionType(final String sourceName) {
        this.sourceName = sourceName;
    }

    public boolean isEventBased() {
        return equals(EVENT_BASED);
    }

    public boolean isProjectionEnabled() {
        return !equals(NONE);
    }
}
