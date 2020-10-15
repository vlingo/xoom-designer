// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.docker.DockerCommandException;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.option.OptionName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class XoomPropertiesLoadStep implements TaskExecutionStep {

    private final static String XOOM_PROPERTIES_FILE = "vlingo-xoom.properties";
    private final static String[] FOLDERS = {"src", "main", "resources", XOOM_PROPERTIES_FILE};

    @Override
    public void process(final TaskExecutionContext context) {
        context.onProperties(loadProperties(context));
    }

    private Properties loadProperties(final TaskExecutionContext context) {
        try {
            final Properties properties = new Properties();

            final String currentDirectory =
                    context.optionValueOf(OptionName.CURRENT_DIRECTORY);

            final String path =
                    Paths.get(currentDirectory, FOLDERS).toString();

            final File propertiesFile = new File(path);
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final FileNotFoundException e) {
            throw new DockerCommandException("Cannot execute Docker commands: unable to find " + XOOM_PROPERTIES_FILE);
        } catch (final IOException e) {
            throw new DockerCommandException(e);
        }
    }
}
