// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.steps;

import io.vlingo.xoom.designer.task.Output;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskExecutionException;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectCompressor;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import java.io.IOException;

public class ProjectCompressionStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
    try {
      final byte[] compressed = ProjectCompressor.compress(context.temporaryProjectPath());
      context.addOutput(Output.COMPRESSED_PROJECT, compressed);
    } catch (IOException e) {
      throw new TaskExecutionException(e);
    }
  }

}
