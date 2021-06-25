// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskStatus;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.designermodel.DesignerModelFormatter;

import static io.vlingo.xoom.designer.task.TaskOutput.COMPRESSED_PROJECT;

public class ProjectGenerationReport {

  public final static String CODEGEN_FAILURE = "CODEGEN_FAILURE";
  public final static String VALIDATION_FAILURE = "VALIDATION_FAILURE";
  public final static String CONTEXT_MAPPING_FAILURE = "CONTEXT_MAPPING_FAILURE";

  public final String target;
  public final String errorType;
  public final TaskStatus status;
  public final String details;
  public final String compressedProject; //Base 64

  public static ProjectGenerationReport onSuccess(final TaskExecutionContext context,
                                                  final ProjectGenerationInformation information) {
    final TaskStatus taskStatus = TaskStatus.SUCCESSFUL;
    final String target = information.generationTarget.key();
    final String compressedProject = context.retrieveOutput(COMPRESSED_PROJECT);
    return new ProjectGenerationReport(target, compressedProject);
  }

  public static ProjectGenerationReport onFail(final TaskExecutionContext context,
                                               final ProjectGenerationInformation information,
                                               final Exception exception) {
    final GenerationTarget target = information.generationTarget;

    final String designerModel =
            context.codeGenerationParameterOf(Label.DESIGNER_MODEL_JSON);

    final String errorDetails =
            ProjectGenerationReportDetails.format(target.key(), CODEGEN_FAILURE, designerModel, exception);

    return new ProjectGenerationReport(TaskStatus.FAILED, target, null, CODEGEN_FAILURE, errorDetails);
  }

  public static ProjectGenerationReport onContextMappingFail(final GenerationTarget target,
                                                             final GenerationSettingsData settings,
                                                             final Exception exception) {
    final String designerModel =
            DesignerModelFormatter.format(settings);

    final String errorDetails =
            ProjectGenerationReportDetails.format(target.key(), CONTEXT_MAPPING_FAILURE, designerModel, exception);

    return new ProjectGenerationReport(CONTEXT_MAPPING_FAILURE, errorDetails);
  }

  public static ProjectGenerationReport onValidationFail(final String validationErrors) {
    return new ProjectGenerationReport(VALIDATION_FAILURE, validationErrors);
  }

  private ProjectGenerationReport(final GenerationTarget target,
                                  final String compressedProject) {
    this(TaskStatus.SUCCESSFUL, target, compressedProject, null, null);
  }

  private ProjectGenerationReport(final TaskStatus status,
                                  final GenerationTarget target,
                                  final String compressedProject,
                                  final String errorType,
                                  final String details) {
    this.status = status;
    this.target = target.key();
    this.compressedProject = compressedProject;
    this.errorType = errorType;
    this.details = details;
  }

  private ProjectGenerationReport(final String errorType, final String details) {
    this.status = TaskStatus.FAILED;
    this.errorType = errorType;
    this.details = details;
    this.compressedProject = null;
    this.target = null;
  }

}
