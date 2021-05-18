// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.restapi;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.task.Task;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskStatus;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.*;
import io.vlingo.xoom.designer.task.reactjs.ReactJSProjectGenerator;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.serialization.JsonSerialization;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;
import java.util.List;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.designer.task.Task.WEB_BASED_PROJECT_GENERATION;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;
import static io.vlingo.xoom.http.resource.ResourceBuilder.*;

public class GenerationSettingsResource extends DynamicResourceHandler {

  private final GenerationTarget generationTarget;
  private final ProjectGenerationInformation generationInformation;
  public static final String REFUSE_REQUEST_URI = "/api/generation-settings/request-refusal";

  public GenerationSettingsResource(final Stage stage) {
    super(stage);
    this.generationTarget = ComponentRegistry.withType(GenerationTarget.class);
    this.generationInformation = ProjectGenerationInformation.from(generationTarget);
  }

  public Completes<Response> startGeneration(final GenerationSettingsData settings) {
    final String validationMessage = validate(settings);

    if(validationMessage.length() > 0) {
      logger().debug(validationMessage);
      return Completes.withFailure(Response.of(Conflict, serialized(validationMessage)));
    }

    return mapContext(settings)
            .andThen(this::runProjectGeneration)
            .andThenTo(this::buildResponse);
  }

  public Completes<Response> makeGenerationPath(final GenerationPath path) {
    final File generationPath = new File(path.path);

    final String serializedPath = JsonSerialization.serialized(path);

    if (generationPath.exists() && generationPath.isDirectory() && generationPath.list().length > 0) {
      return Completes.withSuccess(Response.of(Conflict, serializedPath));
    }

    try {
      generationPath.mkdirs();
    } catch (final Exception e) {
      return Completes.withSuccess(Response.of(Forbidden, serializedPath));
    }

    return Completes.withSuccess(Response.of(Created, headers(of(Location, path.path)), serializedPath));
  }

  public Completes<Response> downloadSettingsFile(final GenerationSettingsData settingsData) {
    try {
      final GenerationSettingsFile settingsFile = GenerationSettingsFile.from(settingsData);
      return Completes.withSuccess(Response.of(Ok, serialized(settingsFile)));
    } catch (final GenerationSettingFileException exception) {
      return Completes.withSuccess(Response.of(InternalServerError));
    }
  }

  public Completes<Response> processSettingsFile(final GenerationSettingsFile generationSettingsFile) {
    try {
      return Completes.withSuccess(Response.of(Ok, serialized(generationSettingsFile.mapData())));
    } catch (final GenerationSettingFileException exception) {
      return Completes.withSuccess(Response.of(InternalServerError));
    }
  }

  public Completes<Response> queryGenerationSettingsInformation() {
    return Completes.withSuccess(Response.of(Ok, serialized(generationInformation)));
  }

  public Completes<Response> refuseRequest() {
    return Completes.withFailure(Response.of(TooManyRequests, serialized(generationInformation)));
  }

  private Completes<TaskExecutionContext> mapContext(final GenerationSettingsData settings) {
    try {
      return Completes.withSuccess(TaskExecutionContextMapper.from(settings, generationTarget));
    } catch (final Exception exception) {
      exception.printStackTrace();
      return Completes.withFailure(TaskExecutionContext.withoutOptions());
    }
  }

  private TaskExecutionContext runProjectGeneration(final TaskExecutionContext context) {
    try {
      return Task.of(WEB_BASED_PROJECT_GENERATION, context).manage(context);
    } catch (final Exception exception) {
      exception.printStackTrace();
      context.changeStatus(TaskStatus.FAILED);
      return context;
    }
  }

  private Completes<Response> buildResponse(final TaskExecutionContext context) {
    final Response.Status responseStatus = context.status().failed() ? InternalServerError : Ok;
    return Completes.withSuccess(Response.of(responseStatus, serialized(ProjectGenerationReport.from(context, generationInformation))));
  }

  private String validate(final GenerationSettingsData settings) {
    final List<String> errorStrings = settings.validate();
    logger().debug("errorStrings: " + errorStrings);
    return String.join(", ", errorStrings);
  }

  @Override
  public Resource<?> routes() {
    return resource("Generation Settings Resource", this,
            post("/api/generation-settings")
                    .body(GenerationSettingsData.class)
                    .handle(this::startGeneration),
            post("/api/generation-settings/exportation-file")
                    .body(GenerationSettingsData.class)
                    .handle(this::downloadSettingsFile),
            post("/api/generation-settings/importation-file")
                    .body(GenerationSettingsFile.class)
                    .handle(this::processSettingsFile),
            post("/api/generation-settings/paths")
                    .body(GenerationPath.class)
                    .handle(this::makeGenerationPath),
            get("/api/generation-settings/info")
                    .handle(this::queryGenerationSettingsInformation),
            get(REFUSE_REQUEST_URI)
                    .handle(this::refuseRequest));
  }

}
