// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ApplicationConfigLoaderStep implements TaskExecutionStep {

    private static final String APPLICATION_CONFIG_FILE = "application.yml";

    @Override
    public void process(final TaskExecutionContext context) {
        final InputStream inputStream =
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(APPLICATION_CONFIG_FILE);

        context.onConfiguration(new Yaml().load(inputStream));
    }

}
