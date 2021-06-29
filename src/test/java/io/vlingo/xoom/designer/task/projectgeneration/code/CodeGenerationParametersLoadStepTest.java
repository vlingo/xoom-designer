// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.task.Agent.TERMINAL;
import static io.vlingo.xoom.designer.task.Agent.WEB;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

public class CodeGenerationParametersLoadStepTest {

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
        Infrastructure.clear();
        Profile.enableTestProfile();
        Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    }

    @AfterAll
    public static void clear() {
        Profile.disableTestProfile();
    }

}
