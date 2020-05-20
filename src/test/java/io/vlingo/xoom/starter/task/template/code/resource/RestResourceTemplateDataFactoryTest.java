// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.resource;

import io.vlingo.xoom.starter.task.template.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

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

        final List<RestResourceTemplateData> templateData =
                RestResourceTemplateDataFactory.build(basePackage, PROJECT_PATH, restResourcesData);

        final RestResourceTemplateData author = templateData.get(0);
        final RestResourceTemplateData book = templateData.get(1);

        Assertions.assertEquals(2, templateData.size());

        Assertions.assertEquals("Author", author.aggregateName);
        Assertions.assertEquals("io.vlingo.xoomapp.resource", author.packageName);
        Assertions.assertEquals(Paths.get(RESOURCE_PACKAGE_PATH, "AuthorResource.java").toString(), author.file().getAbsolutePath());

        Assertions.assertEquals("Book", book.aggregateName);
        Assertions.assertEquals("io.vlingo.xoomapp.resource", book.packageName);
        Assertions.assertEquals(Paths.get(RESOURCE_PACKAGE_PATH, "BookResource.java").toString(), book.file().getAbsolutePath());
    }

}
