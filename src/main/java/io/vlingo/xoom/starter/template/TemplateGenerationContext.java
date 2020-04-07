// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template;

import io.vlingo.xoom.starter.archetype.ArchetypeProperties;

import java.util.Properties;

public class TemplateGenerationContext {

    private Process process;
    private Properties properties;

    public void followProcess(final Process process) {
        this.process = process;
    }

    public void onProperties(final Properties properties) {
        this.properties = properties;
    }

    public Process process() {
        return process;
    }

    public Properties properties() {
        return properties;
    }

    public String propertyOf(final ArchetypeProperties key) {
        return properties != null ? properties.getProperty(key.literal()) : "";
    }
}
