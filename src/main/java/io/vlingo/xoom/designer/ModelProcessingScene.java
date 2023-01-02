// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;
import io.vlingo.xoom.designer.infrastructure.restapi.report.ModelProcessingReport;

import java.io.IOException;

public class ModelProcessingScene {

  public final DesignerModel designerModel;
  public final ModelProcessingInformation information;
  public final CodeGenerationContext codeGenerationContext;
  public final ModelProcessingReport report;

  public static ModelProcessingScene with(final DesignerModel designerModel,
                                          final ModelProcessingInformation information) {
    return new ModelProcessingScene(designerModel, information, null, null);
  }

  private ModelProcessingScene(final DesignerModel model,
                               final ModelProcessingInformation information,
                               final CodeGenerationContext context,
                               final ModelProcessingReport report) {
    this.designerModel = model;
    this.codeGenerationContext = context;
    this.information = information;
    this.report = report;
  }

  public ModelProcessingScene onContextMappingFail(final Exception exception) {
    final ModelProcessingReport report = ModelProcessingReport.onContextMappingFail(this.information.generationTarget, designerModel, exception);
    return new ModelProcessingScene(this.designerModel, this.information, null, report);
  }

  public ModelProcessingScene onValidationFail(final String validationErrors) {
    final ModelProcessingReport report = ModelProcessingReport.onValidationFail(validationErrors, this.information.generationTarget);
    return new ModelProcessingScene(this.designerModel, this.information, null, report);
  }

  public ModelProcessingScene addCodeGenerationContext(final CodeGenerationContext codeGenerationContext) {
    return new ModelProcessingScene(this.designerModel, this.information, codeGenerationContext, null);
  }

  public ModelProcessingScene onCodeGenerationSucceed() throws IOException {
    final ModelProcessingReport report = ModelProcessingReport.onCodeGenerationSucceed(codeGenerationContext, this.information);
    return new ModelProcessingScene(this.designerModel, this.information, this.codeGenerationContext, report);
  }

  public ModelProcessingScene onSchemaPullFail() {
    final ModelProcessingReport report = ModelProcessingReport.onSchemaPullFail(this.information);
    return new ModelProcessingScene(this.designerModel, this.information, this.codeGenerationContext, report);
  }

  public ModelProcessingScene onSchemaPushFail() {
    final ModelProcessingReport report = ModelProcessingReport.onSchemaPushFail(this.information);
    return new ModelProcessingScene(this.designerModel, this.information, this.codeGenerationContext, report);
  }

  public ModelProcessingScene onCodeGenerationFail(final Exception exception) {
    final ModelProcessingReport report = ModelProcessingReport.onCodeGenerationFail(this.codeGenerationContext, this.information, exception);
    return new ModelProcessingScene(this.designerModel, this.information, this.codeGenerationContext, report);
  }

  public boolean isFailed() {
    return report == null ? false : report.status.failed();
  }
}
