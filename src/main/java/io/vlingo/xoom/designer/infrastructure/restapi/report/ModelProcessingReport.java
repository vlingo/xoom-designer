// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.ModelProcessingInformation;
import io.vlingo.xoom.designer.ModelProcessingStatus;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.infrastructure.ProjectCompressor;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;

import java.io.IOException;

public class ModelProcessingReport {

  public final static String CODEGEN_FAILURE = "CODEGEN_FAILURE";
  public final static String VALIDATION_FAILURE = "VALIDATION_FAILURE";
  public final static String SCHEMA_PULL_FAILURE = "SCHEMA_PULL_FAILURE";
  public final static String SCHEMA_PUSH_FAILURE = "SCHEMA_PUSH_FAILURE";
  public final static String CONTEXT_MAPPING_FAILURE = "CONTEXT_MAPPING_FAILURE";

  public final String target;
  public final String errorType;
  public final ModelProcessingStatus status;
  public final String details;
  public final String compressedProject; //Base 64

  public static ModelProcessingReport onCodeGenerationSucceed(final CodeGenerationContext context,
                                                              final ModelProcessingInformation information) throws IOException {
    final String targetFolder = context.parameterOf(Label.TARGET_FOLDER);
    final String compressedProject = ProjectCompressor.compress(targetFolder);
    return new ModelProcessingReport(information.generationTarget, compressedProject);
  }

  public static ModelProcessingReport onCodeGenerationFail(final CodeGenerationContext context,
                                                           final ModelProcessingInformation information,
                                                           final Exception exception) {
    final GenerationTarget target = information.generationTarget;

    final String designerModel =
            context.parameterOf(Label.DESIGNER_MODEL_JSON);

    final String errorDetails =
            ModelProcessingReportDetails.format(target.value(), CODEGEN_FAILURE, designerModel, exception);

    return new ModelProcessingReport(ModelProcessingStatus.FAILED, target, null, CODEGEN_FAILURE, errorDetails);
  }

  public static ModelProcessingReport onSchemaPullFail(final ModelProcessingInformation information) {
    return new ModelProcessingReport(information.generationTarget, SCHEMA_PULL_FAILURE,  "");
  }

  public static ModelProcessingReport onSchemaPushFail(final ModelProcessingInformation information) {
    return new ModelProcessingReport(information.generationTarget, SCHEMA_PUSH_FAILURE,  "");
  }

  public static ModelProcessingReport onContextMappingFail(final GenerationTarget target,
                                                           final DesignerModel model,
                                                           final Exception exception) {
    final String designerModel =
            JsonSerialization.serialized(model);

    final String errorDetails =
            ModelProcessingReportDetails.format(target.value(), CONTEXT_MAPPING_FAILURE, designerModel, exception);

    return new ModelProcessingReport(target, CONTEXT_MAPPING_FAILURE, errorDetails);
  }

  public static ModelProcessingReport onValidationFail(final String validationErrors,
                                                       final GenerationTarget target) {
    return new ModelProcessingReport(target, VALIDATION_FAILURE, validationErrors);
  }

  private ModelProcessingReport(final GenerationTarget target,
                                final String compressedProject) {
    this(ModelProcessingStatus.SUCCESSFUL, target, compressedProject, null, null);
  }

  private ModelProcessingReport(final GenerationTarget target, final String errorType, final String details) {
    this(ModelProcessingStatus.FAILED, target, null, errorType, details);
  }

  private ModelProcessingReport(final ModelProcessingStatus status,
                                final GenerationTarget target,
                                final String compressedProject,
                                final String errorType,
                                final String details) {
    this.status = status;
    this.target = target.value();
    this.compressedProject = compressedProject;
    this.errorType = errorType;
    this.details = details;
  }

}
