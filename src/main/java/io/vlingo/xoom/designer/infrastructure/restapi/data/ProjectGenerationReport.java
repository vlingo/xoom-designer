// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskStatus;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;

import static io.vlingo.xoom.designer.task.Output.COMPRESSED_PROJECT;

public class ProjectGenerationReport {

  public final String status;
  public final String target;
  public final String compressedProject; //Base 64

  public static ProjectGenerationReport from(final TaskExecutionContext context,
                                             final ProjectGenerationInformation information) {
    final TaskStatus taskStatus = context.status();
    final String target = information.generationTarget;
    final String compressedProject = context.retrieveOutput(COMPRESSED_PROJECT);
    return new ProjectGenerationReport(taskStatus.name(), target, compressedProject);
  }

  private ProjectGenerationReport(final String status,
                                  final String target,
                                  final String compressedProject) {
    this.status = status;
    this.target = target;
    this.compressedProject = compressedProject;
  }
}
