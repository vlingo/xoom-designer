// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.codegen.GenerationPathAlreadyExistsException;
import io.vlingo.xoom.designer.codegen.GenerationPathCreationException;
import io.vlingo.xoom.designer.infrastructure.restapi.data.CodeGenerationContextMapper;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelProcessingManager {

  private final List<CodeGenerationStep> codeGenerationSteps = new ArrayList<>();

  public ModelProcessingManager(final List<CodeGenerationStep> codeGenerationSteps) {
    this.codeGenerationSteps.addAll(codeGenerationSteps);
  }

  public Completes<ModelProcessingScene> generate(final DesignerModel model,
                                                  final ModelProcessingInformation information,
                                                  final Logger logger) {
    return validate(model, information)
            .andThenTo(scene -> mapContext(scene, logger))
            .andThen(scene -> processSteps(scene));
  }

  private Completes<ModelProcessingScene> validate(final DesignerModel model,
                                                   final ModelProcessingInformation information) {
    final ModelProcessingScene scene = ModelProcessingScene.with(model, information);
    final String validationErrors = String.join(", ", model.validate());
    if(validationErrors.isEmpty()) {
      return Completes.withSuccess(scene);
    }
    return Completes.withFailure(scene.onValidationFail(validationErrors));
  }

  private Completes<ModelProcessingScene> mapContext(final ModelProcessingScene scene,
                                                     final Logger logger) {
    try {
      final CodeGenerationContext codeGenerationContext =
              CodeGenerationContextMapper.map(scene.designerModel,
                      scene.information.generationTarget, logger);

      return Completes.withSuccess(scene.addCodeGenerationContext(codeGenerationContext));
    } catch (final Exception exception) {
      exception.printStackTrace();
      return Completes.withFailure(scene.onContextMappingFail(exception));
    }
  }

  private ModelProcessingScene processSteps(final ModelProcessingScene scene) {
    try {
      codeGenerationSteps.stream()
              .filter(step -> step.shouldProcess(scene.codeGenerationContext))
              .forEach(step -> step.process(scene.codeGenerationContext));

      return scene.onCodeGenerationSucceed();
    } catch (final Exception exception) {
      exception.printStackTrace();
      switch(exception.getClass().getSimpleName()) {
        case "SchemaPullException":
          return scene.onSchemaPullFail();
        case "SchemaPushException":
          return scene.onSchemaPushFail();
        default:
          return scene.onCodeGenerationFail(exception);
      }
    }
  }

  public void createGenerationPath(final File generationPath) {
    try {
      if (generationPath.exists() && generationPath.isDirectory() && generationPath.list().length > 0) {
        throw new GenerationPathAlreadyExistsException();
      }
      generationPath.mkdirs();
    } catch (final Exception e) {
      e.printStackTrace();
      throw new GenerationPathCreationException();
    }
  }

}
