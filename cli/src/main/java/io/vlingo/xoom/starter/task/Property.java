// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

public enum Property {

    VERSION("version"),
    GROUP_ID("group.id"),
    ARTIFACT_ID("artifact.id"),
    PACKAGE("package"),
    TARGET_FOLDER("target.folder", false),
    XOOM_SERVER_VERSION("vlingo.xoom.server.version"),
    DEPLOYMENT("deployment", true, false),
    DOCKER_IMAGE("docker.image", true, true),
    DOCKER_REPOSITORY("docker.repository", true, false),
    KUBERNETES_IMAGE("k8s.image", true, true),
    KUBERNETES_POD_NAME("k8s.pod.name", true, true),
    REST_RESOURCES("rest.resources", true, false),
    AGGREGATES("aggregates", true, false),
    DOMAIN_EVENTS("events", true, false),
    STORAGE_TYPE("storage.type", true, false),
    CQRS("cqrs", true, false),
    PROJECTIONS("projections", true, false),
    DATABASE("database", true, false),
    COMMAND_MODEL_DATABASE("command.model.database", true, false),
    QUERY_MODEL_DATABASE("query.model.database", true, false),
    CLUSTER_NODES("cluster.nodes");

    private final String key;
    private final boolean optional;
    private final boolean supportedByMaven;

    public static final String DEFAULT_VALUE = "not-informed";

    Property(final String key) {
        this(key, true);
    }

    Property(final String key, final boolean supportedByMaven) {
        this(key, false, supportedByMaven);
    }

    Property(final String key, final boolean optional, final boolean supportedByMaven) {
        this.key = key;
        this.optional = optional;
        this.supportedByMaven = supportedByMaven;
    }

    public String literal() {
        return key;
    }

    public boolean isSupportedByMaven() {
        return supportedByMaven;
    }

    public boolean isOptional() {
        return optional;
    }

    public String defaultValue() {
        return DEFAULT_VALUE;
    }

    public String toMavenFormat() {
        return LOWER_UNDERSCORE.to(LOWER_CAMEL, key.replaceAll("\\.", "_"));
    }
}
