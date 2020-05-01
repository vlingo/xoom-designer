// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.archetype;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Property;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.Property.*;

public enum Archetype {

    BASIC("basic-archetype","vlingo-xoom-basic-archetype",
            "io.vlingo", "1.0", new BasicArchetypePropertiesValidator(),
            VERSION, GROUP_ID, ARTIFACT_ID, PACKAGE, TARGET_FOLDER,
            XOOM_SERVER_VERSION),

    KUBERNETES("kubernetes-archetype", "vlingo-xoom-kubernetes-archetype",
            "io.vlingo", "1.0", new KubernetesArchetypePropertiesValidator(),
            VERSION, GROUP_ID, ARTIFACT_ID, PACKAGE, TARGET_FOLDER, XOOM_SERVER_VERSION,
            DOCKER_IMAGE, DEPLOYMENT, KUBERNETES_POD_NAME, KUBERNETES_IMAGE),

    CLUSTERED_KUBERNETES("kubernetes-archetype", "vlingo-xoom-kubernetes-archetype",
                       "io.vlingo", "1.0", new KubernetesArchetypePropertiesValidator(),
                        VERSION, GROUP_ID, ARTIFACT_ID, PACKAGE, TARGET_FOLDER, XOOM_SERVER_VERSION,
                        DOCKER_IMAGE, DEPLOYMENT, KUBERNETES_POD_NAME, KUBERNETES_IMAGE, CLUSTER_NODES);

    private final String label;
    private final String artifactId;
    private final String groupId;
    private final String version;
    private final ArchetypePropertiesValidator validator;
    private final List<Property> requiredProperties = new ArrayList<>();

    Archetype(final String label,
              final String artifactId,
              final String groupId,
              final String version,
              final ArchetypePropertiesValidator validator,
              final Property... requiredProperties) {
        this.label = label;
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
        this.validator = validator;
        this.requiredProperties.addAll(Arrays.asList(requiredProperties));
    }

    public static Archetype support(final Properties properties) {
        return Arrays.asList(Archetype.values()).stream()
                .filter(archetype -> archetype.isSupported(properties))
                .findFirst().orElseThrow(ArchetypeNotFoundException::new);
    }

    public String fillMavenOptions(final Properties properties) {
        return ArchetypeMavenOptionsBuilder.instance().build(this, properties);
    }

    public String folder() {
        return Paths.get(Resource.ARCHETYPES_FOLDER.path(), label).toString();
    }

    public List<Property> supportedMavenProperties() {
        return this.requiredProperties.stream()
                .filter(properties -> properties.isSupportedByMaven())
                .collect(Collectors.toList());
    }

    private boolean isSupported(final Properties properties) {
        return validator.validate(properties, requiredProperties);
    }

    String artifactId() {
        return artifactId;
    }

    String groupId() {
        return groupId;
    }

    String version() {
        return version;
    }

}