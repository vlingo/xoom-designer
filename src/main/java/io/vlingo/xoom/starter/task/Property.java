// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

public enum Property {

    VERSION("version"),
    GROUP_ID("group.id"),
    ARTIFACT_ID("artifact.id"),
    PACKAGE("package"),
    TARGET_FOLDER("target.folder"),
    XOOM_VERSION("vlingo.xoom.server.version"),
    DEPLOYMENT("deployment"),
    DOCKER_IMAGE("docker.image"),
    DOCKER_REPOSITORY("docker.repository"),
    GLOO_UPSTREAM("gloo.upstream"),
    KUBERNETES_IMAGE("k8s.image"),
    KUBERNETES_POD_NAME("k8s.pod.name"),
    REST_RESOURCES("rest.resources"),
    AGGREGATES("aggregates"),
    DOMAIN_EVENTS("events"),
    STORAGE_TYPE("storage.type"),
    CQRS("cqrs"),
    ANNOTATIONS("annotations"),
    AUTO_DISPATCH("auto.dispatch"),
    PROJECTIONS("projections"),
    DATABASE("database"),
    COMMAND_MODEL_DATABASE("command.model.database"),
    QUERY_MODEL_DATABASE("query.model.database"),
    CLUSTER_NODES("cluster.nodes");

    private final String key;

    Property(final String key) {
        this.key = key;
    }

    public String literal() {
        return key;
    }

}
