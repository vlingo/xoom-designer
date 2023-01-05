// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

public enum DeploymentType {
  NONE,
  DOCKER,
  KUBERNETES;

  public static DeploymentType of(final String typeName) {
    return DeploymentType.valueOf(typeName.trim().toUpperCase());
  }

  public boolean isDocker() {
    return equals(DOCKER);
  }

  public boolean isKubernetes() {
    return equals(KUBERNETES);
  }
}
