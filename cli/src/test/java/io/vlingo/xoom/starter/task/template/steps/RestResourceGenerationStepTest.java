// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;

public class RestResourceGenerationStepTest {

    @Test
    public void testRestResourceGeneration() {
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
        context.onProperties(loadProperties());
        new RestResourceGenerationStep().process(context);

        Assertions.assertEquals(2, context.contents().size());
        Assertions.assertEquals("OrderResource.java", context.contents().get(0).file.getName());
        Assertions.assertEquals("ProductResource.java", context.contents().get(1).file.getName());
        Assertions.assertTrue(context.contents().get(0).text.contains("class OrderResource"));
        Assertions.assertTrue(context.contents().get(0).text.contains("package com.company.context.logistics.resource;"));
        Assertions.assertTrue(context.contents().get(1).text.contains("class ProductResource"));
        Assertions.assertTrue(context.contents().get(1).text.contains("package com.company.context.logistics.resource;"));
    }

    private Properties loadProperties() {
        final Properties properties = new Properties();
        properties.put(PACKAGE.literal(), "com.company.context.logistics");
        properties.put(ARTIFACT_ID.literal(), "logistics");
        properties.put(REST_RESOURCES.literal(), "Order;Product");
        properties.put(TARGET_FOLDER.literal(), "/Projects/");
        return properties;
    }

}
