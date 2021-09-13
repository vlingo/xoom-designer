// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.restassured.http.ContentType;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.infrastructure.userinterface.UserInterfaceBootstrapStep;
import io.vlingo.xoom.designer.infrastructure.userinterface.XoomInitializer;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.vlingo.xoom.designer.task.Property.DESIGNER_SERVER_PORT;

public abstract class ProjectGenerationTest {

  public static void init() {
    Infrastructure.clear();
    ComponentRegistry.clear();
    Profile.enableTestProfile();
    Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    ComponentRegistry.register(GenerationTarget.class, GenerationTarget.FILESYSTEM);
    ComponentRegistry.register(DESIGNER_SERVER_PORT.literal(), PortDriver.init().findAvailable(19099, 20100));
    ComponentRegistry.register("defaultCodeFormatter", CodeElementFormatter.with(Dialect.findDefault(), ReservedWordsHandler.usingSuffix("_")));
    new UserInterfaceBootstrapStep().process(TaskExecutionContext.bare());
  }

  public void generateProjectFor(final String model){
    given().port(Infrastructure.DesignerServer.url().getPort())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(buildPayload(model))
            .post("/api/generation-settings")
            .then().statusCode(200);
  }

  private GenerationSettingsData buildPayload(final String model) {
    final String modelPath =
            String.format("/sample-models/%s/%s.json", modelDirectory(), model);

    try {
      final String generationPath = resolveGenerationPath(model);
      final InputStream modelStream = ProjectGenerationTest.class.getResourceAsStream(modelPath);
      final String modelJson = IOUtils.toString(modelStream, StandardCharsets.UTF_8.name());
      return JsonSerialization.deserialized(String.format(modelJson, generationPath), GenerationSettingsData.class);
    } catch (final IOException exception) {
      throw new RuntimeException(String.format("Failed to load Designer model from `%s`.", modelPath), exception);
    }
  }

  public abstract String modelDirectory();

  public static void clear() throws Exception {
    Infrastructure.clear();
    ComponentRegistry.clear();
    Profile.disableTestProfile();
    XoomInitializer.instance().stopServer();
  }

  private String resolveGenerationPath(final String model) {
    return Paths.get(System.getProperty("user.dir"), "target", "project-generation", model)
            .toString().replace("\\", "\\\\");
  }

}
