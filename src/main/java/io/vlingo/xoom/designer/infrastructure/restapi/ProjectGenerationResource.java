// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModelFileException;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsFile;
import io.vlingo.xoom.designer.infrastructure.restapi.report.ModelFileHandlingReport;
import io.vlingo.xoom.designer.infrastructure.restapi.report.ProjectGenerationReport;
import io.vlingo.xoom.designer.task.TaskOutput;
import io.vlingo.xoom.designer.task.projectgeneration.*;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class ProjectGenerationResource extends DynamicResourceHandler {

  private final Logger logger;
  private final GenerationTarget generationTarget;
  private final ProjectGenerationManager projectGenerationManager;
  private final ProjectGenerationInformation generationInformation;
  public static final String REFUSE_REQUEST_URI = "/api/generation-settings/request-refusal";

  public ProjectGenerationResource(final Stage stage) {
    super(stage);
    this.logger = stage().world().defaultLogger();
    this.projectGenerationManager = new ProjectGenerationManager();
    this.generationTarget = ComponentRegistry.withType(GenerationTarget.class);
    this.generationInformation = ProjectGenerationInformation.from(generationTarget);
  }

  public Completes<Response> startGeneration(final GenerationSettingsData settings) {
    return projectGenerationManager.generate(settings, generationInformation, logger).andThenTo(context -> {
              final ProjectGenerationReport report = context.retrieveOutput(TaskOutput.PROJECT_GENERATION_REPORT);
              final Response.Status responseStatus = report.status.failed() ? InternalServerError : Ok;
              return Completes.withSuccess(Response.of(responseStatus, serialized(report)));
            });
  }

  public Completes<Response> makeGenerationPath(final GenerationPath path) {
    try {
      projectGenerationManager.createGenerationPath(new File(path.path));
      return Completes.withSuccess(Response.of(Created, headers(of(Location, path.path)), path.serialized()));
    } catch (final GenerationPathAlreadyExistsException e) {
      return Completes.withSuccess(Response.of(Conflict, path.serialized()));
    } catch (final GenerationPathCreationException e) {
      return Completes.withSuccess(Response.of(Forbidden, path.serialized()));
    }
  }

  public Completes<Response> processModelExportationFile(final GenerationSettingsData settingsData) {
    try {
      final GenerationSettingsFile settingsFile = GenerationSettingsFile.from(settingsData);
      return Completes.withSuccess(Response.of(Ok, serialized(settingsFile)));
    } catch (final DesignerModelFileException exception) {
      final ModelFileHandlingReport report = ModelFileHandlingReport.onExportFail(exception);
      return Completes.withSuccess(Response.of(InternalServerError, serialized(report)));
    }
  }

  public Completes<Response> processModelImportationFile(final GenerationSettingsFile generationSettingsFile) {
    try {
      return Completes.withSuccess(Response.of(Ok, serialized(generationSettingsFile.mapData())));
    } catch (final DesignerModelFileException exception) {
      final ModelFileHandlingReport report = ModelFileHandlingReport.onImportFail(exception);
      return Completes.withSuccess(Response.of(InternalServerError, serialized(report)));
    }
  }

  public Completes<Response> queryGenerationSettingsInformation() {
    return Completes.withSuccess(Response.of(Ok, serialized(generationInformation)));
  }

  public Completes<Response> refuseRequest() {
    return Completes.withFailure(Response.of(TooManyRequests, serialized(generationInformation)));
  }

  @Override
  public Resource<?> routes() {
    return resource("Generation Settings Resource", this,
            post("/api/generation-settings")
                    .body(GenerationSettingsData.class)
                    .handle(this::startGeneration),
            post("/api/generation-settings/exportation-file")
                    .body(GenerationSettingsData.class)
                    .handle(this::processModelExportationFile),
            post("/api/generation-settings/importation-file")
                    .body(GenerationSettingsFile.class)
                    .handle(this::processModelImportationFile),
            post("/api/generation-settings/paths")
                    .body(GenerationPath.class)
                    .handle(this::makeGenerationPath),
            get("/api/generation-settings/info")
                    .handle(this::queryGenerationSettingsInformation),
            get(REFUSE_REQUEST_URI)
                    .handle(this::refuseRequest));
  }

}
