// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.infrastructure.Infrastructure;

public class TargetFolderResolver {

  public static String resolve(final TargetFolderType type,
                               final String artifactId,
                               final String suggestedFolder) {
    if(type.isTemporary() || ProjectGenerationInformation.load().requiresCompression()) {
      return resolve(type, artifactId);
    }
    return suggestedFolder;
  }

  public static String resolve(final TargetFolderType type, final String artifactId) {
    if(type.isDefinitive() && !ProjectGenerationInformation.load().requiresCompression()) {
      throw new IllegalArgumentException("Please inform the suggested folder to resolve the target folder");
    }
    return Infrastructure.ArchetypesFolder.path().resolve(artifactId).toString();
  }

  public enum TargetFolderType {
    TEMPORARY,
    DEFINITIVE;

    boolean isTemporary() {
      return equals(TEMPORARY);
    }

    boolean isDefinitive() {
      return equals(DEFINITIVE);
    }
  }
}
