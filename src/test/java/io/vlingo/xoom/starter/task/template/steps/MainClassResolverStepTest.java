// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;

public class MainClassResolverStepTest {

    @Test
    public void testThatDefaultMainClassIsResolved() {
        final TaskExecutionContext context = buildContext(false);
        new MainClassResolverStep().process(context);
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.Bootstrap", context.properties().get(MAIN_CLASS.literal()));
    }

    @Test
    public void testThatAnnotatedMainClassIsResolved() {
        final TaskExecutionContext context = buildContext(true);
        new MainClassResolverStep().process(context);
        Assertions.assertEquals("io.vlingo.xoomapp.infrastructure.XoomInitializer", context.properties().get(MAIN_CLASS.literal()));
    }

    private TaskExecutionContext buildContext(final Boolean useAnnotations) {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "io.vlingo.xoomapp");
        properties.put(ANNOTATIONS.literal(), useAnnotations.toString());

        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        context.onProperties(properties);

        return context;
    }

}