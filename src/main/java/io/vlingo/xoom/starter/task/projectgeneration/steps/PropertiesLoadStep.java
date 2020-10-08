// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.vlingo.xoom.starter.Resource.STARTER_PROPERTIES_FILE;

public class PropertiesLoadStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.onProperties(loadProperties());
    }

    private Properties loadProperties() {
        try {
            final Properties properties = new Properties();
            final File propertiesFile = new File(STARTER_PROPERTIES_FILE.path());
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.agent().isTerminal();
    }

}
