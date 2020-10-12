// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.archetype;

public enum ArchetypeOption {

    VERSION("version"),
    GROUP_ID("groupId"),
    ARTIFACT_ID("artifactId"),
    PACKAGE("package"),
    MAIN_CLASS("mainClass"),
    XOOM_SERVER_VERSION("vlingoXoomServerVersion"),
    DOCKER_IMAGE("dockerImage"),
    KUBERNETES_IMAGE("k8sImage"),
    KUBERNETES_POD_NAME("k8sPodName");

    public final String key;

    ArchetypeOption(final String key) {
        this.key = key;
    }

}
