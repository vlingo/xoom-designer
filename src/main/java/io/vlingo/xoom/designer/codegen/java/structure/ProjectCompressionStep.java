// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.structure;

import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionException;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.codegen.ProjectCompressor;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.IOException;

import static io.vlingo.xoom.designer.cli.TaskOutput.COMPRESSED_PROJECT;

public class ProjectCompressionStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
    try {
      context.addOutput(COMPRESSED_PROJECT, ProjectCompressor.compress(context.targetFolder()));
    } catch (final IOException e) {
      throw new TaskExecutionException(e);
    }
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return generationTarget().supportDownload();
  }

  private GenerationTarget generationTarget() {
    return ComponentRegistry.withType(GenerationTarget.class);
  }
}
