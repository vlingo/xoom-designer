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
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import io.vlingo.xoom.starter.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.starter.restapi.data.TaskExecutionContextMapper;
import io.vlingo.xoom.starter.task.Task;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.SupportedTypes;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;
import static io.vlingo.xoom.starter.Configuration.GENERATION_SETTINGS_RESPONSE_HEADER;
import static io.vlingo.xoom.starter.task.Task.WEB_BASED_PROJECT_GENERATION;

public class GenerationSettingsResource extends ResourceHandler {

    private final Stage stage;

    public GenerationSettingsResource(final Stage stage) {
        this.stage = stage;
    }

    public Completes<Response> startGeneration(final GenerationSettingsData settings) {
        try {
            final TaskExecutionContext executionContext = TaskExecutionContextMapper.from(settings);
            return Completes.withSuccess(executionContext).andThen(context -> {
                try {
                    return Task.of(WEB_BASED_PROJECT_GENERATION, context).manage(context);
                } catch (Exception exception) {
                    stage.world().defaultLogger().error(exception.getMessage());
                    return TaskExecutionContext.withoutOptions();
                }}).andThenTo(taskStatus -> Completes.withSuccess(Response.of(Ok, Headers.of(GENERATION_SETTINGS_RESPONSE_HEADER), serialized(taskStatus))));
        } catch (final Exception exception) {
            exception.printStackTrace();
            stage.world().defaultLogger().error(exception.getMessage());
            return Completes.withSuccess(Response.of(InternalServerError));
        }
    }

    public Completes<Response> supportedTypes() {
        return Completes.withSuccess(SupportedTypes.names())
                .andThenTo(typeNames -> Completes.withSuccess(Response.of(Ok, serialized(typeNames))));
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
