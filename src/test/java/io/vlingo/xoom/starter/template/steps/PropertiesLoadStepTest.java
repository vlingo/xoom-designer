// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.archetype.ArchetypeProperties;
import io.vlingo.xoom.starter.template.Resource;
import io.vlingo.xoom.starter.template.TemplateGenerationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class PropertiesLoadStepTest {

    private TemplateGenerationContext context;
    private static final String ROOT_FOLDER = Paths.get(System.getProperty("user.dir"), "dist", "starter").toString();

    @Test
    public void testPropertiesLoad(){
        new PropertiesLoadStep().process(context);
        Assert.assertNotNull(context.properties());
        Assert.assertEquals("1.0", context.propertyOf(ArchetypeProperties.VERSION));
        Assert.assertEquals("com.company", context.propertyOf(ArchetypeProperties.GROUP_ID));
        Assert.assertEquals("xoom-application", context.propertyOf(ArchetypeProperties.ARTIFACT_ID));
        Assert.assertEquals("com.company.business", context.propertyOf(ArchetypeProperties.PACKAGE));
        Assert.assertEquals("k8s", context.propertyOf(ArchetypeProperties.DEPLOYMENT));
        Assert.assertNotNull(context.propertyOf(ArchetypeProperties.TARGET_FOLDER));
    }

    @Before
    public void setUp() {
        this.context = new TemplateGenerationContext();
        Resource.clear();
        Resource.rootIn(ROOT_FOLDER);
    }

}
