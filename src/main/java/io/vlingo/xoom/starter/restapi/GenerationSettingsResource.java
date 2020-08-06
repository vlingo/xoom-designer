// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Header.Headers;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import io.vlingo.xoom.starter.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.starter.task.TaskExecutor;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;
import static io.vlingo.xoom.starter.Configuration.GENERATION_SETTINGS_RESPONSE_HEADER;

public class GenerationSettingsResource extends ResourceHandler {

    public GenerationSettingsResource(final Stage stage) {}

    public Completes<Response> startGeneration(final GenerationSettingsData settings) {
        return Completes.withSuccess(settings.toArguments())
                .andThenConsume(args -> TaskExecutor.execute(args))
                .andThen(args -> serialized(args))
                .andThen(body -> Response.of(Ok, Headers.of(GENERATION_SETTINGS_RESPONSE_HEADER), body));
    }

    @Override
    public Resource<?> routes() {
        return resource("Generation Settings Resource",
                post("/api/generation-settings")
                    .body(GenerationSettingsData.class)
                    .handle(this::startGeneration));
    }

}
