// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.ModelProcessingInformation;
import io.vlingo.xoom.designer.ModelProcessingManager;
import io.vlingo.xoom.designer.codegen.GenerationPathAlreadyExistsException;
import io.vlingo.xoom.designer.codegen.GenerationPathCreationException;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModelFile;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModelFileException;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.infrastructure.restapi.report.ModelFileHandlingReport;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class ModelProcessingResource extends DynamicResourceHandler {

  private final Logger logger;
  private final GenerationTarget generationTarget;
  private ModelProcessingManager modelProcessingManager;
  private final ModelProcessingInformation modelProcessingInformation;
  public static final String REFUSE_REQUEST_URI = "/api/model-processing/request-refusal";

  public ModelProcessingResource(final Stage stage) {
    super(stage);
    this.logger = stage().world().defaultLogger();
    this.generationTarget = ComponentRegistry.withType(GenerationTarget.class);
    this.modelProcessingInformation = ModelProcessingInformation.from(generationTarget);
  }

  public Completes<Response> startGeneration(final DesignerModel model) {
    final CodeElementFormatter codeElementFormatter =
            CodeElementFormatter.with(Dialect.withName(model.platformSettings.lang.toUpperCase()),
                    ReservedWordsHandler.usingSuffix("_"));

    ComponentRegistry.register("defaultCodeFormatter", codeElementFormatter);

    if(model.platformSettings.lang.equalsIgnoreCase("java"))
      this.modelProcessingManager = new ModelProcessingManager(ComponentRegistry.withName("codeGenerationSteps"));
    else
      this.modelProcessingManager = new ModelProcessingManager(ComponentRegistry.withName("cSharpCodeGenerationSteps"));

    return modelProcessingManager.generate(model, modelProcessingInformation, logger).andThenTo(scene -> {
              final Response.Status responseStatus = scene.isFailed() ? InternalServerError : Ok;
              return Completes.withSuccess(Response.of(responseStatus, serialized(scene.report)));
            });
  }

  public Completes<Response> makeGenerationPath(final GenerationPath path) {
    try {
      modelProcessingManager.createGenerationPath(new File(path.path));
      return Completes.withSuccess(Response.of(Created, headers(of(Location, path.path)), path.serialized()));
    } catch (final GenerationPathAlreadyExistsException e) {
      return Completes.withSuccess(Response.of(Conflict, path.serialized()));
    } catch (final GenerationPathCreationException e) {
      return Completes.withSuccess(Response.of(Forbidden, path.serialized()));
    }
  }

  public Completes<Response> processModelExportationFile(final DesignerModel model) {
    try {
      final DesignerModelFile settingsFile = DesignerModelFile.from(model);
      return Completes.withSuccess(Response.of(Ok, serialized(settingsFile)));
    } catch (final DesignerModelFileException exception) {
      final ModelFileHandlingReport report = ModelFileHandlingReport.onExportFail(exception);
      return Completes.withSuccess(Response.of(InternalServerError, serialized(report)));
    }
  }

  public Completes<Response> processModelImportationFile(final DesignerModelFile designerModelFile) {
    try {
      return Completes.withSuccess(Response.of(Ok, serialized(designerModelFile.mapData())));
    } catch (final DesignerModelFileException exception) {
      final ModelFileHandlingReport report = ModelFileHandlingReport.onImportFail(exception);
      return Completes.withSuccess(Response.of(InternalServerError, serialized(report)));
    }
  }

  public Completes<Response> queryModelProcessingInformation() {
    return Completes.withSuccess(Response.of(Ok, serialized(modelProcessingInformation)));
  }


  public Completes<Response> refuseRequest() {
    return Completes.withFailure(Response.of(TooManyRequests, serialized(modelProcessingInformation)));
  }

  @Override
  public Resource<?> routes() {
    return resource("Model Processing Resource", this,
            post("/api/model-processing")
                    .body(DesignerModel.class)
                    .handle(this::startGeneration),
            post("/api/model-processing/exportation-file")
                    .body(DesignerModel.class)
                    .handle(this::processModelExportationFile),
            post("/api/model-processing/importation-file")
                    .body(DesignerModelFile.class)
                    .handle(this::processModelImportationFile),
            post("/api/model-processing/paths")
                    .body(GenerationPath.class)
                    .handle(this::makeGenerationPath),
            get("/api/model-processing/info")
                    .handle(this::queryModelProcessingInformation),
            get(REFUSE_REQUEST_URI)
                    .handle(this::refuseRequest));
  }

}
