// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.ArchetypeOption.*;

public enum Archetype {

  KUBERNETES("kubernetes-archetype", "xoom-turbo-kubernetes-archetype",
          "io.vlingo.xoom", "1.0", VERSION, GROUP_ID, ARTIFACT_ID, MAIN_CLASS,
          PACKAGE, XOOM_VERSION, DOCKER_IMAGE, KUBERNETES_POD_NAME,
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
    //TODO: Optimize by creating lighter archetype according to the set of generation parameters
    return KUBERNETES;
  }

  public String resolvePomPath() {
    final Terminal terminal = Terminal.supported();
    final String relativePathPrefix = terminal.isWindows() ? "" : "." + terminal.pathSeparator();
    return String.format("%s%s%spom.xml", relativePathPrefix, label, terminal.pathSeparator());
  }

  public String formatOptions(final CodeGenerationParameters parameters) {
    return ArchetypeOptionsFormatter.instance().format(this, parameters);
  }

  public String label() {
    return label;
  }

  public Path jarPath(final Path archetypesFolderPath) {
    return archetypesFolderPath.resolve(label).resolve("target")
            .resolve(String.format("%s-%s.jar", artifactId, version));
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