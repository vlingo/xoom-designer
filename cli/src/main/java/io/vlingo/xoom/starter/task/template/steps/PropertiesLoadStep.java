// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.ArgumentNotFoundException;
import io.vlingo.xoom.starter.ArgumentRetriever;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;
import io.vlingo.xoom.starter.task.template.Agent;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static io.vlingo.xoom.starter.Resource.STARTER_PROPERTIES_FILE;

public class PropertiesLoadStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
        context.onProperties(loadProperties(context));
    }

    private Properties loadProperties(final TaskExecutionContext context) {
        if (Agent.findAgent(context.args()).isTerminal()) {
            return loadFromFile();
        }
        return loadFromArgs(context);
    }

    private Properties loadFromArgs(final TaskExecutionContext context) {
        final Properties properties = new Properties();
        Arrays.asList(Property.values()).forEach(property -> {
            try {
                final String key = property.literal();
                final String value = ArgumentRetriever.retrieve(key, context.args());
                properties.put(property.literal(), value);
            } catch(final ArgumentNotFoundException exception) {
                if(!property.isOptional()) {
                    throw exception;
                }
                properties.put(property.literal(),  property.defaultValue());
            }
        });
        return properties;
    }

    private Properties loadFromFile() {
        try {
            final Properties properties = new Properties();
            final File propertiesFile = new File(STARTER_PROPERTIES_FILE.path());
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

}
