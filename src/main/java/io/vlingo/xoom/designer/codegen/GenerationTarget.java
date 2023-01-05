// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.designer.infrastructure.StagingFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public enum GenerationTarget {
  FILESYSTEM("filesystem", false),
  ZIP("zip-download", true);

  private final String value;
  private final boolean supportDownload;

  GenerationTarget(final String value, final boolean supportDownload) {
    this.value = value;
    this.supportDownload = supportDownload;
  }

  public static GenerationTarget from(final String targetOption) {
    if(targetOption.trim().isEmpty()) {
      return FILESYSTEM;
    }
    return Stream.of(values()).filter(gt -> gt.value.equalsIgnoreCase(targetOption)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(targetOption + " is a invalid value for Generation Target."));
  }

  public String value() {
    return value;
  }

  public boolean supportDownload() {
    return supportDownload;
  }

  public boolean requiresLocalInstallation() {
    return !supportDownload;
  }

  public Path definitiveFolderFor(final String executionId, final String artifactName, final String suggestedFolder) {
    if(supportDownload) {
      return temporaryFolderFor(executionId, artifactName);
    }
    return Paths.get(suggestedFolder);
  }

  public Path temporaryFolderFor(final String executionId, final String artifactName) {
    return StagingFolder.path().resolve(executionId).resolve(artifactName);
  }

}
