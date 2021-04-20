// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutionException;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectCompressor;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import java.io.IOException;

import static io.vlingo.xoom.designer.task.Output.COMPRESSED_PROJECT;

public class ProjectCompressionStep implements TaskExecutionStep {

  private final ProjectGenerationInformation projectGenerationInformation;

  public ProjectCompressionStep(final ProjectGenerationInformation projectGenerationInformation) {
    this.projectGenerationInformation = projectGenerationInformation;
  }

  @Override
  public void process(final TaskExecutionContext context) {
    try {
      context.addOutput(COMPRESSED_PROJECT, ProjectCompressor.compress(context.temporaryTargetFolder()));
    } catch (final IOException e) {
      throw new TaskExecutionException(e);
    }
  }

  @Override
  public boolean shouldProcess(final TaskExecutionContext context) {
    return projectGenerationInformation.requiresCompression();
  }
}
