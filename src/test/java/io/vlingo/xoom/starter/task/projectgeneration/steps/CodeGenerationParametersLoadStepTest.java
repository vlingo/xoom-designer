// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static io.vlingo.xoom.codegen.parameter.Label.*;
import static io.vlingo.xoom.starter.task.Agent.TERMINAL;
import static io.vlingo.xoom.starter.task.Agent.WEB;

public class CodeGenerationParametersLoadStepTest {

    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    @Test
    public void testThatPropertiesAreLoaded(){
        final TaskExecutionContext context = TaskExecutionContext.executedFrom(TERMINAL);
        new CodeGenerationParametersLoadStep().process(context);
        Assertions.assertFalse(context.codeGenerationParameters().isEmpty());
        Assertions.assertEquals("1.0.0", context.codeGenerationParameters().retrieveValue(VERSION));
        Assertions.assertEquals("com.company", context.codeGenerationParameters().retrieveValue(GROUP_ID));
        Assertions.assertEquals("xoom-application", context.codeGenerationParameters().retrieveValue(ARTIFACT_ID));
        Assertions.assertEquals("com.company.business", context.codeGenerationParameters().retrieveValue(PACKAGE));
        Assertions.assertEquals("true", context.codeGenerationParameters().retrieveValue(USE_ANNOTATIONS));
        Assertions.assertEquals("MYSQL", context.codeGenerationParameters().retrieveValue(COMMAND_MODEL_DATABASE));
        Assertions.assertEquals("true", context.codeGenerationParameters().retrieveValue(CQRS));
        Assertions.assertEquals("HSQLDB", context.codeGenerationParameters().retrieveValue(DATABASE));
        Assertions.assertEquals("EVENT_BASED", context.codeGenerationParameters().retrieveValue(PROJECTION_TYPE));
        Assertions.assertEquals("YUGA_BYTE", context.codeGenerationParameters().retrieveValue(QUERY_MODEL_DATABASE));
        Assertions.assertEquals("true", context.codeGenerationParameters().retrieveValue(USE_AUTO_DISPATCH));
        Assertions.assertEquals("STATE_STORE", context.codeGenerationParameters().retrieveValue(STORAGE_TYPE));
        Assertions.assertEquals("DOCKER", context.codeGenerationParameters().retrieveValue(DEPLOYMENT));
        Assertions.assertEquals("/home/projects", context.codeGenerationParameters().retrieveValue(TARGET_FOLDER));
        //Assertions.assertEquals(context.codeGenerationParameters().retrieveValue(AGGREGATES), "FirstAggregate;SecondAggregate");
    }

    @Test
    public void testThatStepShouldProcess() {
        Assertions.assertTrue(new CodeGenerationParametersLoadStep().shouldProcess(TaskExecutionContext.executedFrom(TERMINAL)));
    }

    @Test
    public void testThatStepShouldNotProcess() {
        Assertions.assertFalse(new CodeGenerationParametersLoadStep().shouldProcess(TaskExecutionContext.executedFrom(WEB)));
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
    }


}
