// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class BootstrapGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatDefaultBootstrapIsGenerated() {
    final CodeGenerationContext context =
            CodeGenerationContext.empty().with(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED.name());

    loadParameters(context, false);
    loadContents(context);

    new BootstrapGenerationStep().process(context);

    final Content bootstrap = context.findContent(JavaTemplateStandard.BOOTSTRAP, "Bootstrap");

    Assertions.assertEquals(7, context.contents().size());
    Assertions.assertTrue(bootstrap.contains(TextExpectation.onJava().read("default-bootstrap")));
  }

  @Test
  public void testThatAnnotatedBootstrapIsGenerated() {
    final CodeGenerationContext context =
            CodeGenerationContext.empty().with(Label.PROJECTION_TYPE, ProjectionType.OPERATION_BASED.name());

    loadParameters(context, true);
    loadContents(context);

    new BootstrapGenerationStep().process(context);

    final Content bootstrap = context.findContent(JavaTemplateStandard.BOOTSTRAP, "Bootstrap");

    Assertions.assertEquals(7, context.contents().size());
    Assertions.assertTrue(bootstrap.contains(TextExpectation.onJava().read("annotated-bootstrap")));
  }

  private void loadParameters(final CodeGenerationContext context, final Boolean useAnnotation) {
    context.with(Label.PACKAGE, "io.vlingo.xoomapp").with(Label.APPLICATION_NAME, "xoom-app")
            .with(Label.TARGET_FOLDER, HOME_DIRECTORY).with(Label.STORAGE_TYPE, "STATE_STORE")
            .with(Label.CQRS, "true").with(Label.BLOCKING_MESSAGING, Boolean.TRUE.toString())
            .with(Label.USE_ANNOTATIONS, useAnnotation.toString());
  }

  private void loadContents(final CodeGenerationContext context) {
    context.addContent(JavaTemplateStandard.REST_RESOURCE, new OutputFile(RESOURCE_PACKAGE_PATH, "AuthorResource.java"), AUTHOR_RESOURCE_CONTENT);
    context.addContent(JavaTemplateStandard.REST_RESOURCE, new OutputFile(RESOURCE_PACKAGE_PATH, "BookResource.java"), BOOK_RESOURCE_CONTENT);
    context.addContent(JavaTemplateStandard.STORE_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "CommandModelStateStoreProvider.java"), COMMAND_MODEL_STORE_PROVIDER_CONTENT);
    context.addContent(JavaTemplateStandard.STORE_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "QueryModelStateStoreProvider.java"), QUERY_MODEL_STORE_PROVIDER_CONTENT);
    context.addContent(JavaTemplateStandard.EXCHANGE_BOOTSTRAP, new OutputFile(EXCHANGE_PACKAGE_PATH, "ExchangeBootstrap.java"), EXCHANGE_BOOTSTRAP_CONTENT);
    context.addContent(JavaTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.java"), PROJECTION_DISPATCHER_PROVIDER_CONTENT);
  }

  private static final String HOME_DIRECTORY = OperatingSystem.detect().isWindows() ? "D:\\projects" : "/home";

  private static final String PROJECT_PATH = Paths.get(HOME_DIRECTORY, "xoom-app").toString();

  private static final String RESOURCE_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "resource").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
          Paths.get(PROJECT_PATH, "src", "main", "java",
                  "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
          Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence").toString();

  private static final String EXCHANGE_PACKAGE_PATH =
          Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "exchange").toString();

  private static final String AUTHOR_RESOURCE_CONTENT =
          "package io.vlingo.xoomapp.resource; \\n" +
                  "public class AuthorResource { \\n" +
                  "... \\n" +
                  "}";

  private static final String BOOK_RESOURCE_CONTENT =
          "package io.vlingo.xoomapp.resource; \\n" +
                  "public class BookResource { \\n" +
                  "... \\n" +
                  "}";

  private static final String EXCHANGE_BOOTSTRAP_CONTENT =
          "package io.vlingo.xoomapp.infrastructure.exchange; \\n" +
                  "public class ExchangeBootstrap implements ExchangeInitializer { \\n" +
                  "... \\n" +
                  "}";

  private static final String COMMAND_MODEL_STORE_PROVIDER_CONTENT =
          "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                  "public class CommandModelStateStoreProvider { \\n" +
                  "... \\n" +
                  "}";

  private static final String QUERY_MODEL_STORE_PROVIDER_CONTENT =
          "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                  "public class QueryModelStateStoreProvider { \\n" +
                  "... \\n" +
                  "}";

  private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT =
          "package io.vlingo.xoomapp.infrastructure.persistence; \\n" +
                  "public class ProjectionDispatcherProvider { \\n" +
                  "... \\n" +
                  "}";

}