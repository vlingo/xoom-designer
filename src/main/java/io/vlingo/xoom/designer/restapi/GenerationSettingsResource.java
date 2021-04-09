// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.restapi;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.Conflict;
import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.Forbidden;
import static io.vlingo.xoom.http.Response.Status.InternalServerError;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;
import static io.vlingo.xoom.designer.task.Task.WEB_BASED_PROJECT_GENERATION;

import java.io.File;
import java.util.List;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.serialization.JsonSerialization;
import io.vlingo.xoom.designer.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.restapi.data.TaskExecutionContextMapper;
import io.vlingo.xoom.designer.task.Task;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.TaskStatus;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;

public class GenerationSettingsResource extends DynamicResourceHandler {

    public GenerationSettingsResource(final Stage stage) {
        super(stage);
    }

    public Completes<Response> startGeneration(final GenerationSettingsData settings) {
        final String validationMessage = validate(settings);

        if(validationMessage.length() > 0) {
            logger().debug(validationMessage);
            return Completes.withFailure(Response.of(Conflict, serialized(validationMessage)));
        }

        return mapContext(settings).andThen(this::runProjectGeneration).andThenTo(this::buildResponse);
    }

    public Completes<Response> queryGenerationSettingsInformation() {
        final ProjectGenerationInformation information = ProjectGenerationInformation.load();
        return Completes.withSuccess(Response.of(Ok, serialized(information)));
    }

    public Completes<Response> makeGenerationPath(final GenerationPath path) {
      final File generationPath = new File(path.path);

      final String serializedPath = JsonSerialization.serialized(path);

      if (generationPath.exists() && generationPath.isDirectory() && generationPath.list().length > 0) {
        return Completes.withSuccess(Response.of(Conflict, serializedPath));
      }

      try {
        generationPath.mkdirs();
      } catch (Exception e) {
        return Completes.withSuccess(Response.of(Forbidden, serializedPath));
      }

      return Completes.withSuccess(Response.of(Created, headers(of(Location, path.path)), serializedPath));
    }

    private Completes<TaskExecutionContext> mapContext(final GenerationSettingsData settings) {
        try {
            return Completes.withSuccess(TaskExecutionContextMapper.from(settings));
        } catch (final Exception exception) {
            exception.printStackTrace();
            return Completes.withFailure(TaskExecutionContext.withoutOptions());
        }
    }

    private TaskStatus runProjectGeneration(final TaskExecutionContext context) {
        try {
            return Task.of(WEB_BASED_PROJECT_GENERATION, context).manage(context);
        } catch (final Exception exception) {
            exception.printStackTrace();
            return TaskStatus.FAILED;
        }
    }

    private Completes<Response> buildResponse(final TaskStatus taskStatus) {
        final Response.Status status = taskStatus.failed() ? InternalServerError : Ok;
        return Completes.withSuccess(Response.of(status, serialized(taskStatus)));
    }

    private String validate(final GenerationSettingsData settings) {
        final List<String> errorStrings = settings.validate();
        logger().debug("errorStrings: " + errorStrings);
        return String.join(", ", errorStrings);
    }

    @Override
    public Resource<?> routes() {
        return resource("Generation Settings Resource",
                post("/api/generation-settings")
                        .body(GenerationSettingsData.class)
                        .handle(this::startGeneration),
                get("/api/generation-settings/info")
                        .handle(this::queryGenerationSettingsInformation),
                post("/api/generation-paths")
                        .body(GenerationPath.class)
                        .handle(this::makeGenerationPath));
    }

}
