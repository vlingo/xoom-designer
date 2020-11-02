// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Property;

import java.util.Arrays;
import java.util.List;

public class ContextSettingsData {

    public final String groupId;
    public final String artifactId;
    public final String artifactVersion;
    public final String packageName;
    public final String xoomVersion;

    public ContextSettingsData(final String groupId,
                               final String artifactId,
                               final String artifactVersion,
                               final String packageName,
                               final String xoomVersion) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
        this.packageName = packageName;
        this.xoomVersion = xoomVersion;
    }

}
