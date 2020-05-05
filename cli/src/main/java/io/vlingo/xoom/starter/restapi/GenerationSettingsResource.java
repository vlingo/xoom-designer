// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi;

import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.starter.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.starter.task.TaskExecutor;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.post;

@Resource
public class GenerationSettingsResource implements Endpoint {

    public Completes<Response> startGeneration(final GenerationSettingsData settings) {
        return response(Ok, Completes.withSuccess(settings.toArguments())
                .andThenConsume(args -> TaskExecutor.execute(args)));
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[] {
                post("/api/generation-settings")
                    .body(GenerationSettingsData.class)
                    .handle(this::startGeneration)
                    .onError(this::getErrorResponse)
        };
    }

    @Override
    public String getName() {
        return "GenerationSettings";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}
