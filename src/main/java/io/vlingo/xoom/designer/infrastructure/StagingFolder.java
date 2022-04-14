// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.turbo.ComponentRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StagingFolder {
  private static final String ARCHETYPES_PARENT_FOLDER = "staging";
  private final Path path;

  static void resolve(final HomeDirectory homeDirectory) {
    if (!ComponentRegistry.has(StagingFolder.class)) {
      ComponentRegistry.register(StagingFolder.class, new StagingFolder(homeDirectory));
    }
  }

  private StagingFolder(final HomeDirectory homeDirectory) {
    this.path = Paths.get(homeDirectory.path, ARCHETYPES_PARENT_FOLDER);
  }

  public static Path path() {
    if (!ComponentRegistry.has(StagingFolder.class)) {
      throw new IllegalStateException("Unresolved Archetypes Folder");
    }
    return ComponentRegistry.withType(StagingFolder.class).path;
  }
}
