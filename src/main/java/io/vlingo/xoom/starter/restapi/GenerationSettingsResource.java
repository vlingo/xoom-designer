// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;
import io.vlingo.xoom.starter.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.starter.restapi.data.TaskExecutionContextMapper;
import io.vlingo.xoom.starter.task.Task;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.TaskStatus;
import io.vlingo.xoom.starter.task.projectgeneration.SupportedTypes;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.InternalServerError;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.Headers;
import static io.vlingo.http.resource.ResourceBuilder.*;
import static io.vlingo.xoom.starter.Configuration.GENERATION_SETTINGS_RESPONSE_HEADER;
import static io.vlingo.xoom.starter.task.Task.WEB_BASED_PROJECT_GENERATION;

public class GenerationSettingsResource extends DynamicResourceHandler {

    public GenerationSettingsResource(final Stage stage) {
        super(stage);
    }

    public Completes<Response> startGeneration(final GenerationSettingsData settings) {
        try {
            final Completes<TaskExecutionContext> executionContext =
                    Completes.withSuccess(TaskExecutionContextMapper.from(settings));

            return executionContext.andThen(this::runProjectGeneration).andThenTo(this::buildSuccessfulResponse);
        } catch (final Exception exception) {
            logger().error(exception.getMessage());
            exception.printStackTrace();
            return Completes.withFailure(Response.of(InternalServerError));
        }
    }

    private Completes<Response> runProjectGeneration(final TaskExecutionContext context) {
        try {
            return buildSuccessfulResponse(Task.of(WEB_BASED_PROJECT_GENERATION, context).manage(context));
        } catch (final Exception exception) {
            exception.printStackTrace();
            logger().error(exception.getMessage());
            return Completes.withSuccess(Response.of(InternalServerError, serialized(TaskStatus.FAILED)));
        }
    }

    public Completes<Response> supportedTypes() {
        return Completes.withSuccess(SupportedTypes.names()).andThenTo(this::buildSuccessfulResponse);
    }

    private Completes<Response> buildSuccessfulResponse(final Object payload) {
        return Completes.withSuccess(Response.of(Ok, Headers.of(GENERATION_SETTINGS_RESPONSE_HEADER), serialized(payload)));
    }

    @Override
    public Resource<?> routes() {
        return resource("Generation Settings Resource",
                post("/api/generation-settings")
                    .body(GenerationSettingsData.class)
                    .handle(this::startGeneration),
                options("/api/generation-settings/field-types")
                    .handle(this::supportedTypes));
    }

}
