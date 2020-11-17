// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

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

    public List<String> validate(List<String> errorStrings) {
        if(groupId==null) errorStrings.add("groupId is null");
        if(artifactId==null) errorStrings.add("artifactId is null");
        if(artifactVersion==null) errorStrings.add("artifactVersion is null");
        if(packageName==null) errorStrings.add("packageName is null");
        if(xoomVersion==null) errorStrings.add("xoomVersion is null");
        return errorStrings;
    }

}
