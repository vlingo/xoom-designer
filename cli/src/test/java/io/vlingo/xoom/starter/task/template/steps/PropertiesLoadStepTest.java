// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class PropertiesLoadStepTest {

    private TaskExecutionContext context;
    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    @Test
    public void testPropertiesLoad(){
        new PropertiesLoadStep().process(context);
        Assertions.assertNotNull(context.properties());
        Assertions.assertEquals("1.0", context.propertyOf(Property.VERSION));
        Assertions.assertEquals("com.company", context.propertyOf(Property.GROUP_ID));
        Assertions.assertEquals("xoom-application", context.propertyOf(Property.ARTIFACT_ID));
        Assertions.assertEquals("com.company.business", context.propertyOf(Property.PACKAGE));
        Assertions.assertEquals("k8s", context.propertyOf(Property.DEPLOYMENT));
        Assertions.assertNotNull(context.propertyOf(Property.TARGET_FOLDER));
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
        this.context = TaskExecutionContext.withoutOptions();
    }

}
