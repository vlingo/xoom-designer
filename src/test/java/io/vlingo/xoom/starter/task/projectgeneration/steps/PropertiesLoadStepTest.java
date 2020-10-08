// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Task;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.Agent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.task.Agent.TERMINAL;
import static io.vlingo.xoom.starter.task.Agent.WEB;
import static io.vlingo.xoom.starter.task.Property.*;

public class PropertiesLoadStepTest {

    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    @Test
    public void testThatPropertiesAreLoaded(){
        final TaskExecutionContext context = TaskExecutionContext.executedFrom(TERMINAL);
        new PropertiesLoadStep().process(context);
        Assertions.assertNotNull(context.properties());
        Assertions.assertEquals("1.0", context.propertyOf(VERSION));
        Assertions.assertEquals("com.company", context.propertyOf(GROUP_ID));
        Assertions.assertEquals("xoom-application", context.propertyOf(ARTIFACT_ID));
        Assertions.assertEquals("com.company.business", context.propertyOf(PACKAGE));
        Assertions.assertEquals("DOCKER", context.propertyOf(DEPLOYMENT));
        Assertions.assertEquals("true", context.propertyOf(ANNOTATIONS));
        Assertions.assertNotNull(context.propertyOf(TARGET_FOLDER));
    }

    @Test
    public void testThatStepShouldProcess() {
        Assertions.assertTrue(new PropertiesLoadStep().shouldProcess(TaskExecutionContext.executedFrom(TERMINAL)));
    }

    @Test
    public void testThatStepShouldNotProcess() {
        Assertions.assertFalse(new PropertiesLoadStep().shouldProcess(TaskExecutionContext.executedFrom(WEB)));
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
    }

}
