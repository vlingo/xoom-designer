// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.vlingo.xoom.starter.template.archetype;

import java.util.Properties;
import java.util.stream.Collectors;

public class ArchetypeMavenOptionsBuilder {

    private static final String MAVEN_OPTION_PATTERN = "-D%s=%s ";
    private static ArchetypeMavenOptionsBuilder instance;

    private ArchetypeMavenOptionsBuilder() {}

    public static ArchetypeMavenOptionsBuilder instance() {
        if(instance == null) {
            instance = new ArchetypeMavenOptionsBuilder();
        }
        return instance;
    }

    public String build(final Archetype archetype, final Properties properties) {
        return buildArchetypeMetadata(archetype) + buildArchetypeOptions(properties, archetype);
    }

    private String buildArchetypeMetadata(final Archetype archetype) {
        return String.format(MAVEN_OPTION_PATTERN, "archetypeGroupId", archetype.groupId()) +
                String.format(MAVEN_OPTION_PATTERN, "archetypeArtifactId", archetype.artifactId()) +
                String.format(MAVEN_OPTION_PATTERN, "archetypeVersion", archetype.version());
    }

    private String buildArchetypeOptions(final Properties properties, final Archetype archetype) {
        return archetype.supportedMavenProperties().stream().map(archetypeProperties -> {
            final String optionValue = properties.get(archetypeProperties.literal()).toString();
            return String.format(MAVEN_OPTION_PATTERN, archetypeProperties.toMavenFormat(), optionValue);
        }).collect(Collectors.joining());
    }

}
