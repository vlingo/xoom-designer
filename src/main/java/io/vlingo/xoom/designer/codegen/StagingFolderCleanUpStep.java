// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.designer.ModelProcessingException;
import io.vlingo.xoom.designer.infrastructure.StagingFolder;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.vlingo.xoom.designer.Configuration.MAVEN_WRAPPER_DIRECTORY;

public class StagingFolderCleanUpStep implements CodeGenerationStep {

  private final Phase phase;

  public StagingFolderCleanUpStep(final Phase phase) {
    this.phase = phase;
  }

  @Override
  public void process(final CodeGenerationContext context) {
    try {
      Files.list(StagingFolder.path()).filter(Files::isDirectory)
              .filter(dir -> !dir.getFileName().toString().equals(MAVEN_WRAPPER_DIRECTORY))
              .forEach(this::removeDirectory);
    } catch (final IOException e) {
      throw new ModelProcessingException(e);
    }
  }

  private void removeDirectory(final Path directory) {
    try {
      FileUtils.deleteDirectory(directory.toFile());
    } catch (final IOException e) {
      throw new ModelProcessingException(e);
    }
  }

  @Override
  public boolean shouldProcess(final CodeGenerationContext context) {
    final GenerationTarget generationTarget = ComponentRegistry.withType(GenerationTarget.class);
    if(generationTarget.supportDownload()) {
      return phase.equals(Phase.PRE_GENERATION);
    }
    return true;
  }

  public enum Phase {
    PRE_GENERATION,
    POST_GENERATION
  }

}