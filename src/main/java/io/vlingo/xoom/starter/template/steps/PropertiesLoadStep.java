// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import io.vlingo.xoom.starter.template.TemplateGenerationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.vlingo.xoom.starter.template.Resource.PROPERTIES_FILE;

public class PropertiesLoadStep implements TemplateGenerationStep {


    @Override
    public void process(final TemplateGenerationContext context) {
        context.onProperties(loadProperties());
    }

    private Properties loadProperties() {
        try {
            final Properties properties = new Properties();
            final File propertiesFile = new File(PROPERTIES_FILE.path());
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final IOException e) {
            throw new TemplateGenerationException(e);
        }
    }


}
