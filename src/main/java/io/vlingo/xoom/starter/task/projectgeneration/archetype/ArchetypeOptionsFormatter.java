// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.vlingo.xoom.starter.task.projectgeneration.archetype;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.Label;

import java.util.stream.Collectors;

public class ArchetypeOptionsFormatter {

    private static ArchetypeOptionsFormatter instance;
    private static final String MAVEN_OPTION_PATTERN = "-D%s=%s ";

    private ArchetypeOptionsFormatter() {}

    public static ArchetypeOptionsFormatter instance() {
        if(instance == null) {
            instance = new ArchetypeOptionsFormatter();
        }
        return instance;
    }

    public String format(final Archetype archetype, final CodeGenerationParameters parameters) {
        return buildArchetypeMetadata(archetype) + buildArchetypeOptions(parameters, archetype);
    }

    private String buildArchetypeMetadata(final Archetype archetype) {
        return String.format(MAVEN_OPTION_PATTERN, "archetypeGroupId", archetype.groupId()) +
                String.format(MAVEN_OPTION_PATTERN, "archetypeArtifactId", archetype.artifactId()) +
                String.format(MAVEN_OPTION_PATTERN, "archetypeVersion", archetype.version());
    }

    private String buildArchetypeOptions(final CodeGenerationParameters parameters, final Archetype archetype) {
        return archetype.archetypeOptions().stream().map(option -> {
            final Label parameterLabel = Label.valueOf(option.name());
            final String optionValue = parameters.retrieveValue(parameterLabel);
            return String.format(MAVEN_OPTION_PATTERN, option.key, optionValue);
        }).collect(Collectors.joining());
    }

}
