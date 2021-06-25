// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.task.TaskExecutionContext;

import java.io.File;

import static io.vlingo.xoom.designer.infrastructure.restapi.report.ProjectGenerationReport.onFail;
import static io.vlingo.xoom.designer.infrastructure.restapi.report.ProjectGenerationReport.onSuccess;
import static io.vlingo.xoom.designer.task.TaskOutput.PROJECT_GENERATION_REPORT;

public abstract class ProjectGenerationManager {

  public Completes<TaskExecutionContext> generate(final GenerationSettingsData settings,
                                                  final ProjectGenerationInformation information) {
    return new WebBasedProjectGenerationManager().manage(settings, information);
  }

  public void createGenerationPath(final File generationPath) {
    try {
      if (generationPath.exists() && generationPath.isDirectory() && generationPath.list().length > 0) {
        throw new GenerationPathAlreadyExistsException();
      }
      generationPath.mkdirs();
    } catch (final Exception e) {
      throw new GenerationPathCreationException();
    }
  }

  protected void processSteps(final TaskExecutionContext context, final ProjectGenerationInformation information) {
    try {
      Configuration.PROJECT_GENERATION_STEPS.stream()
              .filter(step -> step.shouldProcess(context))
              .forEach(step -> step.process(context));

      context.addOutput(PROJECT_GENERATION_REPORT, onSuccess(context, information));
    } catch (final Exception exception) {
      exception.printStackTrace();
      context.addOutput(PROJECT_GENERATION_REPORT, onFail(context, information, exception));
    }
  }

}
