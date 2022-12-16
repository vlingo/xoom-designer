// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.TextBasedContent;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class BootstrapGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatDefaultBootstrapIsGenerated() {
    final CodeGenerationParameters parameters = CodeGenerationParameters.from(Label.PACKAGE, "Io.Vlingo.Xoomapp")
        .add(Label.APPLICATION_NAME, "Author")
        .add(Label.DIALECT, Dialect.C_SHARP);

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new BootstrapGenerationStep().process(context);

    final Content bootstrap = context.findContent(CsharpTemplateStandard.BOOTSTRAP, "Bootstrap");

    Assertions.assertEquals(3, context.contents().size());
    Assertions.assertEquals(((TextBasedContent)bootstrap).text, (TextExpectation.onCSharp().read("default-bootstrap")));
  }

  private Content[] contents() {
    return new Content[]{
        Content.with(CsharpTemplateStandard.REST_RESOURCE,
            new OutputFile(Paths.get(RESOURCE_PACKAGE_PATH).toString(), "AuthorResource.cs"), null, null,
            AUTHOR_RESOURCE_CONTENT),
        Content.with(CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "ProjectionDispatcherProvider.cs"), null, null,
            PROJECTION_DISPATCHER_PROVIDER_CONTENT),
    };
  }

  private static final String PROJECT_PATH = OperatingSystem.detect().isWindows()
      ? Paths.get("D:\\projects", "xoom-app").toString()
      : Paths.get("/home", "xoom-app").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH = Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();

  private static final String RESOURCE_PACKAGE_PATH = Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "Resource")
      .toString();
  private static final String PERSISTENCE_PACKAGE_PATH = Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "Persistence")
      .toString();

  private static final String AUTHOR_RESOURCE_CONTENT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Resource; \\n" +
          "public class AuthorResource { \\n" +
          "... \\n" +
          "}";

  private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.Persistence; \\n" +
          "public class ProjectionDispatcherProvider { \\n" +
          "... \\n" +
          "}";
}