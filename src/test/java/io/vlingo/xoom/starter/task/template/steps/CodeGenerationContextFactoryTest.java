// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.vlingo.xoom.codegen.CodeGenerationParameter.*;

public class CodeGenerationContextFactoryTest {

    @Test
    public void testCodeGenerationContextInstantiation() {
        final TaskExecutionContext taskExecutionContext =
                TaskExecutionContext.withoutOptions();

        loadProperties(taskExecutionContext);

        final CodeGenerationContext codeGenerationContext =
                CodeGenerationContextFactory.from(taskExecutionContext);

        Assertions.assertEquals("FirstAggregate;SecondAggregate", codeGenerationContext.parameterOf(AGGREGATES));
        Assertions.assertEquals("true", codeGenerationContext.parameterOf(ANNOTATIONS));
        Assertions.assertEquals("xoom-app-name", codeGenerationContext.parameterOf(APPLICATION_NAME));
        Assertions.assertEquals("POSTGRES", codeGenerationContext.parameterOf(COMMAND_MODEL_DATABASE));
        Assertions.assertEquals("false", codeGenerationContext.parameterOf(CQRS));
        Assertions.assertEquals("HSQLDB", codeGenerationContext.parameterOf(DATABASE));
        Assertions.assertEquals("io.vlingo.xoom", codeGenerationContext.parameterOf(PACKAGE));
        Assertions.assertEquals("true", codeGenerationContext.parameterOf(PROJECTIONS));
        Assertions.assertEquals("IN_MEMORY", codeGenerationContext.parameterOf(QUERY_MODEL_DATABASE));
        Assertions.assertEquals("FirstAggregate;SecondAggregate", codeGenerationContext.parameterOf(REST_RESOURCES));
        Assertions.assertEquals("STATE_STORE", codeGenerationContext.parameterOf(STORAGE_TYPE));
        Assertions.assertEquals("/home/projects", codeGenerationContext.parameterOf(TARGET_FOLDER));
    }

    private void loadProperties(final TaskExecutionContext context) {
        final Properties properties = new Properties();
        properties.put(Property.AGGREGATES.literal(), "FirstAggregate;SecondAggregate");
        properties.put(Property.ANNOTATIONS.literal(), "true");
        properties.put(Property.ARTIFACT_ID.literal(), "xoom-app-name");
        properties.put(Property.COMMAND_MODEL_DATABASE.literal(), "POSTGRES");
        properties.put(Property.CQRS.literal(), "false");
        properties.put(Property.DATABASE.literal(), "HSQLDB");
        properties.put(Property.PACKAGE.literal(), "io.vlingo.xoom");
        properties.put(Property.PROJECTIONS.literal(), "true");
        properties.put(Property.QUERY_MODEL_DATABASE.literal(), "IN_MEMORY");
        properties.put(Property.REST_RESOURCES.literal(), "FirstAggregate;SecondAggregate");
        properties.put(Property.STORAGE_TYPE.literal(), "STATE_STORE");
        properties.put(Property.TARGET_FOLDER.literal(), "/home/projects");
        context.onProperties(properties);
    }

}
