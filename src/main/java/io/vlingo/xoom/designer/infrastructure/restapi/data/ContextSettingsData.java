// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.List;

public class ContextSettingsData {

  public final String groupId;
  public final String artifactId;
  public final String artifactVersion;
  public final String packageName;

  public final String solutionName;
  public final String projectName;
  public final String projectVersion;
  public final String namespace;
  public ContextSettingsData(final String groupId,
                             final String artifactId,
                             final String artifactVersion,
                             final String packageName) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.artifactVersion = artifactVersion;
    this.packageName = packageName;

    this.solutionName = groupId;
    this.projectName = artifactId;
    this.projectVersion = artifactVersion;
    this.namespace = packageName;
  }

  public List<String> validate(List<String> errorStrings) {
    if (groupId == null) errorStrings.add("ContextSettingsData.groupId is null");
    if (artifactId == null) errorStrings.add("ContextSettingsData.artifactId is null");
    if (artifactVersion == null) errorStrings.add("ContextSettingsData.artifactVersion is null");
    if (packageName == null) errorStrings.add("ContextSettingsData.packageName is null");
    return errorStrings;
  }

}
