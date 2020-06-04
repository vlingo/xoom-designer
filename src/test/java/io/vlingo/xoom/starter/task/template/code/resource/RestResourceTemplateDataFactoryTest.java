// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.resource;

import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.TemplateData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.PACKAGE_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.REST_RESOURCE_NAME;

public class RestResourceTemplateDataFactoryTest {

    private static final String PROJECT_PATH =
            Terminal.supported().isWindows() ?
                    Paths.get("D:\\projects", "xoom-app").toString() :
                    Paths.get("/home", "xoom-app").toString();

    private static final String RESOURCE_PACKAGE_PATH =
            Paths.get(PROJECT_PATH, "src", "main", "java", "io", "vlingo", "xoomapp", "resource").toString();

    @Test
    public void testRestResourceTemplateDataBuild() {
        final String basePackage = "io.vlingo.xoomapp";
        final String restResourcesData = "Author;Book";

        final List<TemplateData> templateData =
                RestResourceTemplateDataFactory.build(basePackage, PROJECT_PATH, restResourcesData);

        Assertions.assertEquals(2, templateData.size());

        Assertions.assertEquals("AuthorResource", templateData.get(0).parameters().find(REST_RESOURCE_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.resource", templateData.get(0).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(RESOURCE_PACKAGE_PATH, "AuthorResource.java").toString(), templateData.get(0).file().getAbsolutePath());

        Assertions.assertEquals("BookResource", templateData.get(1).parameters().find(REST_RESOURCE_NAME));
        Assertions.assertEquals("io.vlingo.xoomapp.resource", templateData.get(1).parameters().find(PACKAGE_NAME));
        Assertions.assertEquals(Paths.get(RESOURCE_PACKAGE_PATH, "BookResource.java").toString(), templateData.get(1).file().getAbsolutePath());
    }

}
