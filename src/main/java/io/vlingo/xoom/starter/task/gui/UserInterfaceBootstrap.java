// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.gui;

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.StaticFilesConfiguration;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.annotation.initializer.Xoom;

import java.util.Arrays;

@Xoom(name = "xoom-starter")
@ResourceHandlers(packages = "io.vlingo.xoom.starter.restapi")
public class UserInterfaceBootstrap implements XoomInitializationAware {

    private static int DEFAULT_PORT = 19090;

    @Override
    public Configuration configureServer(final Stage stage, final String[] args) {
        final Configuration.Timing timing =
                Configuration.Timing.defineWith(7, 3, 100);

        final Configuration.Sizing sizing =
                Configuration.Sizing.define().withDispatcherPoolSize(2)
                        .withMaxBufferPoolSize(100)
                        .withMaxMessageSize(4096);

        return Configuration.define()
                .withPort(DEFAULT_PORT)
                .with(timing).with(sizing);
    }

    @Override
    public StaticFilesConfiguration staticFilesConfiguration() {
        return StaticFilesConfiguration.defineWith(100, "frontend", Arrays.asList("/"));
    }

    @Override
    public void onInit(final Stage stage) {
    }

}
