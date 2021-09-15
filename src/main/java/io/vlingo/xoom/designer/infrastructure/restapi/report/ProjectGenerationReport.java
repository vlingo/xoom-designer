// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskStatus;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.ProjectGenerationInformation;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;

import static io.vlingo.xoom.designer.cli.TaskOutput.COMPRESSED_PROJECT;

public class ProjectGenerationReport {

  public final static String CODEGEN_FAILURE = "CODEGEN_FAILURE";
  public final static String VALIDATION_FAILURE = "VALIDATION_FAILURE";
  public final static String SCHEMA_PULL_FAILURE = "SCHEMA_PULL_FAILURE";
  public final static String SCHEMA_PUSH_FAILURE = "SCHEMA_PUSH_FAILURE";
  public final static String CONTEXT_MAPPING_FAILURE = "CONTEXT_MAPPING_FAILURE";

  public final String target;
  public final String errorType;
  public final TaskStatus status;
  public final String details;
  public final String compressedProject; //Base 64

  public static ProjectGenerationReport onCodeGenerationSucceed(final TaskExecutionContext context,
                                                                final ProjectGenerationInformation information) {
    final String compressedProject = context.retrieveOutput(COMPRESSED_PROJECT);
    return new ProjectGenerationReport(information.generationTarget, compressedProject);
  }

  public static ProjectGenerationReport onCodeGenerationFail(final TaskExecutionContext context,
                                                             final ProjectGenerationInformation information,
                                                             final Exception exception) {
    final GenerationTarget target = information.generationTarget;

    final String designerModel =
            context.codeGenerationParameterOf(Label.DESIGNER_MODEL_JSON);

    final String errorDetails =
            ProjectGenerationReportDetails.format(target.key(), CODEGEN_FAILURE, designerModel, exception);

    return new ProjectGenerationReport(TaskStatus.FAILED, target, null, CODEGEN_FAILURE, errorDetails);
  }

  public static ProjectGenerationReport onSchemaPullFail(final ProjectGenerationInformation information) {
    return new ProjectGenerationReport(information.generationTarget, SCHEMA_PULL_FAILURE,  "");
  }

  public static ProjectGenerationReport onSchemaPushFail(final ProjectGenerationInformation information) {
    return new ProjectGenerationReport(information.generationTarget, SCHEMA_PUSH_FAILURE,  "");
  }

  public static ProjectGenerationReport onContextMappingFail(final GenerationTarget target,
                                                             final GenerationSettingsData settings,
                                                             final Exception exception) {
    final String designerModel =
            JsonSerialization.serialized(settings);

    final String errorDetails =
            ProjectGenerationReportDetails.format(target.key(), CONTEXT_MAPPING_FAILURE, designerModel, exception);

    return new ProjectGenerationReport(target, CONTEXT_MAPPING_FAILURE, errorDetails);
  }

  public static ProjectGenerationReport onValidationFail(final String validationErrors, final GenerationTarget target) {
    return new ProjectGenerationReport(target, VALIDATION_FAILURE, validationErrors);
  }

  private ProjectGenerationReport(final GenerationTarget target,
                                  final String compressedProject) {
    this(TaskStatus.SUCCESSFUL, target, compressedProject, null, null);
  }

  private ProjectGenerationReport(final GenerationTarget target, final String errorType, final String details) {
    this(TaskStatus.FAILED, target, null, errorType, details);
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

}
