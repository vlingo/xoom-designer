// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.Resource;
import io.vlingo.xoom.starter.task.Task;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.template.Agent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.vlingo.xoom.starter.task.Property.*;

public class PropertiesLoadStepTest {

    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    @Test
    public void testPropertiesLoadFromFile(){
        final TaskExecutionContext context = TaskExecutionContext.withoutOptions();
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
    public void testPropertiesLoadFromArgs(){
        final TaskExecutionContext context = TaskExecutionContext.withArgs(args());
        new PropertiesLoadStep().process(context);
        Assertions.assertNotNull(context.properties());
        Assertions.assertEquals("1.0", context.propertyOf(VERSION));
        Assertions.assertEquals("com.company", context.propertyOf(GROUP_ID));
        Assertions.assertEquals("xoom-application", context.propertyOf(ARTIFACT_ID));
        Assertions.assertEquals("com.company.business", context.propertyOf(PACKAGE));
        Assertions.assertEquals("1.3.0", context.propertyOf(XOOM_SERVER_VERSION));
        Assertions.assertEquals("3", context.propertyOf(CLUSTER_NODES));
        Assertions.assertEquals("k8s", context.propertyOf(DEPLOYMENT));
        Assertions.assertEquals("/home/projects/xoom-app", context.propertyOf(TARGET_FOLDER));
        Assertions.assertEquals("xoom-app", context.propertyOf(DOCKER_IMAGE));
        Assertions.assertEquals("dambrosio/xoom-app", context.propertyOf(DOCKER_REPOSITORY));
        Assertions.assertEquals("vlingo/xoom-app", context.propertyOf(KUBERNETES_IMAGE));
        Assertions.assertEquals("xoom-app-pod", context.propertyOf(KUBERNETES_POD_NAME));
        Assertions.assertEquals("true", context.propertyOf(ANNOTATIONS));
    }

    private List<String> args() {
        return Arrays.asList(
            Task.TEMPLATE_GENERATION.command(),
            GROUP_ID.literal(), "com.company",
            ARTIFACT_ID.literal(), "xoom-application",
            VERSION.literal(), "1.0",
            PACKAGE.literal(), "com.company.business",
            XOOM_SERVER_VERSION.literal(), "1.3.0",
            CLUSTER_NODES.literal(), "3",
            DEPLOYMENT.literal(), "k8s",
            DOCKER_IMAGE.literal(), "xoom-app",
            DOCKER_REPOSITORY.literal(), "dambrosio/xoom-app",
            KUBERNETES_IMAGE.literal(), "vlingo/xoom-app",
            KUBERNETES_POD_NAME.literal(), "xoom-app-pod",
            TARGET_FOLDER.literal(), "/home/projects/xoom-app",
            ANNOTATIONS.literal(),  "true",
            Agent.argumentKey(), Agent.WEB.name()
        );
    }

    @BeforeEach
    public void setUp() {
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
    }

}
