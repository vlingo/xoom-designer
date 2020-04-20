// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.archetype;

import io.vlingo.xoom.starter.task.template.archetype.Archetype;
import io.vlingo.xoom.starter.task.template.archetype.ArchetypeNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.*;

public class ArchetypeTest {

    @Test
    public void testBasicArchetypeRetrieval() {
        final Properties properties = loadBasicArchetypeProperties();
        Assert.assertEquals(Archetype.BASIC, Archetype.support(properties));
    }

    @Test
    public void testKubernetesArchetypeRetrieval() {
        final Properties properties = loadKubernetesArchetypeProperties();
        Assert.assertEquals(Archetype.KUBERNETES, Archetype.support(properties));
    }

    @Test(expected = ArchetypeNotFoundException.class)
    public void testBasicArchetypeRetrievalFailure() {
        final Properties properties = loadBasicArchetypeProperties();
        properties.remove(XOOM_SERVER_VERSION.literal());
        Archetype.support(properties);
    }

    @Test(expected = ArchetypeNotFoundException.class)
    public void testKubernetesArchetypeRetrievalFailure() {
        final Properties properties = loadKubernetesArchetypeProperties();
        properties.remove(KUBERNETES_IMAGE.literal());
        Archetype.support(properties);
    }

    private Properties loadBasicArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(VERSION.literal(), "1.0");
        properties.put(GROUP_ID.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "starter-example");
        properties.put(PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(TARGET_FOLDER.literal(), "E:\\projects");
        return properties;
    }

    private Properties loadKubernetesArchetypeProperties() {
        final Properties properties = new Properties();
        properties.put(VERSION.literal(), "1.0");
        properties.put(GROUP_ID.literal(), "io.vlingo");
        properties.put(ARTIFACT_ID.literal(), "starter-example");
        properties.put(PACKAGE.literal(), "io.vlingo.starterexample");
        properties.put(XOOM_SERVER_VERSION.literal(), "1.2.9");
        properties.put(TARGET_FOLDER.literal(), "E:\\projects");
        properties.put(DEPLOYMENT.literal(), "k8s");
        properties.put(DOCKER_IMAGE.literal(), "starter-example-image");
        properties.put(KUBERNETES_IMAGE.literal(), "starter-example-image");
        properties.put(KUBERNETES_POD_NAME.literal(), "starter-example-pod");
        return properties;
    }

}
