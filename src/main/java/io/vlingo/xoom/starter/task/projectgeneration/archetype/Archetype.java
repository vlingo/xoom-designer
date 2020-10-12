// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.archetype;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.starter.Resource;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.task.projectgeneration.archetype.ArchetypeOption.*;

public enum Archetype {

    KUBERNETES("kubernetes-archetype", "vlingo-xoom-kubernetes-archetype",
            "io.vlingo", "1.0", VERSION, GROUP_ID, ARTIFACT_ID, MAIN_CLASS,
            PACKAGE, XOOM_SERVER_VERSION, DOCKER_IMAGE, KUBERNETES_POD_NAME,
            KUBERNETES_IMAGE);

    private final String label;
    private final String artifactId;
    private final String groupId;
    private final String version;

    private final List<ArchetypeOption> archetypeOptions = new ArrayList<>();

    Archetype(final String label,
              final String artifactId,
              final String groupId,
              final String version,
              final ArchetypeOption... requiredOptions) {
        this.label = label;
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
        this.archetypeOptions.addAll(Arrays.asList(requiredOptions));
    }

    public static Archetype findDefault() {
        //TODO: Optimize by creating lighter archetype that fits exactly a set of project generation parameters
        return KUBERNETES;
    }

    public String formatOptions(final CodeGenerationParameters parameters) {
        return ArchetypeOptionsFormatter.instance().format(this, parameters);
    }

    public String folder() {
        return Paths.get(Resource.ARCHETYPES_FOLDER.path(), label).toString();
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

    List<ArchetypeOption> archetypeOptions() {
        return archetypeOptions;
    }

}